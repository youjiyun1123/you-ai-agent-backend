package com.you.youaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页抓取工具
 */
public class WebScrapingTool {
    @Tool(description = "scrape the content of a web page")
    public String scrapWebPage(@ToolParam(description = "URL of the web page to scrape")
                               String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (Exception e) {
            return "Error occurred while scraping the web page:" + e.getMessage();
        }
    }
}
