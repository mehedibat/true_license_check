package com.business.automation.MicroService1.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

//    private final String baseUrl;
//
//    public WebClientConfig(@Value("${base.url}") String baseUrl) {
//        this.baseUrl = baseUrl;
//    }

    @Bean(name = "jsonWebClient")
    public WebClient jsonWebClientInstance() {
        return getJsonWebClientBuilderInstance()
                .build();
    }

    @Bean(name = "xmlWebClient")
    public WebClient xmlWebClientInstance() {
        return getXmlWebClientBuilderInstance()
                .build();
    }

    private static WebClient.Builder getXmlWebClientBuilderInstance() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .clientConnector(new ReactorClientHttpConnector(getHttpClientInstance()))
                .filter(logRequest())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024));
    }

    private static WebClient.Builder getJsonWebClientBuilderInstance() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(getHttpClientInstance()))
                .filter(logRequest())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024));
    }

    private static HttpClient getHttpClientInstance() {
        int timeout = 118000; //118 seconds
        return HttpClient
                .create()
                .compress(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                });

    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
