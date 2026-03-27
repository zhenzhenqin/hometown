package com.mjc.controller;

import com.mjc.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AI流式问答控制器
 */
@Slf4j
@RestController
@Tag(name = "AI智能问答")
@RequestMapping("/ai")
public class AiStreamController {

    @Autowired
    private AiChatService aiChatService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 流式输出AI回答（同时保存到数据库）
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式AI问答")
    public SseEmitter chatStream(@RequestParam String question,
                                @RequestParam(required = false) String sessionId) {
        SseEmitter emitter = new SseEmitter(60000L);

        executor.execute(() -> {
            try {
                // 调用AI服务获取回答
                String answer = aiChatService.chatStream(question, sessionId);

                // 批量发送（每次发送20个字符，减少请求次数）
                int batchSize = 20;
                StringBuilder buffer = new StringBuilder();
                
                for (int i = 0; i < answer.length(); i++) {
                    buffer.append(answer.charAt(i));
                    
                    // 每达到batchSize或最后一个字符时发送
                    if (buffer.length() >= batchSize || i == answer.length() - 1) {
                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(buffer.toString()));
                        buffer.setLength(0);
                        
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                emitter.send(SseEmitter.event().name("done").data(""));
                emitter.complete();

            } catch (IOException e) {
                log.error("流式响应失败: {}", e.getMessage());
                emitter.completeWithError(e);
            }
        });

        emitter.onCompletion(() -> log.info("流式响应完成"));
        emitter.onTimeout(() -> log.warn("流式响应超时"));
        emitter.onError(e -> log.error("流式响应错误: {}", e.getMessage()));

        return emitter;
    }

    /**
     * 获取会话历史
     */
    @GetMapping("/history/{sessionId}")
    @Operation(summary = "获取会话历史")
    public com.mjc.Result.Result<com.mjc.vo.AiChatVO> getHistory(@PathVariable String sessionId) {
        com.mjc.vo.AiChatVO result = aiChatService.getHistory(sessionId);
        return com.mjc.Result.Result.success(result);
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "删除会话")
    public com.mjc.Result.Result<Void> deleteSession(@PathVariable String sessionId) {
        aiChatService.deleteSession(sessionId);
        return com.mjc.Result.Result.success();
    }

    /**
     * 获取用户的所有会话
     */
    @GetMapping("/sessions")
    @Operation(summary = "获取会话列表")
    public com.mjc.Result.Result<com.mjc.vo.AiChatVO> getUserSessions(
            @RequestHeader(value = "User-ID", required = false) Integer userId) {
        com.mjc.vo.AiChatVO result = aiChatService.getUserSessions(userId);
        return com.mjc.Result.Result.success(result);
    }
}
