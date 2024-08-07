package com.example.java_actor_test;

import com.example.java_actor_test.exception.InvalidCredentialsException;
import com.example.java_actor_test.exception.TokenGenerationException;
import com.example.java_actor_test.service.async.SimpleAsyncTokenService;
import com.example.java_actor_test.service.async.TokenGenerationService;
import com.example.java_actor_test.service.async.UserValidationService;
import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.UserToken;
import com.example.java_actor_test.user.credential.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class JavaActorTestApplicationTests {

    @Mock
    private UserValidationService userValidationService;

    @Mock
    private TokenGenerationService tokenGenerationService;

    private SimpleAsyncTokenService simpleAsyncTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        simpleAsyncTokenService = new SimpleAsyncTokenService(userValidationService, tokenGenerationService);
    }

    @Test
    void testAuthenticateSuccess() throws ExecutionException, InterruptedException {
        Credentials credentials = new Credentials("testUser", "TESTUSER");
        User expectedUser = new User("testUser");
        when(userValidationService.validateCredentials(credentials))
                .thenReturn(CompletableFuture.completedFuture(expectedUser));

        CompletableFuture<User> result = simpleAsyncTokenService.authenticate(credentials);

        assertEquals(expectedUser, result.get());
        verify(userValidationService).validateCredentials(credentials);
    }

    @Test
    void testAuthenticateFailure() {
        Credentials credentials = new Credentials("testUser", "wrongPassword");
        when(userValidationService.validateCredentials(credentials))
                .thenReturn(CompletableFuture.failedFuture(new InvalidCredentialsException("Invalid credentials")));

        CompletableFuture<User> result = simpleAsyncTokenService.authenticate(credentials);

        assertThrows(ExecutionException.class, result::get);
        verify(userValidationService).validateCredentials(credentials);
    }

    @Test
    void testRequestTokenSuccess() throws ExecutionException, InterruptedException {
        User user = new User("testUser");
        UserToken expectedToken = new UserToken("testUser_2023-04-14T12:00:00Z");
        when(tokenGenerationService.generateToken(user))
                .thenReturn(CompletableFuture.completedFuture(expectedToken));

        CompletableFuture<UserToken> result = simpleAsyncTokenService.requestToken(user);

        assertEquals(expectedToken, result.get());
        verify(tokenGenerationService).generateToken(user);
    }

    @Test
    void testRequestTokenFailure() {
        User user = new User("ATestUser");
        when(tokenGenerationService.generateToken(user))
                .thenReturn(CompletableFuture.failedFuture(new TokenGenerationException("Cannot generate token for users starting with 'A'")));

        CompletableFuture<UserToken> result = simpleAsyncTokenService.requestToken(user);

        assertThrows(ExecutionException.class, result::get);
        verify(tokenGenerationService).generateToken(user);
    }
}