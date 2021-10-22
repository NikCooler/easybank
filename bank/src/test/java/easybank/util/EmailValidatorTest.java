package org.easybank.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
@RunWith(Parameterized.class)
public class EmailValidatorTest {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return List.of(
                new Object[]{"nikolay.smirnov@gmail.com", true},
                new Object[]{"nikolay.smirnov", false},
                new Object[]{".nikolay.", false},
                new Object[]{null, false},
                new Object[]{"", false}
                );
    }

    private String  email;
    private boolean expected;

    public EmailValidatorTest(String email, boolean expected) {
        this.email    = email;
        this.expected = expected;
    }

    @Test
    public void testIsValid() {
        // given
        // when
        boolean valid = EmailValidator.isValid(email);
        // then
        Assert.assertEquals(expected, valid);
    }
}