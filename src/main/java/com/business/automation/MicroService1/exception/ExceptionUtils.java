package com.business.automation.MicroService1.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
public class ExceptionUtils {

    public static void logTraceResponse(WebClientResponseException exception) {
        log.info("Response status: {}", exception.getRawStatusCode());
        log.info("Response headers: {}", exception.getHeaders());
        log.info("Response body: {}", exception.getResponseBodyAsString());
    }

    public static JsonNode createMessageForException(ObjectMapper objectMapper, String response) {
        try {
            return objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Function<WebClientResponseException, WebClientResponseException> getLogExceptionFunction() {
        return e -> {
            ExceptionUtils.logTraceResponse(e);
            return e;
        };
    }

    public static Function<WebClientResponseException, Mono<Throwable>> get4xxClientErrorFunction(ObjectMapper objectMapper) {

        log.info("objectMapper {} ",objectMapper);
        return ex ->
                Mono.error(new ServiceExceptionHolder.CustomJsonException
                        (
                                ExceptionUtils.createMessageForException(objectMapper
                                        , ex.getResponseBodyAsString())
                                , ex.getRawStatusCode()
                        ));
    }

    public static Function<WebClientResponseException, Mono<Throwable>> get5xxServerErrorFunction() {
        return ex -> Mono.error(new ServiceExceptionHolder.ServerNotFoundException
                (
                        "Server is not available"
                        , ex.getRawStatusCode()
                ));
    }

    public static Mono<? extends Throwable> handleClientError(ClientResponse response) {
        return response.createException()
                .flatMap(exception -> {
                            String responseBodyAsString = exception.getResponseBodyAsString();
                            String stringFromByte = new String(exception.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
                            log.info("stringFromByte {}",stringFromByte);
                            return Mono.error(new Remote4XXException(response.statusCode(),responseBodyAsString));
                        }

                );
    }

    public static Mono<? extends Throwable> handleServerError(ClientResponse response) {
        return response.createException()
                .flatMap(exception -> Mono.error(new Remote5XXException(exception.getMessage())));
    }

    public static Throwable handleWebClientException(WebClientResponseException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        String message = e.getMessage();
        return new RemoteResponseMappingException(statusCode, message);
    }


    public static String getStacktraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

}
