package com.mjc.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.mjc.properties.KaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Autowired
    private KaptchaProperties kaptchaProperties;

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", kaptchaProperties.getBorder());
        // 边框颜色
        properties.setProperty("kaptcha.border.color", kaptchaProperties.getBorderColor());
        // 验证码文本字符颜色
        properties.setProperty("kaptcha.textproducer.font.color", kaptchaProperties.getTextProducerFontColor());
        // 图片宽度
        properties.setProperty("kaptcha.image.width", kaptchaProperties.getImageWidth());
        // 图片高度
        properties.setProperty("kaptcha.image.height", kaptchaProperties.getImageHeight());
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", kaptchaProperties.getTextProducerFontSize());
        // session key
        properties.setProperty("kaptcha.session.key", kaptchaProperties.getSessionKey());

        //使用自定义的文本生成器类
        properties.setProperty("kaptcha.textproducer.impl", kaptchaProperties.getTextProducerImpl());

        // 干扰线配置
        properties.setProperty("kaptcha.noise.impl", kaptchaProperties.getNoiseImpl());

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
