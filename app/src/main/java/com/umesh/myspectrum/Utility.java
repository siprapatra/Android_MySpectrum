package com.sipra.myspectrum;

import java.util.regex.Pattern;

/**
 * Created by sipra on 24/8/19.
 */

public class Utility {

    public static boolean isValidPassword(String email, Pattern pattern) {
        return pattern.matcher(email).matches();
    }
}
