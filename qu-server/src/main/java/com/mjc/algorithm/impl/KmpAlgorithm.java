package com.mjc.algorithm.impl;

import com.mjc.algorithm.AlgorithmScene;
import com.mjc.algorithm.BaseArrayAlgorithm;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KmpAlgorithm extends BaseArrayAlgorithm {
    
    private int[] lps;
    
    @Override
    public String getName() {
        return "kmp";
    }
    
    @Override
    public String getDescription() {
        return "KMP算法是一种高效的字符串匹配算法，通过预处理模式串构建最长公共前缀后缀数组，避免文本串的回溯。";
    }
    
    @Override
    public String getCodeSnippet() {
        return """
            void computeLPS(int lps[], char pat[]) {      // 第1行
                int len = 0;                               // 第2行
                lps[0] = 0;                                // 第3行
                int i = 1;                                 // 第4行
                while (i < pat.length) {                   // 第5行
                    if (pat[i] == pat[len]) {              // 第6行
                        len++;                             // 第7行
                        lps[i] = len;                      // 第8行
                        i++;                               // 第9行
                    } else {
                        if (len != 0) {                    // 第10行
                            len = lps[len - 1];            // 第11行
                        } else {                           // 第12行
                            lps[i] = 0;                    // 第13行
                            i++;                           // 第14行
                        }
                    }
                }
            }
            
            int kmpSearch(char txt[], char pat[]) {         // 第15行
                int n = txt.length;                         // 第16行
                int m = pat.length;                         // 第17行
                int lps[] = new int[m];                     // 第18行
                computeLPS(lps, pat);                       // 第19行
                int i = 0, j = 0;                           // 第20行
                while (i < n) {                             // 第21行
                    if (pat[j] == txt[i]) {                 // 第22行
                        i++; j++;                           // 第23行
                    }
                    if (j == m) {                           // 第24行
                        return i - j;                       // 第25行
                    } else if (i < n && pat[j] != txt[i]) { // 第26行
                        if (j != 0) j = lps[j - 1];         // 第27行
                        else i++;                           // 第28行
                    }
                }
                return -1;                                  // 第29行
            }
            """;
    }
    
    @Override
    protected void initPointers() {
        pointers.clear();
        pointers.put("i", -1);
        pointers.put("j", -1);
    }
    
    @Override
    protected List<AlgorithmScene> run() {
        if (array.length < 2) {
            addScene(array, "数组长度不足，无法进行KMP演示", 0, AlgorithmScene.AlgorithmStatus.IDLE);
            return scenes;
        }
        
        int n = array.length;
        int m = n / 2;
        
        if (m == 0) m = 1;
        
        int[] text = new int[n];
        int[] pattern = new int[m];
        
        for (int i = 0; i < n; i++) {
            text[i] = array[i];
        }
        for (int i = 0; i < m; i++) {
            pattern[i] = array[i];
        }
        
        addScene(text, createPointersMap(-1, -1), 
                String.format("文本串 T = [%s]，模式串 P = [%s]", formatArray(text), formatArray(pattern)), 15, AlgorithmScene.AlgorithmStatus.IDLE);
        
        computeLps(pattern);
        
        addScene(text, createPointersMap(0, 0), "LPS数组构建完成，开始KMP匹配", 19, AlgorithmScene.AlgorithmStatus.IDLE);
        
        int i = 0, j = 0;
        boolean found = false;
        
        while (i < n) {
            addScene(text, createPointersMap(i, j), 
                    String.format("比较 T[%d]=%d 和 P[%d]=%d", i, text[i], j, pattern[j]), 22, AlgorithmScene.AlgorithmStatus.COMPARING);
            
            if (text[i] == pattern[j]) {
                i++;
                j++;
                
                if (j == m) {
                    addScene(text, createPointersMap(i, j), 
                            String.format("模式串完全匹配！起始位置 = %d", i - j), 25, AlgorithmScene.AlgorithmStatus.MATCH_SUCCESS);
                    found = true;
                    break;
                }
            } else {
                if (j != 0) {
                    addScene(text, createPointersMap(i, j), 
                            String.format("匹配失败，利用LPS数组回退 j 从 %d 到 %d", j, lps[j - 1]), 27, AlgorithmScene.AlgorithmStatus.MATCH_FAILED);
                    j = lps[j - 1];
                } else {
                    addScene(text, createPointersMap(i, j), 
                            String.format("匹配失败且 j=0，i 右移一位"), 28, AlgorithmScene.AlgorithmStatus.MATCH_FAILED);
                    i++;
                }
            }
        }
        
        if (!found) {
            addScene(text, createPointersMap(-1, -1), "模式串在文本串中未找到匹配", 29, AlgorithmScene.AlgorithmStatus.IDLE);
        }
        
        return scenes;
    }
    
    private void computeLps(int[] pattern) {
        int m = pattern.length;
        lps = new int[m];
        
        Map<String, Object> pointers = new HashMap<>();
        pointers.put("i", 1);
        pointers.put("j", 0);
        addScene(pattern, pointers, String.format("开始构建LPS数组，P = [%s]", formatArray(pattern)), 1, AlgorithmScene.AlgorithmStatus.RUNNING);
        
        int len = 0;
        lps[0] = 0;
        
        int i = 1;
        while (i < m) {
            pointers.put("i", i);
            pointers.put("j", len);
            addScene(pattern, pointers, String.format("计算 lps[%d]，当前匹配长度 len = %d", i, len), 5, AlgorithmScene.AlgorithmStatus.COMPARING);
            
            if (pattern[i] == pattern[len]) {
                len++;
                lps[i] = len;
                addScene(pattern, pointers, String.format("pattern[%d] == pattern[%d]，len++ = %d，lps[%d] = %d", i, len - 1, len, i, len), 8, AlgorithmScene.AlgorithmStatus.RUNNING);
                i++;
            } else {
                if (len != 0) {
                    addScene(pattern, pointers, String.format("不匹配，len != 0，回退 len = lps[%d] = %d", len - 1, lps[len - 1]), 11, AlgorithmScene.AlgorithmStatus.MATCH_FAILED);
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    addScene(pattern, pointers, String.format("不匹配且 len == 0，lps[%d] = 0", i), 13, AlgorithmScene.AlgorithmStatus.RUNNING);
                    i++;
                }
            }
        }
        
        addScene(lps, String.format("LPS数组构建完成: [%s]", formatArray(lps)), 19, AlgorithmScene.AlgorithmStatus.SORTED);
    }
    
    private Map<String, Object> createPointersMap(int i, int j) {
        Map<String, Object> map = new HashMap<>();
        map.put("i", i);
        map.put("j", j);
        return map;
    }
    
    private String formatArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
    
    @Override
    public List<AlgorithmScene> execute(Object... params) {
        scenes.clear();
        if (params.length > 0 && params[0] instanceof int[]) {
            this.array = ((int[]) params[0]).clone();
        } else if (params.length > 0 && params[0] instanceof List) {
            this.array = ((List<?>) params[0]).stream().mapToInt(x -> ((Number) x).intValue()).toArray();
        }
        initPointers();
        return run();
    }
}
