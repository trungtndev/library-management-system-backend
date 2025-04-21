package com.example.springbootweb.service;

import com.example.springbootweb.dto.respone.IntrospectResponse;
import com.example.springbootweb.dto.request.AuthenticationRequest;
import com.example.springbootweb.dto.request.IntrospectRequest;
import com.example.springbootweb.dto.respone.AuthenticationResponse;
import com.example.springbootweb.entity.Role;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.enums.UserRole;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_STRING;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

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

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // Check if the token is valid
        String token = request.getToken();

        // Define the JWS verifier
        JWSVerifier verifier = new MACVerifier(SIGNER_STRING.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Verify the token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean isVerified = signedJWT.verify(verifier);


        return IntrospectResponse.builder()
                .isVerified(expirationTime.after(new Date()) && isVerified)
                .build();
    }

    private String _generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("library.com")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                Instant.now().plusSeconds(3600).toEpochMilli() // 1 hour
                        )
                )
                .claim("scope", _buildScope(user.getRoles()))
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
                    if (!CollectionUtils.isEmpty(role.getPermissions()))
                        role.getPermissions().forEach(
                                permission -> stringJoiner.add(permission.getName())
                        );
                }
        );
        return stringJoiner.toString();
    }
}
