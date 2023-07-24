package com.business.automation.MicroService1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class Remote4XXException extends RuntimeException{

    private String message;
    private HttpStatusCode statusCode;
    public Remote4XXException(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }



}
