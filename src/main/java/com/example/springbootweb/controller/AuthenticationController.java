package com.example.springbootweb.controller;

import com.example.springbootweb.dto.request.AuthenticationRequest;
import com.example.springbootweb.dto.request.IntrospectRequest;
import com.example.springbootweb.dto.respone.ApiResponse;
import com.example.springbootweb.dto.respone.AuthenticationResponse;
import com.example.springbootweb.dto.respone.IntrospectResponse;
import com.example.springbootweb.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
     private final AuthenticationService authenticationService;

     @PostMapping("/login")
     public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
         AuthenticationResponse response = authenticationService.authenticate(request);

         return ApiResponse.<AuthenticationResponse>builder()
                 .success(true)
                 .result(response)
                 .build();
     }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@Valid @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse response = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .success(true)
                .result(response)
                .build();
    }
}
