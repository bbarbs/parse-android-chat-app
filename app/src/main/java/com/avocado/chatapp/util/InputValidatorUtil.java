package com.avocado.chatapp.util;

import com.wajahatkarim3.easyvalidation.core.Validator;

public final class InputValidatorUtil {

    private InputValidatorUtil() {
    }

    public static Validator inputValidator(String input) {
        return new Validator(input)
                .nonEmpty()
                .minLength(8)
                .maxLength(16);
    }
}
