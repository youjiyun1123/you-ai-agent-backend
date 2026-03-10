package com.you.youaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = "你好,我是程序员游纪云";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        //第二轮
        message = "我想让另一半 (张三) 更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着?刚刚跟你说过,帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testDoChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好,我是程序员游纪云";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我们总是为一些鸡毛蒜皮的小事吵架（比如谁洗碗、袜子乱扔），该如何停止这种恶性循环？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithCouldRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我们总是为一些鸡毛蒜皮的小事吵架（比如谁洗碗、袜子乱扔），该如何停止这种恶性循环？";
        String answer = loveApp.doChatWithCouldRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
//        testMessage("周末想带女朋友去杭州约会，推荐几个适合情侣约会的小众打卡地点");
        testMessage("最近和对象吵架了,看看编程导航网站(https://www.codefather.cn/)的其他情侣是怎么解决矛盾的?");
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");
//        testMessage("执行 python3 脚本来生成数据分析报告");
//        testMessage("保存我的恋爱档案为文件");
//        testMessage("生成一份'七夕约会计划'PDF,包含餐厅预订,活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        //测试地图 MCP
//        String message="我的另一半住在上海浦东，请帮我找到5公里内合适的约会地点";
//        String answer = loveApp.doChatWithMcp(message, chatId);
//        Assertions.assertNotNull(answer);
        String message="帮我搜索一些哄另一半开心的图片";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}