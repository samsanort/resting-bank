package com.samsanort.restingbank.security.impl;

import com.samsanort.restingbank.security.AuthenticatedUser;
import com.samsanort.restingbank.security.AuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * TODO describe
 */

@RestController
public class AuthenticationRESTController implements AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationRESTController.class);

    @Override
    @RequestMapping(value = "/credentials/id", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Long getId(Principal user) {

        Authentication authentication = (Authentication) user;
        AuthenticatedUser authenticatedUser =  (AuthenticatedUser) authentication.getPrincipal();
        return authenticatedUser.getId();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unexpected internal error")
    public void internalError(HttpServletRequest req, Exception ex) {
        logger.error(req.getRequestURL().toString(), ex);
    }
}
