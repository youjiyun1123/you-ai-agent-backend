package com.you.youaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool pdfGenerationTool=new PDFGenerationTool();
        String fileName="测试PDF.pdf";
        String content="测试PDF       www.baidu.com";
        String result=pdfGenerationTool.generatePDF(fileName,content);
        Assertions.assertNotNull(result);
    }
}