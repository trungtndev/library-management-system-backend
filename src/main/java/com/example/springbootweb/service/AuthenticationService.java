package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.AuthenticationRequest;
import com.example.springbootweb.dto.request.IntrospectRequest;
import com.example.springbootweb.dto.request.LogoutRequest;
import com.example.springbootweb.dto.request.RefreshRequest;
import com.example.springbootweb.dto.respone.AuthenticationResponse;
import com.example.springbootweb.dto.respone.IntrospectResponse;
import com.example.springbootweb.entity.InvalidatedToken;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.repository.InvalidatedTokenRepository;
import com.example.springbootweb.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_STRING;

    @NonFinal
    @Value("${jwt.duration.access-token}")
    protected long ACCESS_TOKEN_EXPIRATION_TIME;

    @NonFinal
    @Value("${jwt.duration.refresh-token}")
    protected long REFRESH_TOKEN_EXPIRATION_TIME;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.getRole().forEach(role -> log.info(role.getName()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // Check if the password matches
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String token = _generateToken(user);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(authenticated);
        response.setToken(token);

        return response;
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException, AppException {
        // Get token from the request
        String token = request.getToken();
        boolean isVerified = true;
        try {
            _verifyToken(token, false);
        } catch (AppException e) {
            isVerified = false;
        }

        return IntrospectResponse.builder()
                .isVerified(isVerified)
                .build();
    }

    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {
        try {
            String token = request.getToken();
            SignedJWT signedJWT = _verifyToken(token, true);

            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiration);

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (ParseException | JOSEException e) {
            log.info(e.getMessage());
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest token)
            throws ParseException, JOSEException {
        // Verify the token
        SignedJWT signedJWT = _verifyToken(token.getToken(), true);
        // Get the token ID and expiration time from the JWT claims
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        // logout the token
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiration);
        invalidatedTokenRepository.save(invalidatedToken);

        // Generate a new token
        String newToken = _generateToken(
                userRepository.findByUsername(
                        signedJWT.getJWTClaimsSet().getSubject()
                ).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED))
        );
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(newToken);
        response.setAuthenticated(true);

        return response;
    }

    private SignedJWT _verifyToken(String token, boolean isRefresh)
            throws JOSEException, ParseException {
        // Define the JWS verifier
        JWSVerifier verifier = new MACVerifier(SIGNER_STRING.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Verify the token
        Date expirationTime = isRefresh ?
                new Date(
                        signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_TOKEN_EXPIRATION_TIME, ChronoUnit.SECONDS).toEpochMilli() // 1 hour
                ) :
                signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isVerified = signedJWT.verify(verifier);

        if (!(expirationTime.after(new Date()) && isVerified))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        // Check if the token is in the invalidated tokens in database
        if (invalidatedTokenRepository.existsById(
                signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return signedJWT;
    }

    private String _generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("library.com")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                Instant.now()
                                        .plus(ACCESS_TOKEN_EXPIRATION_TIME, ChronoUnit.SECONDS)
                                        .toEpochMilli() // 1 hour
                        )
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", _buildScope(user.getRole()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(SIGNER_STRING.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWT", e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String _buildScope(Set<Role> roles) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        roles.forEach(
                role -> {
                    stringJoiner.add(role.getName());
//                    if (!CollectionUtils.isEmpty(role.getPermission()))
//                        role.getPermission().forEach(
//                                permission -> stringJoiner.add(permission.getName())
//                        );
                }
        );
        return stringJoiner.toString();
    }
}
