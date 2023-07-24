package com.business.automation.MicroService1.web;


import com.business.automation.MicroService1.exception.ExceptionUtils;
import com.business.automation.MicroService1.exception.ServiceExceptionHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class WebService {

    private final WebClient xmlWebClient;
    private final WebClient jsonWebClient;
    private final ObjectMapper objectMapper;

    private final OkHttpClient okHttpClient;

    public WebService(@Qualifier("xmlWebClient") WebClient xmlWebClient, @Qualifier("jsonWebClient") WebClient jsonWebClient
            , ObjectMapper objectMapper, OkHttpClient okHttpClient) {
        this.xmlWebClient = xmlWebClient;
        this.jsonWebClient = jsonWebClient;
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;
    }

    public JsonNode getJsonResponse(String uri) {
        JsonNode jsonNode = jsonWebClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ExceptionUtils::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, ExceptionUtils::handleServerError)
                .bodyToMono(JsonNode.class)
                .onErrorMap(WebClientResponseException.class, ExceptionUtils::handleWebClientException)
                .block();
        if (Objects.isNull(jsonNode)) return null;
        return jsonNode;
    }

    public Object postXmlResponse(Object object, String uri, Class<?> classRequest,Class<?> classResponse) {
        log.info("request-url======>>>>>> {}",uri);
        log.info("request-data======>>>>>> {}",object);
        
        return xmlWebClient.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .body(Mono.just(object), classRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError
                        , clientResponse -> clientResponse.createException()
                                .map(ExceptionUtils.getLogExceptionFunction())
                                .flatMap(ex -> Mono.error(new ServiceExceptionHolder.CustomXMLException(ex.getRawStatusCode()
                                        , ex.getLocalizedMessage()
                                        , ex.getResponseBodyAsString()))
                                ))
                .onStatus(HttpStatusCode::is5xxServerError
                        , clientResponse -> clientResponse.createException()
                                .map(ExceptionUtils.getLogExceptionFunction())
                                .flatMap(ex -> Mono.error(new ServiceExceptionHolder.CustomXMLException(ex.getRawStatusCode()
                                        , ex.getLocalizedMessage()
                                        , ex.getResponseBodyAsString()))))
                .bodyToMono(classResponse)
                .block();
    }

    public JsonNode postJsonResponse(String uri, Object object, Class<?> classRequest) {
        JsonNode jsonNode = jsonWebClient.post()
                .uri(uri)
                .body(Mono.just(object), classRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ExceptionUtils::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, ExceptionUtils::handleServerError)
                .bodyToMono(JsonNode.class)
                .onErrorMap(WebClientResponseException.class, ExceptionUtils::handleWebClientException)
                .block();
        if (Objects.isNull(jsonNode)) return null;
        return jsonNode;
    }

    public String postUrlEncodedFormData(String uri, String content) throws IOException {

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(uri)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        okhttp3.Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public JsonNode postJsonResponseForSSLWireless(String uri, Object object, Class<?> classRequest,String authToken,String stkCode) {
        JsonNode jsonNode = jsonWebClient.post()
                .uri(uri)
                .headers(header -> {
                    header.add("AUTH-KEY",authToken);
                    header.add("STK-CODE", stkCode);
                })
                .body(Mono.just(object), classRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ExceptionUtils::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, ExceptionUtils::handleServerError)
                .bodyToMono(JsonNode.class)
                .onErrorMap(WebClientResponseException.class, ExceptionUtils::handleWebClientException)
                .block();
        if (Objects.isNull(jsonNode)) return null;
        return jsonNode;
    }


}
