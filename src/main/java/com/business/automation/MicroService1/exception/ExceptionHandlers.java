package com.business.automation.MicroService1.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log
@AllArgsConstructor
public class ExceptionHandlers {

    private final ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(ExceptionHandlers.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ServiceExceptionHolder.AuthorizationTokenNotFoundException.class)
    public ResponseEntity<?> handleAuthorizationTokenNotFoundException(final ServiceExceptionHolder.AuthorizationTokenNotFoundException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.ServerNotFoundException.class)
    public ResponseEntity<?> handleServerNotFoundException(final ServiceExceptionHolder.ServerNotFoundException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ServiceExceptionHolder.ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(final ServiceExceptionHolder.ResourceNotFoundException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.WebServiceException.class)
    public ResponseEntity<?> handleWebServiceException(final ServiceExceptionHolder.WebServiceException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.CustomException.class)
    public ResponseEntity<?> handleCustomException(final ServiceExceptionHolder.CustomException ex) {
        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(final ServiceExceptionHolder.BadRequestException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.FileNotFoundException.class)
    public ResponseEntity<?> handleNullPointException(ServiceExceptionHolder.FileNotFoundException ex) {
        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceExceptionHolder.InvalidRequestExceptioin.class)
    public ResponseEntity<?> handleInvalidRequestException(ServiceExceptionHolder.InvalidRequestExceptioin ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }





    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleWebClientResponseException(WebClientResponseException ex, WebRequest request) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));

        String message = ex.getMessage();
        HttpStatusCode status = ex.getStatusCode();

        return new ResponseEntity<>(new ExceptionBean(status.value(), message), status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));

        String message = ex.getMessage();
        return new ResponseEntity<>(new ExceptionBean(400, message), HttpStatus.BAD_REQUEST);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> handleInvalidBankAccountException(ConnectException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));

        String message = ex.getMessage();
        return new ResponseEntity<>(new ExceptionBean(400, message), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(io.netty.handler.timeout.ReadTimeoutException.class)
    public ResponseEntity<?> handleReadTimeoutException(io.netty.handler.timeout.ReadTimeoutException ex) {

        logger.error(ExceptionUtils.getStacktraceAsString(ex));
        return new ResponseEntity<>(new ExceptionBean(400, "Request ReadTimeoutException"), HttpStatus.BAD_REQUEST);
    }

}
