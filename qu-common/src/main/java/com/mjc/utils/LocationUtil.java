package com.mjc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * IP属地获取工具类 (基于 Ip2Region)
 */
@Slf4j
public class LocationUtil {

    private static Searcher searcher;

    static {
        try {
            // 1. 从 classpath 读取 ip2region.xdb 文件
            ClassPathResource resource = new ClassPathResource("ip2region.xdb");
            InputStream inputStream = resource.getInputStream();
            
            // 2. 将流转为 byte 数组 (加载到内存，提高查询速度)
            byte[] cBuff = IOUtils.toByteArray(inputStream);
            
            // 3. 创建 searcher 对象
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("Ip2Region 离线库加载成功");
        } catch (Exception e) {
            log.error("Ip2Region 离线库加载失败: {}", e.getMessage());
        }
    }

    /**
     * 根据IP获取属地
     * @param ip ip地址
     * @return String 例如：浙江省 杭州市
     */
    public static String getCityInfo(String ip) {
        if (searcher == null || ip == null || ip.isEmpty()) {
            return "未知";
        }
        
        // 内网IP特殊处理
        if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
            return "内网IP";
        }

        try {
            // 4. 查询并获取结果
            // 返回格式固定为：国家|区域|省份|城市|ISP
            // 例如：中国|0|浙江省|杭州市|电信
            String region = searcher.search(ip);
            
            if (region == null) {
                return "未知";
            }

            // 5. 解析字符串
            String[] parts = region.split("\\|");
            // parts[2] 是省份，parts[3] 是城市
            String province = parts[2];
            String city = parts[3];

            // 处理 "0" 的情况 (有时候查不到省份会显示0)
            if ("0".equals(province)) province = "";
            if ("0".equals(city)) city = "";

            String shortProvince = simplifyArea(province);
            String shortCity = simplifyArea(city);

            // 2. 特殊处理直辖市 (上海市|上海市 -> 上海)
            if (shortProvince.equals(shortCity)) {
                return shortProvince;
            }

            // 3. 拼接结果 (如果有城市名则拼接，没有则只返回省份)
            if (shortCity.isEmpty()) {
                return shortProvince;
            }

            return shortProvince + "·" + shortCity;

        } catch (Exception e) {
            log.error("IP解析异常: {} -> {}", ip, e.getMessage());
            return "未知";
        }
    }

    /**
     * 辅助方法：简化地名 (去除行政单位后缀)
     */
    private static String simplifyArea(String name) {
        if (name == null) return "";
        return name
                .replace("省", "")
                .replace("市", "")
                .replace("自治区", "")
                .replace("特别行政区", "")
                .replace("壮族", "")
                .replace("回族", "")
                .replace("维吾尔", "");
    }
}