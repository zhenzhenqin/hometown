package com.mjc.algorithm.impl;

import com.mjc.algorithm.AlgorithmScene;
import com.mjc.algorithm.BaseArrayAlgorithm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BubbleSortAlgorithm extends BaseArrayAlgorithm {
    
    @Override
    public String getName() {
        return "bubbleSort";
    }
    
    @Override
    public String getDescription() {
        return "冒泡排序是一种简单的排序算法，通过重复遍历待排序序列，比较相邻元素并交换位置，直到整个序列有序。";
    }
    
    @Override
    public String getCodeSnippet() {
        return """
            void bubbleSort(int arr[], int n) {
                for (int i = 0; i < n - 1; i++) {           // 第1行
                    for (int j = 0; j < n - i - 1; j++) {   // 第2行
                        if (arr[j] > arr[j + 1]) {           // 第3行
                            int temp = arr[j];               // 第4行
                            arr[j] = arr[j + 1];            // 第5行
                            arr[j + 1] = temp;               // 第6行
                        }
                    }
                }
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
        int n = array.length;
        
        addScene(array, "初始数组状态，准备开始冒泡排序", 0, AlgorithmScene.AlgorithmStatus.IDLE);
        
        for (int i = 0; i < n - 1; i++) {
            pointers.put("i", i);
            addScene(array, String.format("第 %d 轮外层循环：i = %d，将确定第 %d 个最大元素的位置", i + 1, i, n - i), 1, AlgorithmScene.AlgorithmStatus.RUNNING);
            
            for (int j = 0; j < n - i - 1; j++) {
                pointers.put("j", j);
                addScene(array, String.format("比较 arr[j=%d]=%d 和 arr[j+1=%d]=%d", j, array[j], j + 1, array[j + 1]), 2, AlgorithmScene.AlgorithmStatus.COMPARING);
                
                if (array[j] > array[j + 1]) {
                    addScene(array, String.format("arr[%d]=%d > arr[%d]=%d，需要交换位置", j, array[j], j + 1, array[j + 1]), 3, AlgorithmScene.AlgorithmStatus.SWAPPING);
                    
                    swap(j, j + 1);
                    addScene(array, String.format("交换完成：arr[%d]=%d, arr[%d]=%d", j, array[j], j + 1, array[j + 1]), 6, AlgorithmScene.AlgorithmStatus.SWAPPING);
                } else {
                    addScene(array, String.format("arr[%d]=%d <= arr[%d]=%d，无需交换", j, array[j], j + 1, array[j + 1]), 2, AlgorithmScene.AlgorithmStatus.IDLE);
                }
            }
        }
        
        pointers.put("i", -1);
        pointers.put("j", -1);
        addScene(array, "排序完成！数组已按升序排列", 0, AlgorithmScene.AlgorithmStatus.SORTED);
        
        return scenes;
    }
}
