package com.panickapps.pdfboxutils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PDFBoxUtils {

    public static void addBlankPage(PDDocument document) {
        PDPage page = new PDPage();
        document.addPage(page);
    }

    public static PDPage getPage(PDDocument document, int pageNumber) {
        if (pageNumber >= document.getNumberOfPages() || pageNumber< 0)
            throw new RuntimeException("Cannot get page " + pageNumber + " from a " + document.getNumberOfPages() + " page document. Indexes start from 0.");
        else {
            return (PDPage) document.getDocumentCatalog().getAllPages().get(pageNumber);
        }
    }

    public static PDDocument loadDocument(String filename) {
        File file = new File(filename);
        try {
            return PDDocument.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to load PDF document from file: " + filename);
    }

    public static void removePage(PDDocument document, int page) {
        final int NUM_PAGES = document.getNumberOfPages();
        if (page >= NUM_PAGES || page < 0) throw new RuntimeException("Cannot delete page " + page + " from a " + NUM_PAGES + " page document. Indexes start from 0.");
        else {
            document.removePage(getPage(document, page));
        }
    }

    public static void addText(PDDocument document, PDPage page, String text, Color color, PDType1Font font, int fontSize, float xPos, float yPos) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page, true, false);

            contentStream.beginText();

            contentStream.setFont(font, fontSize);
            contentStream.moveTextPositionByAmount(xPos, yPos);
            contentStream.setNonStrokingColor(color);
            contentStream.drawString(text);

            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRectangle(PDDocument document, PDPage page, Rectangle rectangle, boolean fill, Color color) {
        PDPageContentStream contentStream = null;
        try {
            contentStream = new PDPageContentStream(document, page, true, false);
            contentStream.setNonStrokingColor(color);
            contentStream.addRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            if (fill) contentStream.fill(1);
            contentStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addImage(PDDocument document, PDPage page, String imageFilepath, int posX, int posY, float scale) {
        PDPageContentStream contentStream = null;
        try {
            contentStream = new PDPageContentStream(document, page, true, false);

            PDXObjectImage image;
            if (imageFilepath.endsWith(".jpg")) {
                image = new PDJpeg(document, new FileInputStream(imageFilepath));
            }
            else {
                BufferedImage awtImage = ImageIO.read(new File(imageFilepath));
                image = new PDPixelMap(document, awtImage);
            }

            contentStream.drawXObject(image, posX, posY, image.getWidth() * scale, image.getHeight() * scale);

            contentStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
