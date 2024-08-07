package com.example.java_actor_test.service.async;

import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.UserToken;
import com.example.java_actor_test.user.credential.Credentials;

import java.util.concurrent.CompletableFuture;

public interface IAsyncTokenService {
    CompletableFuture<User> authenticate(Credentials credentials);
    CompletableFuture<UserToken> requestToken(User user);

    default CompletableFuture<UserToken> issueToken(Credentials credentials) {
        return authenticate(credentials)
                .thenCompose(this::requestToken);
    }
}
