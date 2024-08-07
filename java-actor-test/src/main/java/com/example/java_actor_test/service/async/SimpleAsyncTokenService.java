package com.example.java_actor_test.service.async;
import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.UserToken;
import com.example.java_actor_test.user.credential.Credentials;

import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class SimpleAsyncTokenService implements IAsyncTokenService {

    private final UserValidationService userValidationService;
    private final TokenGenerationService tokenGenerationService;

    public SimpleAsyncTokenService(UserValidationService userValidationService, TokenGenerationService tokenGenerationService) {
        this.userValidationService = userValidationService;
        this.tokenGenerationService = tokenGenerationService;
    }

    @Override
    public CompletableFuture<User> authenticate(Credentials credentials) {
        return userValidationService.validateCredentials(credentials);
    }

    @Override
    public CompletableFuture<UserToken> requestToken(User user) {
        return tokenGenerationService.generateToken(user);
    }
}