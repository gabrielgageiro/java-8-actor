package com.example.java_actor_test.service.async;


import com.example.java_actor_test.exception.InvalidCredentialsException;
import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.credential.Credentials;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class UserValidationService {
    private final Random random = new Random();

    public CompletableFuture<User> validateCredentials(Credentials credentials) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(random.nextInt(5001)); // Random delay between 0-5000 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (credentials != null && credentials.getPassword().equals(credentials.getUsername().toUpperCase())) {
                return new User(credentials.getUsername());
            } else {
                throw new RuntimeException(new InvalidCredentialsException("Invalid credentials"));
            }

        });
    }
}