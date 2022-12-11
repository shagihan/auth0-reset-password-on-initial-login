package com.example.passwordresetbackend.service;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.example.passwordresetbackend.utils.Auth0Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PasswordResetService {

    @Autowired
    private Auth0Utils auth0Utils;

    public boolean resetPassword(String uuid, String hash, String password) throws Auth0Exception {
        User auth0UserFromUUID = auth0Utils.getAuth0UserFromUUID(uuid);
        String passwordResetHash = auth0UserFromUUID.getUserMetadata().get("passwordResetHash").toString();
        String passwordResetHashCreated = auth0UserFromUUID.getUserMetadata().get("passwordResetHashCreated").toString();
        if (isHashValid(passwordResetHash, passwordResetHashCreated, hash)) {
            User user = new User();
            user.setPassword(password.toCharArray());
            Map<String, Object> userMetadata = new HashMap<>();
            userMetadata.put("passwordResetHash", "");
            userMetadata.put("passwordResetHashCreated", "");
            userMetadata.put("isResetPasswordOnFirstLogin", false);
            user.setUserMetadata(userMetadata);
            auth0Utils.updateUser(uuid, user);
            return true;
        }
        return false;
    }

    @SneakyThrows
    private String getRandomPasswordResetHash(String uuid) {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return Base64.getUrlEncoder().withoutPadding().encodeToString(md.digest(uuid.getBytes(StandardCharsets.UTF_8)));
    }

    private boolean isHashValid(String hash, String timestamp, String hashFromRequest) {
        long currentTimestamp = Date.from(Instant.now()).getTime();
        long auth0timestamp = Long.parseLong(timestamp);

        return hash.equals(getRandomPasswordResetHash(hashFromRequest)) && (currentTimestamp < (auth0timestamp + 1800 * 1000));
    }
}
