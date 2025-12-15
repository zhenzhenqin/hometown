package com.mjc.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.mjc.utils.RedisConstants.CAPTCHA_QUERY_KEY;

@RestController
public class CaptchaController {

    @Autowired
    @Qualifier("captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/captchaImage")
    public Map<String, Object> getCode() throws IOException {
        Map<String, Object> ajax = new HashMap<>();
        
        // 1. 生成UUID，作为Redis的key
        String uuid = UUID.randomUUID().toString();
        String verifyKey = CAPTCHA_QUERY_KEY + uuid;

        // 2. 生成验证码文本 (结果是 "8*9=?@72")
        String capText = captchaProducerMath.createText();
        
        // 3. 拆分：算式(用于画图) 和 答案(用于存Redis)
        String capStr = capText.substring(0, capText.lastIndexOf("@")); // 8*9=?
        String code = capText.substring(capText.lastIndexOf("@") + 1);  // 72

        // 4. 图片生成
        BufferedImage image = captchaProducerMath.createImage(capStr);
        
        // 5. 存入 Redis (设置2分钟过期)
        redisTemplate.opsForValue().set(verifyKey, code, 2, TimeUnit.MINUTES);
        
        // 6. 转换图片为 Base64
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.getEncoder().encodeToString(os.toByteArray()));
        
        return ajax;
    }
}