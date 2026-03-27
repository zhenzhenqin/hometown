package com.mjc.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.mjc.dto.AiChatDTO;
import com.mjc.entity.AiConversation;
import com.mjc.entity.AiMessage;
import com.mjc.mapper.AiConversationMapper;
import com.mjc.mapper.AiMessageMapper;
import com.mjc.service.AiChatService;
import com.mjc.vo.AiChatVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI智能问答服务实现
 */
@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    @Autowired(required = false)
    private AiConversationMapper conversationMapper;

    @Autowired(required = false)
    private AiMessageMapper messageMapper;

    @Value("${ai.api-key:}")
    private String apiKey;

    @Value("${ai.model:deepseek-ai/DeepSeek-V3}")
    private String model;

    private static final String SYSTEM_PROMPT = """
你是"家乡百科"AI助手，专门帮助用户了解浙江衢州（南孔圣地·衢州有礼）的景点、特产和文化。
请根据知识库信息，用亲切友好的方式回答用户的问题。如果问题与衢州无关，请礼貌引导。

回答要求：
1. 语言亲切自然，像朋友聊天
2. 回答控制在200字以内
3. 可以适当推荐衢州的景点、美食等""";

    @Override
    @Transactional
    public AiChatVO chat(AiChatDTO dto, Integer userId) {
        String question = dto.getQuestion();
        String sessionId = dto.getSessionId();
        
        // 获取或创建会话
        AiConversation conversation;
        boolean isNewSession = false;
        
        if (sessionId != null && !sessionId.isEmpty() && conversationMapper != null) {
            conversation = conversationMapper.findBySessionId(sessionId);
        } else {
            conversation = null;
        }

        // 创建新会话
        if (conversation == null) {
            String newSessionId = UUID.randomUUID().toString().replace("-", "");
            String title = question.length() > 20 ? question.substring(0, 20) + "..." : question;
            
            conversation = AiConversation.builder()
                    .sessionId(newSessionId)
                    .userId(userId)
                    .title(title)
                    .messageCount(0)
                    .lastMessage("")
                    .build();
            
            if (conversationMapper != null) {
                conversationMapper.insert(conversation);
            }
            isNewSession = true;
        }

        // 调用AI获取回答
        String answer = callAiApi(question);

        // 保存消息到数据库
        if (messageMapper != null && conversationMapper != null && conversation.getId() != null) {
            try {
                // 保存用户消息
                AiMessage userMessage = AiMessage.builder()
                        .conversationId(conversation.getId())
                        .role("user")
                        .content(question)
                        .tokens(0)
                        .build();
                messageMapper.insert(userMessage);

                // 保存AI回复
                AiMessage aiMessage = AiMessage.builder()
                        .conversationId(conversation.getId())
                        .role("assistant")
                        .content(answer)
                        .tokens(answer.length() / 2)
                        .build();
                messageMapper.insert(aiMessage);

                // 更新会话
                conversation.setMessageCount(conversation.getMessageCount() + 2);
                conversation.setLastMessage(answer);
                conversationMapper.updateBySessionId(conversation);
            } catch (Exception e) {
                log.error("保存消息失败: {}", e.getMessage());
            }
        }

        // 获取历史消息
        List<AiChatVO.AiMessageVO> history = new ArrayList<>();
        if (messageMapper != null && conversation != null && conversation.getId() != null) {
            try {
                List<AiMessage> messages = messageMapper.findByConversationId(conversation.getId());
                history = messages.stream()
                        .map(m -> AiChatVO.AiMessageVO.builder()
                                .id(m.getId())
                                .role(m.getRole())
                                .content(m.getContent())
                                .createTime(m.getCreateTime())
                                .build())
                        .collect(Collectors.toList());
            } catch (Exception e) {
                log.error("获取历史消息失败: {}", e.getMessage());
            }
        }

        return AiChatVO.builder()
                .sessionId(conversation.getSessionId())
                .answer(answer)
                .title(isNewSession ? conversation.getTitle() : null)
                .tokens(answer.length() / 2)
                .history(history)
                .createTime(conversation.getCreateTime())
                .build();
    }

    /**
     * 流式获取AI回答（同时保存到数据库）
     */
    @Override
    public String chatStream(String question, String sessionId) {
        // 获取或创建会话
        AiConversation conversation = null;
        
        if (sessionId != null && !sessionId.isEmpty() && conversationMapper != null) {
            conversation = conversationMapper.findBySessionId(sessionId);
        }

        if (conversation == null) {
            String newSessionId = UUID.randomUUID().toString().replace("-", "");
            String title = question.length() > 20 ? question.substring(0, 20) + "..." : question;
            
            conversation = AiConversation.builder()
                    .sessionId(newSessionId)
                    .userId(null)
                    .title(title)
                    .messageCount(0)
                    .lastMessage("")
                    .build();
            
            if (conversationMapper != null) {
                conversationMapper.insert(conversation);
            }
        }

        // 调用AI获取回答
        String answer = callAiApi(question);

        // 保存到数据库
        if (messageMapper != null && conversationMapper != null && conversation.getId() != null) {
            try {
                // 保存用户消息
                AiMessage userMessage = AiMessage.builder()
                        .conversationId(conversation.getId())
                        .role("user")
                        .content(question)
                        .tokens(0)
                        .build();
                messageMapper.insert(userMessage);

                // 保存AI回复
                AiMessage aiMessage = AiMessage.builder()
                        .conversationId(conversation.getId())
                        .role("assistant")
                        .content(answer)
                        .tokens(answer.length() / 2)
                        .build();
                messageMapper.insert(aiMessage);

                // 更新会话
                conversation.setMessageCount(conversation.getMessageCount() + 2);
                conversation.setLastMessage(answer);
                conversationMapper.updateBySessionId(conversation);
                
                log.info("AI问答已保存到数据库 - 会话ID: {}", conversation.getSessionId());
            } catch (Exception e) {
                log.error("保存消息失败: {}", e.getMessage());
            }
        }

        return answer;
    }

    @Override
    public AiChatVO getHistory(String sessionId) {
        if (conversationMapper == null || messageMapper == null) {
            return null;
        }
        
        AiConversation conversation = conversationMapper.findBySessionId(sessionId);
        if (conversation == null) {
            return AiChatVO.builder().sessionId(sessionId).history(new ArrayList<>()).build();
        }

        List<AiMessage> messages = messageMapper.findByConversationId(conversation.getId());
        List<AiChatVO.AiMessageVO> history = messages.stream()
                .map(m -> AiChatVO.AiMessageVO.builder()
                        .id(m.getId())
                        .role(m.getRole())
                        .content(m.getContent())
                        .createTime(m.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return AiChatVO.builder()
                .sessionId(sessionId)
                .history(history)
                .createTime(conversation.getCreateTime())
                .build();
    }

    @Override
    @Transactional
    public void deleteSession(String sessionId) {
        if (conversationMapper != null) {
            AiConversation conversation = conversationMapper.findBySessionId(sessionId);
            if (conversation != null) {
                conversationMapper.deleteById(conversation.getId());
            }
        }
    }

    @Override
    public AiChatVO getUserSessions(Integer userId) {
        if (conversationMapper == null) {
            return AiChatVO.builder().history(new ArrayList<>()).build();
        }
        
        List<AiConversation> conversations = conversationMapper.findByUserId(userId);
        return AiChatVO.builder()
                .history(conversations.stream()
                        .map(c -> AiChatVO.AiMessageVO.builder()
                                .id(c.getId())
                                .role("session")
                                .content(c.getTitle())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 调用SiliconFlow API
     */
    private String callAiApi(String question) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
            return getDefaultAnswer(question);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            
            List<Map<String, String>> messages = new ArrayList<>();
            
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            messages.add(systemMsg);

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", question);
            messages.add(userMsg);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            org.springframework.http.HttpEntity<Map<String, Object>> entity = 
                new org.springframework.http.HttpEntity<>(requestBody, headers);

            String apiUrl = "https://api.siliconflow.cn/v1/chat/completions";
            org.springframework.http.ResponseEntity<String> response = 
                restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.POST, entity, String.class);

            Map<String, Object> responseMap = JSON.parseObject(response.getBody());
            
            if (responseMap != null && responseMap.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    return message.get("content").toString();
                }
            }

            return "抱歉，我现在无法回答您的问题，请稍后再试。";

        } catch (Exception e) {
            log.error("调用AI API失败: {}", e.getMessage());
            return getDefaultAnswer(question);
        }
    }

    /**
     * 获取预设回答
     */
    private String getDefaultAnswer(String question) {
        String lowerQuestion = question.toLowerCase();
        
        if (lowerQuestion.contains("景点") || lowerQuestion.contains("好玩") || lowerQuestion.contains("推荐")) {
            return "[景点推荐] 衢州热门景点推荐\n\n江郎山 - 世界自然遗产，丹霞地貌，国家5A级景区，建议游玩半天\n\n天脊龙门 - 4A级景区，峡谷风光，飞瀑绝壁，避暑圣地\n\n孔氏南宗家庙 - 儒家圣地，文化之旅，免费参观";
        }
        
        if (lowerQuestion.contains("美食") || lowerQuestion.contains("好吃") || lowerQuestion.contains("特产") || lowerQuestion.contains("小吃")) {
            return "[美食推荐] 衢州特色美食\n\n衢州烤饼 - 千年传承的传统小吃，酥脆可口\n\n龙游发糕 - 软糯香甜，起源于明代，馈赠佳品\n\n不老神鸡 - 药膳美食，滋补养生\n\n鸭头/兔头 - 衢州人最爱的夜宵，辣得过瘾";
        }
        
        if (lowerQuestion.contains("文化") || lowerQuestion.contains("历史") || lowerQuestion.contains("孔")) {
            return "[文化介绍] 衢州历史文化\n\n南孔文化 - 衢州是南孔圣地，孔子后裔南迁后的居住地\n\n孔氏南宗家庙 - 全国仅有的两座孔庙之一，南孔圣地-衢州有礼\n\n婺剧 - 浙江传统戏曲，国家级非遗";
        }
        
        if (lowerQuestion.contains("攻略") || lowerQuestion.contains("怎么玩") || lowerQuestion.contains("几天")) {
            return "[旅游攻略] 衢州2-3日游攻略\n\nDay 1 - 江郎山登山赏景\nDay 2 - 天脊龙门峡谷探险 + 市区孔庙 + 晚上品尝美食\nDay 3 - 水亭门历史文化街区 + 烂柯山(围棋圣地)\n\n住宿建议：住市区，交通便利";
        }
        
        return "你好！我是衢州AI导游\n\n我可以帮你解答：\n- 衢州有哪些好玩景点\n- 有什么特色美食\n- 当地文化历史\n- 旅游攻略推荐\n\n请告诉我你想了解什么？";
    }
}
