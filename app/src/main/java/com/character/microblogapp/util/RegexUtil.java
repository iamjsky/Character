package com.character.microblogapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM_special
            = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,20}$"); // 영문, 수자, 특수문자 포함하여 8자리 ~ 20자리까지 가능
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM
            = Pattern.compile("^[a-zA-Z0-9]{6,20}$"); // 영문, 수자 포함하여 6자리 ~ 20자리까지 가능
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA
            = Pattern.compile("^(?=.*[a-zA-Z]).+$"); // 영문 포함
    public static final Pattern VALID_PASSWOLD_REGEX_NUMBER
            = Pattern.compile("^(?=.*[0-9]).+$"); // 수자 포함
    public static final Pattern VALID_PASSWOLD_REGEX_SPECIAL
            = Pattern.compile("^(?=.*[!@#$%^*+=-]).+$"); // 특수문자 포함
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUMBER
            = Pattern.compile("^[a-zA-Z0-9]+$"); // 영문, 숫자 포함
    public static final Pattern VALID_PASSWOLD_REGEX_NUMBER_SPECIAL
            = Pattern.compile("^[0-9!@.#$%^&*?_~]+$"); // 수자, 특수문자 포함
    public static final Pattern VALID_PASSWOLD_REGEX_SPECIAL_ALPHA
            = Pattern.compile("^[a-zA-Z!@.#$%^&*?_~]+$"); // 특수문자, 영문 포함
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM2
            = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$"); // 8자리 ~ 20자리까지 가능

    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //비밀번호정규식
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.find();
    }

    public static String userName(String userId) {
        String[] userNm = userId.split("@");
        String userName = userNm[0];
        return userName;
    }
}
