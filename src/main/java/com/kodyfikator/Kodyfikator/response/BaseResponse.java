package com.kodyfikator.Kodyfikator.response;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseResponse {

    private HttpStatus status;
    private String key;
    private String value;
    private List<Object> listValue;

    public BaseResponse(String key, String value) {
        this.key = key;
        this.value = value;
        this.status = HttpStatus.OK;
    }

    public BaseResponse(String key, String value, HttpStatus status) {
        this(key, value);
        this.status = status;
    }

    public BaseResponse(String key, List<Object> value, HttpStatus status) {
        this(key, value);
        this.status = status;
    }

    public BaseResponse(String key, List<Object> value) {
        this.key = key;
        this.listValue = value;
        this.status = HttpStatus.OK;
    }

    public ResponseEntity<Object> response() {
        return (this.value == null) ? new ResponseEntity<>(new JSONObject().put(this.key, this.listValue).toMap(), this.status) : new ResponseEntity<>(new JSONObject().put(this.key, this.value).toMap(), this.status);
    }
}
