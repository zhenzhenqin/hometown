package com.mjc.algorithm;

import com.mjc.algorithm.impl.BubbleSortAlgorithm;
import com.mjc.algorithm.impl.KmpAlgorithm;
import com.mjc.algorithm.impl.LinkedListInsertAlgorithm;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class AlgorithmFactory {
    
    private final Map<String, AlgorithmService> algorithms = new HashMap<>();
    
    public AlgorithmFactory(List<AlgorithmService> algorithmList) {
        for (AlgorithmService algorithm : algorithmList) {
            algorithms.put(algorithm.getName(), algorithm);
        }
    }
    
    public AlgorithmService getAlgorithm(String name) {
        AlgorithmService algorithm = algorithms.get(name);
        if (algorithm == null) {
            throw new IllegalArgumentException("不支持的算法: " + name);
        }
        return algorithm;
    }
    
    public List<String> getSupportedAlgorithms() {
        return algorithms.keySet().stream().toList();
    }
    
    public Map<String, String> getAlgorithmInfo(String name) {
        AlgorithmService algorithm = getAlgorithm(name);
        Map<String, String> info = new HashMap<>();
        info.put("name", algorithm.getName());
        info.put("description", algorithm.getDescription());
        info.put("codeSnippet", algorithm.getCodeSnippet());
        return info;
    }
}
