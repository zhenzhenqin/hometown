package com.mjc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Slf4j
public class LocationUtil {

    private static Searcher searcher;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("ip2region.xdb");
            InputStream inputStream = resource.getInputStream();
            byte[] cBuff = IOUtils.toByteArray(inputStream);
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("Ip2Region 离线库加载成功");
        } catch (Exception e) {
            log.error("Ip2Region 离线库加载失败: {}", e.getMessage());
        }
    }

    public static String getCityInfo(String ip) {
        if (searcher == null || ip == null || ip.isEmpty()) {
            return "未知";
        }
        if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
            return "内网IP";
        }

        try {
            String region = searcher.search(ip);
            // 格式：国家|区域|省份|城市|ISP
            if (region == null) return "未知";

            String[] parts = region.split("\\|");

            if (parts.length < 4) return "未知";

            String province = parts[1]; // 省份 (浙江省)
            String city = parts[2];     // 城市 (宁波市)

            // 处理 "0"
            if ("0".equals(province)) province = "";
            if ("0".equals(city)) city = "";

            // 简化名称
            String shortProvince = simplifyArea(province);
            String shortCity = simplifyArea(city);

            // 1. 如果省份为空，尝试返回城市
            if (shortProvince.isEmpty()) {
                return shortCity.isEmpty() ? "未知" : shortCity;
            }

            // 2. 如果城市为空，返回省份
            if (shortCity.isEmpty()) {
                return shortProvince;
            }

            // 3. 处理直辖市 (上海·上海 -> 上海)
            // 或者 省市同名 (吉林省·吉林市 -> 吉林)
            if (shortProvince.equals(shortCity)) {
                return shortProvince;
            }

            // 4. 标准返回：浙江·宁波
            return shortProvince + "·" + shortCity;

        } catch (Exception e) {
            log.error("IP解析异常: {} -> {}", ip, e.getMessage());
            return "未知";
        }
    }

    /**
     * 辅助方法：简化地名
     * 增加了对 "自治州"、"地区" 等后缀的处理，防止名字过长
     */
    private static String simplifyArea(String name) {
        if (name == null) return "";
        return name
                .replace("省", "")
                .replace("市", "")
                .replace("自治区", "")
                .replace("特别行政区", "")
                .replace("自治州", "")
                .replace("地区", "")
                .replace("盟", "")
                .replace("壮族", "")
                .replace("回族", "")
                .replace("维吾尔", "")
                .replace("土家族", "")
                .replace("苗族", "")
                .replace("藏族", "");
    }
}