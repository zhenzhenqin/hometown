package com.mjc.algorithm;

import java.util.List;

public interface AlgorithmService {
    
    String getName();
    
    String getDescription();
    
    String getCodeSnippet();
    
    List<AlgorithmScene> execute(Object... params);
}
