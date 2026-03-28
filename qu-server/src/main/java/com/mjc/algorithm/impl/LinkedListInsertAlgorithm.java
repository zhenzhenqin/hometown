package com.mjc.algorithm.impl;

import com.mjc.algorithm.AlgorithmScene;
import com.mjc.algorithm.AlgorithmService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LinkedListInsertAlgorithm implements AlgorithmService {
    
    @Data
    @AllArgsConstructor
    public static class ListNode {
        private int val;
        private ListNode next;
        
        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
    
    private List<AlgorithmScene> scenes = new ArrayList<>();
    
    @Override
    public String getName() {
        return "linkedListInsert";
    }
    
    @Override
    public String getDescription() {
        return "链表插入算法演示如何在单链表中指定位置插入新节点，包含头插法、尾插法和指定位置插入。";
    }
    
    @Override
    public String getCodeSnippet() {
        return """
            struct ListNode {                              // 第1行
                int val;                                   // 第2行
                struct ListNode* next;                     // 第3行
            };
            
            void insertNode(struct ListNode** head, int val, int pos) {  // 第4行
                struct ListNode* newNode = (struct ListNode*)malloc(sizeof(struct ListNode));  // 第5行
                newNode->val = val;                        // 第6行
                newNode->next = NULL;                      // 第7行
                
                if (pos == 0) {                            // 第8行
                    newNode->next = *head;                 // 第9行
                    *head = newNode;                       // 第10行
                    return;                                // 第11行
                }
                
                struct ListNode* p = *head;                // 第12行
                for (int i = 0; i < pos - 1; i++) {       // 第13行
                    p = p->next;                           // 第14行
                }
                
                newNode->next = p->next;                   // 第15行
                p->next = newNode;                         // 第16行
            }
            """;
    }
    
    @Override
    public List<AlgorithmScene> execute(Object... params) {
        scenes.clear();
        
        int insertVal = 99;
        int insertPos = 2;
        
        if (params.length > 0) insertVal = ((Number) params[0]).intValue();
        if (params.length > 1) insertPos = ((Number) params[1]).intValue();
        
        ListNode head = createList(new int[]{1, 3, 5, 7, 9});
        
        addScene(head, "初始链表状态: 1 -> 3 -> 5 -> 7 -> 9 -> NULL", 0, AlgorithmScene.AlgorithmStatus.IDLE);
        
        addScene(head, String.format("准备在位置 %d 插入节点，节点值为 %d", insertPos, insertVal), 4, AlgorithmScene.AlgorithmStatus.RUNNING);
        
        if (insertPos == 0) {
            addScene(head, "插入位置为0，执行头插法", 8, AlgorithmScene.AlgorithmStatus.RUNNING);
            
            ListNode newNode = new ListNode(insertVal, head);
            head = newNode;
            
            addScene(head, String.format("头插完成: %d -> 1 -> 3 -> 5 -> 7 -> 9 -> NULL", insertVal), 11, AlgorithmScene.AlgorithmStatus.SORTED);
        } else {
            addScene(head, String.format("创建新节点 val=%d", insertVal), 5, AlgorithmScene.AlgorithmStatus.RUNNING);
            
            addScene(head, String.format("遍历到位置 %d，找到前驱节点", insertPos - 1), 12, AlgorithmScene.AlgorithmStatus.RUNNING);
            
            ListNode p = head;
            for (int i = 0; i < insertPos - 1 && p != null; i++) {
                addSceneWithPointer(p, null, String.format("当前节点: %d，指向下一个节点...", p.getVal()), 13, AlgorithmScene.AlgorithmStatus.COMPARING);
                p = p.getNext();
            }
            
            if (p == null) {
                addScene(head, "插入位置超出链表长度！", 0, AlgorithmScene.AlgorithmStatus.MATCH_FAILED);
            } else {
                addSceneWithPointer(p, null, String.format("找到插入位置: p = %d，将新节点插入其后", p.getVal()), 14, AlgorithmScene.AlgorithmStatus.INSERTING);
                
                ListNode newNode = new ListNode(insertVal, p.getNext());
                p.setNext(newNode);
                
                addScene(head, String.format("插入完成: %s -> %d -> NULL", formatList(head, insertVal), insertVal), 16, AlgorithmScene.AlgorithmStatus.SORTED);
            }
        }
        
        return scenes;
    }
    
    private ListNode createList(int[] values) {
        if (values == null || values.length == 0) return null;
        ListNode head = new ListNode(values[0], null);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.setNext(new ListNode(values[i], null));
            current = current.getNext();
        }
        return head;
    }
    
    private String formatList(ListNode head, int newVal) {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        while (current != null) {
            if (current.getVal() == newVal && current.getNext() != null && current.getNext().getVal() == newVal) {
                break;
            }
            sb.append(current.getVal());
            if (current.getNext() != null) sb.append(" -> ");
            current = current.getNext();
        }
        return sb.toString();
    }
    
    private void addScene(ListNode head, String explanation, int codeLine, AlgorithmScene.AlgorithmStatus status) {
        addSceneWithPointer(head, null, explanation, codeLine, status);
    }
    
    private void addSceneWithPointer(ListNode head, Map<String, Object> pointers, String explanation, int codeLine, AlgorithmScene.AlgorithmStatus status) {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        Map<String, Object> ptrs = pointers != null ? pointers : new HashMap<>();
        
        ListNode current = head;
        int index = 0;
        while (current != null) {
            Map<String, Object> node = new HashMap<>();
            node.put("val", current.getVal());
            node.put("next", current.getNext() != null ? current.getNext().getVal() : "null");
            node.put("index", index);
            nodeList.add(node);
            current = current.getNext();
            index++;
        }
        
        scenes.add(AlgorithmScene.builder()
                .data(nodeList)
                .pointers(ptrs)
                .explanation(explanation)
                .activeCodeLine(codeLine)
                .status(status)
                .build());
    }
}
