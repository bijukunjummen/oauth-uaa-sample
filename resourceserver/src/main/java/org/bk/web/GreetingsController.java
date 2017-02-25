package org.bk.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {
    @PreAuthorize("#oauth2.hasScope('resource.read')")
    @RequestMapping(method = RequestMethod.GET, value = "/secured/read")
    @ResponseBody
    public String read(Authentication authentication) {
        return String.format("Read Called: Hello %s", authentication.getCredentials());
    }

    @PreAuthorize("#oauth2.hasScope('resource.write')")
    @RequestMapping(method = RequestMethod.GET, value = "/secured/write")
    @ResponseBody
    public String write(Authentication authentication) {
        return String.format("Write Called: Hello %s", authentication.getCredentials());
    }


    @PreAuthorize("#oauth2.hasScope('resource.invalid')")
    @RequestMapping(method = RequestMethod.GET, value = "/secured/invalid")
    @ResponseBody
    public String invalidScope(Authentication authentication) {
        return String.format("Invalid Scope Called: Hello %s", authentication.getCredentials());
    }
}
