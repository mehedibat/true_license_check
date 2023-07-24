package com.business.automation.MicroService1.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ServiceExceptionHolder {

    public final static int EXCEPTION_ID_NOT_FOUND_IN_DB = 1001;
    public final static int EXCEPTION_USER_LOGIN_ID_NOT_FOUND_IN_DB = 1002;
    public final static int EXCEPTION_PASSWORD_MISMATCH = 1003;

    public final static int EXCEPTION_GROUP_NOT_FOUND_IN_DB = 1004;
    public final static int EXCEPTION_USER_CORPORATE_INFO_NOT_FOUND_IN_DB = 1005;
    public final static int EXCEPTION_USER_GROUP_INFO_NOT_FOUND_IN_DB = 1006;
    public final static int EXCEPTION_USER_CORPORATE_ENTITY_FOUND_IN_DB = 1007;
    public final static int EXCEPTION_USER_CORPORATE_ENTITY_PERMISSION_FOUND_IN_DB = 1008;
    public final static int EXCEPTION_PERMISSION_NOT_FOUND_IN_DB = 1009;

    @Getter
    @RequiredArgsConstructor
    public static class ServiceException extends RuntimeException {
        private final int code;
        private final String message;
        private final JsonNode jsonError;
        private final String xmlError;
    }

    public static class AuthorizationTokenNotFoundException extends ServiceException {
        public AuthorizationTokenNotFoundException() {
            super(403, "Authorization Token Unavailable", null, null);
        }
    }

    public static class BadRequestException extends ServiceException {
        public BadRequestException(String message) {
            super(HttpStatus.BAD_REQUEST.value(), message, null, null);
        }
    }

    public static class ResourceNotFoundException extends ServiceException {
        public ResourceNotFoundException(int code, String message, JsonNode jsonError, String xmlError) {
            super(code, message, jsonError, xmlError);
        }
    }

    public static class InvalidRequestExceptioin extends ServiceException {
        public InvalidRequestExceptioin(int code, String message) {
            super(code, message, null, null);
        }
    }

    public static class ResourceAlreadyExistException extends ServiceException {
        public ResourceAlreadyExistException(int code, String message) {
            super(code, message, null, null);
        }
    }

    public static class ServerNotFoundException extends ResourceNotFoundException {
        public ServerNotFoundException(final String msg, final int code) {
            super(code, msg, null, null);
        }
    }

    public static class IdNotFoundInDBException extends ResourceNotFoundException {
        public IdNotFoundInDBException(final String msg) {
            super(EXCEPTION_ID_NOT_FOUND_IN_DB, msg, null, null);
        }
    }

    public static class IdMismatchedException extends ServiceException {
        public IdMismatchedException() {
            super(HttpStatus.BAD_REQUEST.value(), "Id Mismatched", null, null);
        }
    }

    public static class WebServiceException extends ServiceException {
        public WebServiceException(String message) {
            super(HttpStatus.BAD_REQUEST.value(), message, null, null);
        }
    }


    public static class CustomException extends ResourceNotFoundException {
        public CustomException(int code, String msg) {
            super(code, msg, null, null);
        }
    }

    public static class CustomJsonException extends ResourceNotFoundException {
        public CustomJsonException(final JsonNode msg, final int code) {
            super(code, null, msg, null);
        }
    }

    public static class CustomXMLException extends ResourceNotFoundException {
        public CustomXMLException(final Integer statusCode, final String message, final String errorResponse) {
            super(statusCode, message, null, errorResponse);
        }
    }

    public static class FileNotFoundException extends ServiceException {
        public FileNotFoundException(int code, String message) {
            super(code, message, null, null);
        }
    }

    public static class InvalidCheckSerialNoException extends ServiceException {
        public InvalidCheckSerialNoException(int code, String message) {
            super(code, message, null, null);
        }
    }

    public static class DuplicateCreditAccountException extends ServiceException {
        public DuplicateCreditAccountException(int code, String message) {
            super(code, message, null, null);
        }
    }
}
