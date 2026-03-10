package com.you.youaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {
    @Test
    void scrapWebPage(){
        WebScrapingTool webScrapingTool=new WebScrapingTool();
        String result = webScrapingTool.scrapWebPage("https://i.mooc.chaoxing.com/space/index?t=1670675129393");
        Assertions.assertNotNull(result);
    }
}