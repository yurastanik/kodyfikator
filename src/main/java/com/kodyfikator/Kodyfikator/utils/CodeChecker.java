package com.kodyfikator.Kodyfikator.utils;

public class CodeChecker {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean check(String code) {
        return (code.length() == 19 && code.contains("UA") && isNumeric(code.replace("UA", "")));
    }
}
