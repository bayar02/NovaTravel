package tn.esprit.utils;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GoogleAuthService {
    private static final String CLIENT_ID = "461955083842-9gvj3rut34de2bijpcasjq5isa4eb21i.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String CALLBACK_URL = "http://localhost:8080/callback"; // Change if needed
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile"; // Permissions requested

    private final OAuth20Service service;

    public GoogleAuthService() {
        this.service = new ServiceBuilder(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .defaultScope(SCOPE)
                .callback(CALLBACK_URL)
                .build(GoogleApi20.instance());
    }


    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl();
    }


    public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException {
        return service.getAccessToken(code);
    }


    public String getUserProfile(OAuth2AccessToken accessToken) throws IOException, ExecutionException, InterruptedException {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v2/userinfo");
        service.signRequest(accessToken, request);
        Response response = service.execute(request);
        return response.getBody();
    }
}
