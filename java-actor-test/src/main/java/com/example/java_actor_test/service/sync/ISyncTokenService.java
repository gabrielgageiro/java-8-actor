package com.example.java_actor_test.service.sync;

import com.example.java_actor_test.user.User;
import com.example.java_actor_test.user.UserToken;
import com.example.java_actor_test.user.credential.Credentials;

public interface ISyncTokenService {
    User authenticate(Credentials credentials);
    UserToken requestToken(User user);

    default UserToken issueToken(Credentials credentials) {
        User user = authenticate(credentials);
        return requestToken(user);
    }
}
