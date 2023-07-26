package com.business.automation.MicroService1.bean.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaDataBean {
    private String message;
    private Integer secretCode;
}
