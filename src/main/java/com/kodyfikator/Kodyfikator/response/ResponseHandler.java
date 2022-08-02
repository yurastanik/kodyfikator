package com.kodyfikator.Kodyfikator.response;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> getResponse(HttpStatus httpStatus, String key, String value) {
        return new BaseResponse(key, value, httpStatus).response();
    }

    public static ResponseEntity<Object> getResponse(HttpStatus httpStatus, String key, List<Object> value) {
        return new BaseResponse(key, value, httpStatus).response();
    }

    public static ResponseEntity<Object> getResponse(String key, List<Object> value) {
        return new BaseResponse(key, value).response();
    }

    public static ResponseEntity<Object> getResponse(String key, String value) {
        return new BaseResponse(key, value).response();
    }
}
