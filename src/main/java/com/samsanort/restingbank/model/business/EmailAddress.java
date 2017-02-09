package com.samsanort.restingbank.model.business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO describe
 */
public class EmailAddress {

    private static final String PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(PATTERN);

    private final String value;

    /**
     *
     * @param address
     * @throws IllegalArgumentException
     */
    public EmailAddress(String address) {

        Matcher matcher = pattern.matcher(address);

        if( ! matcher.matches()) {
            throw new IllegalArgumentException( "Invalid email format: " + address);
        }

        this.value = address;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return this.value;
    }
}
