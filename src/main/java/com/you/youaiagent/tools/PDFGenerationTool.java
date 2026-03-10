package com.you.youaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.you.youaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * PDF 生成工具
 */
public class PDFGenerationTool {
    @Tool(description = "Generate a PDF file with given content")
    public String generatePDF(@ToolParam(description = "name of the file to save generated PDF")
                              String fileName,
                              @ToolParam(description = "content to be included in the PDF")
                              String content) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName;
        try {
            FileUtil.mkdir(fileDir);
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)
            ) {
                //使用内置中文字体
                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                document.setFont(font);
                //创建段落
                Paragraph paragraph = new Paragraph(content);
                //添加段落并关闭文档
                document.add(paragraph);
            }
            return "PDF generated successfully to:" + filePath;
        } catch (Exception e) {
            return "Error generating PDF:" + e.getMessage();
        }
    }
}
