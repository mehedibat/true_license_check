package com.business.automation.MicroService1.lintener;


import com.business.automation.MicroService1.bean.message.MessagePublisherBean;
import com.business.automation.MicroService1.bean.message.MetaDataBean;
import com.business.automation.MicroService1.config.RabbitConfig;
import com.business.automation.MicroService1.controller.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LicenseUploadedAcknowledgement {

    private final LicenseService licenseService;
    private final ObjectMapper mapper;

    @RabbitListener(queues = RabbitConfig.NEW_LICENSE_UPLOADED_STATUS_QUEUE_MICRO_1)
    public void licenseUploadedAcknowledgement(String message)  {
        try {
            MetaDataBean metaData = MessagePublisherBean.getConvertedData(message, MetaDataBean.class, mapper);
            log.info("metaData => : {} ",metaData);
            licenseService.downloadNewLicenseAndInstall();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }


    }
}
