package samples.authcode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import samples.authcode.service.DownstreamServiceHandler;

@SpringBootApplication
public class Application {

    @Value("${resourceServerUrl}")
    private String resourceServerUrl;

    public static void main(String[] args) {
        if ("true".equals(System.getenv("SKIP_SSL_VALIDATION"))) {
            SSLValidationDisabler.disableSSLValidation();
        }
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails,
                                                 OAuth2ClientContext oAuth2ClientContext) {
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, oAuth2ClientContext);
    }

    @Bean
    public DownstreamServiceHandler downstreamServiceHandler(OAuth2RestTemplate oAuth2RestTemplate) {
        return new DownstreamServiceHandler(oAuth2RestTemplate, resourceServerUrl);
    }

}
