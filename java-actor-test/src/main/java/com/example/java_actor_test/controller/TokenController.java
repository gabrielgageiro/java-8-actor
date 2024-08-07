package com.example.java_actor_test.controller;

import com.example.java_actor_test.exception.InvalidCredentialsException;
import com.example.java_actor_test.exception.TokenGenerationException;
import com.example.java_actor_test.service.async.SimpleAsyncTokenService;
import com.example.java_actor_test.user.UserToken;
import com.example.java_actor_test.user.credential.Credentials;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final SimpleAsyncTokenService tokenService;

    public TokenController(SimpleAsyncTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<UserToken>> issueToken(@RequestBody Credentials credentials) {
        return tokenService.issueToken(credentials)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    if (ex.getCause() instanceof InvalidCredentialsException) {

                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                    } else if (ex.getCause() instanceof TokenGenerationException) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

                });
    }
}