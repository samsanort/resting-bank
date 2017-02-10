package com.samsanort.restingbank.model.business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email object, with format validation.
 */
public class EmailAddress {

    private static final String PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(PATTERN);

    private final String value;

    /**
     * C'tor.
     * @param address The email address.
     * @throws IllegalArgumentException If the email format is invalid.
     */
    public EmailAddress(String address) {

        Matcher matcher = pattern.matcher(address);

        if( ! matcher.matches()) {
            throw new IllegalArgumentException( "Invalid email format: " + address);
        }

        this.value = address;
    }

    /**
     * Obtains the validated email value.
     * @return The format-checked email.
     */
    public String getValue() {
        return this.value;
    }
}
