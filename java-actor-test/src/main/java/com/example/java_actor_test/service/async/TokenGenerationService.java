package com.example.java_actor_test.service.async;

import com.example.java_actor_test.exception.TokenGenerationException;
import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.UserToken;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class TokenGenerationService {
    private final Random random = new Random();

    public CompletableFuture<UserToken> generateToken(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(random.nextInt(5001)); // Random delay between 0-5000 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (user.getId().startsWith("A")) {
                throw new TokenGenerationException("Cannot generate token for users starting with 'A'");
            }

            String token = user.getId() + "_" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
            return new UserToken(token);
        });
    }
}