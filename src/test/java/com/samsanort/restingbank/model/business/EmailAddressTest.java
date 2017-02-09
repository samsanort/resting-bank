package com.samsanort.restingbank.model.business;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by samu on 2/9/17.
 */
public class EmailAddressTest {

    EmailAddress testSubject;

    @Test
    public void construct_validAddress_buildsTheInstance() {

        // Given
        String address = "user@mail.com";

        // When
        testSubject = new EmailAddress(address);

        // Then
        assertThat(testSubject.getValue(), is( equalTo( address)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_invalidEmalFormat_throwsEmailAddressFormatException() {

        // Given
        String addressWithWrongFormat = "foo";

        // When
        testSubject = new EmailAddress(addressWithWrongFormat);
    }

}
