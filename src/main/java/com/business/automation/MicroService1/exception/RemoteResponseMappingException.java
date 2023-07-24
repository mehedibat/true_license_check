package com.business.automation.MicroService1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;


@Getter
public class RemoteResponseMappingException extends RuntimeException {
    HttpStatusCode statusCode;
    String message;
    public RemoteResponseMappingException(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
