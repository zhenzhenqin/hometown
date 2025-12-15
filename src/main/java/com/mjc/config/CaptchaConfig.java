package com.mjc.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 验证码文本字符颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "160");
        // 图片高度
        properties.setProperty("kaptcha.image.height", "60");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        // session key
        properties.setProperty("kaptcha.session.key", "kaptchaCodeMath");

        //使用自定义的文本生成器类
        properties.setProperty("kaptcha.textproducer.impl", "com.mjc.common.KaptchaTextCreator");

        // 干扰线配置
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}