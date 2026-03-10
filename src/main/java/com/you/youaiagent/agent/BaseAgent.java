package com.you.youaiagent.agent;


import com.you.youaiagent.agent.model.AgentState;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行流程。
 * <p>
 * 提供状态转换，内存管理和基于步骤的执行循环的基础功能
 * 子类必须实现step方法
 */
@Data
@Slf4j
public abstract class BaseAgent {
    //核心属性
    private String name;
    //提示词
    private String systemPrompt;
    private String nextStepPrompt;

    //代理状态
    private AgentState state = AgentState.IDLE;
    //执行步骤控制
    private int currentStep = 0;
    private int maxSteps = 10;
    //LLM大模型
    private ChatClient    chatClient;

    // Memory记忆（需要自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     *
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt) {
        //基础校验
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state：" + this.state);
        }
        if (StringUtils.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty prompt：");
        }
        //执行
        this.state = AgentState.RUNNING;
        //记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        //保存结果列表
        List<String> results = new ArrayList<>();
        try {
            //执行循环
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step {}/{}", stepNumber, maxSteps);
                //单步执行
                String stepResult = step();
                String result = "Step" + stepNumber + ":" + stepResult;
                results.add(result);
            }
            //检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max step(" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent:" + e);
            return "执行错误:" + e.getMessage();
        } finally {
            this.cleanup();
        }
    }

    /**
     * 运行代理(流式输出)
     *
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public SseEmitter runSteam(String userPrompt) {
        //创建一个超时时间较长的SseEmitter
        SseEmitter sseEmitter = new SseEmitter(300000L);
        //使用线程异步处理,避免阻塞主线程
        CompletableFuture.runAsync(() -> {
            //基础校验
            try {
                if (this.state != AgentState.IDLE) {
                    sseEmitter.send("错误：无法从状态运行代理：" + this.state);
                    sseEmitter.complete();
                    return;
                }
                if (StringUtils.isBlank(userPrompt)) {
                    sseEmitter.send("错误：不能使用空提示词运行代理:");
                    sseEmitter.complete();
                    return;
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }

            //执行
            this.state = AgentState.RUNNING;
            //记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            //保存结果列表
            List<String> results = new ArrayList<>();
            try {
                //执行循环
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i + 1;
                    currentStep = stepNumber;
                    log.info("Executing step {}/{}", stepNumber, maxSteps);
                    //单步执行
                    String stepResult = step();
                    String result = "Step" + stepNumber + ":" + stepResult;
                    results.add(result);
                    //输出当前每一步的结果到SSE
                    sseEmitter.send(result);
                }
                //检查是否超出步骤限制
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    results.add("Terminated: Reached max step(" + maxSteps + ")");
                    sseEmitter.send("执行结束，到达最大步骤(" + maxSteps + ")");
                }
                //正常完成
                sseEmitter.complete();
            } catch (Exception e) {
                state = AgentState.ERROR;
                log.error("Error executing agent:" + e);
                try {
                    sseEmitter.send("执行错误:" + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
            } finally {
                this.cleanup();
            }
        });
        //设置超市回调
        sseEmitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE Connection timeout");
        });
        //设置完成回调
        sseEmitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.warn("SSE Connection completed");
        });
        return sseEmitter;
    }

    /**
     * 定义单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup() {
        //子类可以重写此方法来清理资源
    }
}
