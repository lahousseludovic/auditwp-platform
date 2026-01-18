package com.auditwp.platform.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;

public class PdfGeneratorService {

    public static byte[] generatePdfFromHtml(String htmlContent) {
        htmlContent = htmlContent.trim(); // important !

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode(); // optionnel, plus rapide
            builder.withHtmlContent(htmlContent, null); // null = base URI
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF OpenHTMLToPDF", e);
        }
    }
}
