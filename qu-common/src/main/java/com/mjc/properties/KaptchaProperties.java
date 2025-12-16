package com.mjc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kaptcha")
public class KaptchaProperties {
    private String border;
    private String borderColor;
    private String textProducerFontColor;
    private String imageWidth;
    private String imageHeight;
    private String textProducerFontSize;
    private String sessionKey;
    private String textProducerImpl;
    private String noiseImpl;
}
