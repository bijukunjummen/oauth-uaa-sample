package samples.authcode.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import samples.authcode.service.DownstreamServiceHandler;
import samples.authcode.service.TokenBeautifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SampleController {

    @Value("${ssoServiceUrl:placeholder}")
    private String ssoServiceUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.oauth2.client.clientId:client1}")
    private String clientId;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private DownstreamServiceHandler downstreamServiceHandler;

    @Autowired
    private TokenBeautifier tokenBeautifier;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("ssoServiceUrl", ssoServiceUrl);
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/secured/show_token")
    public String authCode(Model model, HttpServletRequest request) throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)auth.getDetails();
        String tokenValue = details.getTokenValue();

        if (tokenValue != null) {
            model.addAttribute("access_token", tokenBeautifier.formatJwtToken(tokenValue));
        }
        return "show_token";
    }

    @RequestMapping("/secured/read")
    public String callDownStreamWithReadScope(Model model) {
        String received = this.downstreamServiceHandler.callRead();
        model.addAttribute("received", received);
        return "showvalue";
    }

    @RequestMapping("/secured/write")
    public String callDownStreamWithWriteScope(Model model) {
        String received = this.downstreamServiceHandler.callWrite();
        model.addAttribute("received", received);
        return "showvalue";
    }

    @RequestMapping(value = "/userlogout", method = GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        URL url = new URL(request.getRequestURL().toString());
        String urlStr = url.getProtocol() + "://" + url.getAuthority();
        return "redirect:" + ssoServiceUrl + "/logout.do?redirect=" + urlStr + "&clientId=" + clientId;
    }
}
