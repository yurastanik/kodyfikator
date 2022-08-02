package com.kodyfikator.Kodyfikator.exception;

public class CodeNotFound extends Exception {

    public CodeNotFound(String code) {
        super("Nothing was found for code " + code);
    }

}
