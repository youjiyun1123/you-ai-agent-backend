package com.you.youaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YouManusTest {
    @Resource
    private YouManus youManus;

    @Test
    public void run() {
        String userPrompt = """
                我的另一半住杭州临平区崇贤街道,请帮我找到5公里内合适的约会地点，
                并结合一些网络图片，制定一份详细的约会计划
                并以PDF格式输出
                """;
        String answer = youManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}