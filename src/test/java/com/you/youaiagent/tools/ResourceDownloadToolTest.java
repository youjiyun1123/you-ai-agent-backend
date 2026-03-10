package com.you.youaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://www.codefather.cn/_next/image?url=%2Fimages%2Flogo.png&w=256&q=75";
        String fileName = "logo.png";
        String result = resourceDownloadTool.downloadResource(url, fileName);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}