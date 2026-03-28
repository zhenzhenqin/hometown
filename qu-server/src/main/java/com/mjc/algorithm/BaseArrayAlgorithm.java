package com.mjc.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseArrayAlgorithm implements AlgorithmService {
    
    protected List<AlgorithmScene> scenes = new ArrayList<>();
    
    protected int[] array;
    
    protected Map<String, Integer> pointers = new HashMap<>();
    
    protected void addScene(int[] data, String explanation, int activeCodeLine, AlgorithmScene.AlgorithmStatus status) {
        Map<String, Object> pointerSnapshot = new HashMap<>();
        pointers.forEach((k, v) -> pointerSnapshot.put(k, v));
        
        scenes.add(AlgorithmScene.builder()
                .data(data.clone())
                .pointers(pointerSnapshot)
                .explanation(explanation)
                .activeCodeLine(activeCodeLine)
                .status(status)
                .build());
    }
    
    protected void addScene(int[] data, Map<String, Object> customPointers, String explanation, int activeCodeLine, AlgorithmScene.AlgorithmStatus status) {
        scenes.add(AlgorithmScene.builder()
                .data(data.clone())
                .pointers(customPointers)
                .explanation(explanation)
                .activeCodeLine(activeCodeLine)
                .status(status)
                .build());
    }
    
    protected void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
    
    protected abstract void initPointers();
    
    protected abstract List<AlgorithmScene> run();
}
