package com.kodyfikator.Kodyfikator.exception;

public class BadRequest extends Exception {

    public BadRequest(String code) {
        super("The code " + code + " is incorrect");
    }
}
