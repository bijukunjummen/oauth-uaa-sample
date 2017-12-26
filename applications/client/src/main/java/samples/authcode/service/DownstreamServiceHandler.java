package samples.authcode.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

public class DownstreamServiceHandler {

    private final OAuth2RestTemplate oAuth2RestTemplate;
    private final String resourceUrl;


    public DownstreamServiceHandler(OAuth2RestTemplate oAuth2RestTemplate, String resourceUrl) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.resourceUrl = resourceUrl;
    }


    public String callRead() {
        return callDownstream(String.format("%s/secured/read", resourceUrl));
    }

    public String callWrite() {
        return callDownstream(String.format("%s/secured/write", resourceUrl));
    }

    public String callInvalidScope() {
        return callDownstream(String.format("%s/secured/invalid", resourceUrl));
    }

    private String callDownstream(String uri) {
        try {
            ResponseEntity<String> responseEntity = this.oAuth2RestTemplate.getForEntity(uri, String.class);
            return responseEntity.getBody();
        } catch(HttpStatusCodeException statusCodeException) {
            return statusCodeException.getResponseBodyAsString();
        }
    }
}
