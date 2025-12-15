package com.mjc.common;

import com.google.code.kaptcha.text.TextProducer;
import java.util.Random;

public class KaptchaTextCreator implements TextProducer {

    @Override
    public String getText() {
        int result = 0;
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder suChinese = new StringBuilder();
        int randomoperands = (int) (Math.random() * 3);
        
        if (randomoperands == 0) {
            result = x * y;
            suChinese.append(x).append("*").append(y).append("=?@").append(result);
        } else if (randomoperands == 1) {
            if (!(x == 0) && y % x == 0) {
                result = y / x;
                suChinese.append(y).append("/").append(x).append("=?@").append(result);
            } else {
                result = x + y;
                suChinese.append(x).append("+").append(y).append("=?@").append(result);
            }
        } else {
            if (x >= y) {
                result = x - y;
                suChinese.append(x).append("-").append(y).append("=?@").append(result);
            } else {
                result = y - x;
                suChinese.append(y).append("-").append(x).append("=?@").append(result);
            }
        }
        return suChinese.toString();
    }
}