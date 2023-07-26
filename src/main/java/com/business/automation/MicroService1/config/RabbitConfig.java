package com.business.automation.MicroService1.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitConfig {

    public static final String NEW_LICENSE_UPLOADED_STATUS_QUEUE = "new.license.uploaded.status.q";
    public static final String NEW_LICENSE_UPLOADED_STATUS_EX = "new.license.uploaded.status.ex";
    public static final String NEW_LICENSE_UPLOADED_STATUS_KEY= "new.license.uploaded.status.key";
    public static final String NEW_LICENSE_UPLOADED_STATUS_QUEUE_MICRO_1 = "new.license.uploaded.status.q1";
    public static final String NEW_LICENSE_UPLOADED_STATUS_QUEUE_MICRO_2 = "new.license.uploaded.status.q2";


    /*---------------------------NEW LICENSE UPLOADED STATUS QUEUE---------------------------*/
//    @Bean
//    Queue newLicenseUploadedQueue() {
//        return new Queue(NEW_LICENSE_UPLOADED_STATUS_QUEUE, true);
//    }
//
//    @Bean
//    TopicExchange newLicenseUploadedExchange() {
//        return new TopicExchange(NEW_LICENSE_UPLOADED_STATUS_EX);
//    }
//
//    @Bean
//    Binding newLicenseUploadedBinding(Queue newLicenseUploadedQueue, TopicExchange newLicenseUploadedExchange) {
//        return BindingBuilder.bind(newLicenseUploadedQueue).to(newLicenseUploadedExchange).with(NEW_LICENSE_UPLOADED_STATUS_KEY);
//    }

    /*---------------------------NEW LICENSE UPLOADED STATUS QUEUE---------------------------*/

    @Bean
    public RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(10)
                .backOffOptions(5000, 1.5, 30000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }
}
