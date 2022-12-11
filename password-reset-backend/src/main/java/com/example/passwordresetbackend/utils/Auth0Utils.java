package com.example.passwordresetbackend.utils;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Auth0Utils {
    private ManagementAPI mgmtAPI = null;
    private AuthAPI authAPI = null;
    private AuthRequest authRequest = null;
    private TokenHolder tokenHolder = null;
    private String AUTH0_CONNECTION;
    private String AUTH0_CLIENT_ID;
    private String AUTH0_CLIENT_SECRET;
    private String AUTH0_DOMAIN;
    private String AUTH0_APP_AUDIENCE;

    @SneakyThrows
    private Auth0Utils(@Value("${auth0.domain}") String AUTH0_DOMAIN,
                       @Value("${auth0.clientId}") String AUTH0_CLIENT_ID,
                       @Value("${auth0.clientSecret}") String AUTH0_CLIENT_SECRET,
                       @Value("${auth0.connection}") String AUTH0_CONNECTION,
                       @Value("${auth0.audience}") String AUTH0_APP_AUDIENCE) {
        this.AUTH0_CONNECTION = AUTH0_CONNECTION;
        this.AUTH0_CLIENT_SECRET = AUTH0_CLIENT_SECRET;
        this.AUTH0_CLIENT_ID = AUTH0_CLIENT_ID;
        this.AUTH0_DOMAIN = AUTH0_DOMAIN;
        this.AUTH0_APP_AUDIENCE = AUTH0_APP_AUDIENCE;
        if (mgmtAPI == null) {
            log.debug("Initiating management auth0 client authentication : " + AUTH0_CLIENT_ID);
            authAPI = new AuthAPI(AUTH0_DOMAIN, AUTH0_CLIENT_ID, AUTH0_CLIENT_SECRET);
            authRequest = authAPI.requestToken(AUTH0_APP_AUDIENCE);
            tokenHolder = authRequest.execute();
            mgmtAPI = new ManagementAPI(AUTH0_DOMAIN, tokenHolder.getAccessToken());
        }
    }

    public com.auth0.json.mgmt.users.User getAuth0UserFromUUID(String uid) throws Auth0Exception {
        com.auth0.json.mgmt.users.User auth0User = mgmtAPI.users().get(uid, new UserFilter()).execute();
        return auth0User;
    }

    public com.auth0.json.mgmt.users.User updateUser(String uuid, com.auth0.json.mgmt.users.User user)
            throws Auth0Exception {
        return mgmtAPI.users().update(uuid, user).execute();

    }
}
