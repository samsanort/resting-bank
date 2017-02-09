package com.samsanort.restingbank.controller.impl;

import com.samsanort.restingbank.controller.UserController;
import com.samsanort.restingbank.dataservice.EmailAlreadyRegisteredException;
import com.samsanort.restingbank.dataservice.UserDataService;
import com.samsanort.restingbank.dataservice.UserNotFoundException;
import com.samsanort.restingbank.model.dto.BankAccountDto;
import com.samsanort.restingbank.model.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO Add description
 */
@RestController
@RequestMapping("/users")
public class UserRESTController implements UserController {

    private final Logger logger = LoggerFactory.getLogger(UserRESTController.class);

    @Autowired
    private UserDataService userDataService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody UserDto user) {

        userDataService.register(user.getEmail(), user.getPassword());
    }

    @PreAuthorize("@accessService.canManageUser(authentication,#userId)")
    @RequestMapping(value = "/{userId}/accounts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccountDto> getBankAccounts(
            @PathVariable Long userId) {

        return userDataService.getBankAccounts(userId);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email already registered")
    public void emailAlreadyRegistered() {}

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
    public void userNotFound() {}

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid request body")
    public void invalidParameter() {}

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Access denied to requested resource")
    public void accessDenied() {}

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unexpected internal error")
    public void internalError(HttpServletRequest req, Exception ex) {
        logger.error(req.getRequestURL().toString(), ex);
    }
}
