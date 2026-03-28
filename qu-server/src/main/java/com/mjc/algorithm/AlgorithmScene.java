package com.mjc.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmScene {
    
    private Object data;
    
    private Map<String, Object> pointers;
    
    private String explanation;
    
    private Integer activeCodeLine;
    
    private AlgorithmStatus status;
    
    public enum AlgorithmStatus {
        IDLE("空闲"),
        COMPARING("比较中"),
        SWAPPING("交换中"),
        MATCHING("匹配中"),
        MATCH_SUCCESS("匹配成功"),
        MATCH_FAILED("匹配失败"),
        INSERTING("插入中"),
        SORTED("已完成"),
        RUNNING("执行中");
        
        private final String description;
        
        AlgorithmStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
