package com.mjc.controller;

import com.mjc.algorithm.AlgorithmFactory;
import com.mjc.algorithm.AlgorithmScene;
import com.mjc.algorithm.AlgorithmService;
import com.mjc.Result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    
    @Autowired
    private AlgorithmFactory algorithmFactory;
    
    @GetMapping("/list")
    public Result getAlgorithmList() {
        List<String> algorithms = algorithmFactory.getSupportedAlgorithms();
        return Result.success(algorithms);
    }
    
    @GetMapping("/info/{name}")
    public Result getAlgorithmInfo(@PathVariable String name) {
        try {
            Map<String, String> info = algorithmFactory.getAlgorithmInfo(name);
            return Result.success(info);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{name}")
    public Result executeAlgorithm(
            @PathVariable String name,
            @RequestParam(required = false) String data,
            @RequestParam(required = false, defaultValue = "5,3,8,1,9,2,7,4,6") String array,
            @RequestParam(required = false) Integer insertVal,
            @RequestParam(required = false) Integer insertPos) {
        
        try {
            AlgorithmService algorithm = algorithmFactory.getAlgorithm(name);
            
            List<AlgorithmScene> scenes;
            
            if ("bubbleSort".equals(name) || "kmp".equals(name)) {
                int[] arr = parseArray(array);
                scenes = algorithm.execute((Object) arr);
            } else if ("linkedListInsert".equals(name)) {
                Object[] params = new Object[2];
                params[0] = insertVal != null ? insertVal : 99;
                params[1] = insertPos != null ? insertPos : 2;
                scenes = algorithm.execute(params);
            } else {
                scenes = algorithm.execute();
            }
            
            return Result.success(scenes);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("算法执行失败: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/{name}/stream", produces = "text/event-stream")
    public String streamAlgorithm(
            @PathVariable String name,
            @RequestParam(required = false, defaultValue = "5,3,8,1,9,2,7,4,6") String array) {
        
        try {
            AlgorithmService algorithm = algorithmFactory.getAlgorithm(name);
            int[] arr = parseArray(array);
            List<AlgorithmScene> scenes = algorithm.execute((Object) arr);
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < scenes.size(); i++) {
                sb.append("data: {\"index\":").append(i)
                  .append(",\"scene\":{\"status\":\"").append(scenes.get(i).getStatus())
                  .append("\",\"explanation\":\"").append(scenes.get(i).getExplanation())
                  .append("\"}}\n\n");
            }
            sb.append("data: [DONE]\n\n");
            return sb.toString();
        } catch (Exception e) {
            return "data: {\"error\":\"" + e.getMessage() + "\"}\n\n";
        }
    }
    
    private int[] parseArray(String arrayStr) {
        if (arrayStr == null || arrayStr.isEmpty()) {
            return new int[]{5, 3, 8, 1, 9, 2, 7, 4, 6};
        }
        String[] parts = arrayStr.split(",");
        return Arrays.stream(parts)
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
