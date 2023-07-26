package com.business.automation.MicroService1.bean.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessagePublisherBean {

    private MetaDataBean metaData;
    private Object data;

    public MessagePublisherBean() {
    }

    public MessagePublisherBean(Object data) {
        this.data = data;
    }

    public MetaDataBean getMetaData() {
        return metaData;
    }

    public Object getData() {
        return data;
    }

    public static <T> T getConvertedData(String message, Class<T> toValueType, ObjectMapper mapper) throws JsonProcessingException {
        MessagePublisherBean publisherBean = mapper.readValue(message, MessagePublisherBean.class);
        return mapper.convertValue(publisherBean.getData(), toValueType);
    }

    public static <T> T getConvertedData(MessagePublisherBean message, Class<T> toValueType, ObjectMapper mapper) throws JsonProcessingException {
        return mapper.convertValue(message.getData(), toValueType);
    }

}
