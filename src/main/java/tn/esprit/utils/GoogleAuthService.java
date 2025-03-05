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

import static com.github.scribejava.core.model.OAuthConstants.CLIENT_ID;
import static com.github.scribejava.core.model.OAuthConstants.CLIENT_SECRET;

public class GoogleAuthService {
    private String clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID");
    private String clientSecret = System.getenv("GOOGLE_OAUTH_CLIENT_SECRET");
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
