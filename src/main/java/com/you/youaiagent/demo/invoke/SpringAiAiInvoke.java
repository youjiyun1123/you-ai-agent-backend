package com.you.youaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * spring AI 框架调用 AI 大模型
 */
//todo implements CommandLineRunner
@Component
public class SpringAiAiInvoke{
    @Resource
    private ChatModel dashscopeChatModel;


//    @Override
    public void run(String... args) throws Exception {
        String output = dashscopeChatModel.call(new Prompt("你好呀,我是小游")).getResult().getOutput().getText();
        System.out.println(output);
//        dashscopeChatModel.stream(new Prompt("你好呀,我是小游")).toStream().forEach(chatResponse -> System.out.println(chatResponse.getResult().getOutput().getText()));
    }
}
