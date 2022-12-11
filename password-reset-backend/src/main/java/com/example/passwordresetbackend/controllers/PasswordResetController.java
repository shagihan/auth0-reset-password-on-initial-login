package com.example.passwordresetbackend.controllers;

import com.auth0.exception.Auth0Exception;
import com.example.passwordresetbackend.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping(path = "api/v1/reset-password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping
    public ResponseEntity<Object> resetPassword(String hash, String password) {
        String hashData = new String(Base64.getDecoder().decode(hash));
        String[] split = hashData.split(":");
        try {
            if (passwordResetService.resetPassword(split[0], split[1], password))
                return ResponseEntity.status(HttpStatus.OK).build();
            else
                return ResponseEntity.badRequest().body("Invalid or expired hash provided with request");
        } catch (Auth0Exception e) {
            throw new RuntimeException(e);
        }
    }
}
