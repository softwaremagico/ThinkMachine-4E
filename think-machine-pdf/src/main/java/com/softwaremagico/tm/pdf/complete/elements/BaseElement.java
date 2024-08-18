package com.softwaremagico.tm.pdf.complete.elements;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

import java.awt.Color;
import java.io.IOException;

public class BaseElement {
    private static final int TOP_PADDING = 10;
    private static final int WIDTH = 100;
    private static final int MIN_HEIGHT = 10;
    private static final int TITLE_MIN_HEIGHT = 16;
    private static final int TITLE_PADDING_CORRECTION = -2;
    private static final int SEPARATOR_MIN_HEIGHT = 6;
    private static final int BIG_SEPARATOR_MIN_HEIGHT = 15;
    public static final float MARGIN_LENGTH = 0.1f;

    public static PdfPCell getCell(String text, int border, int colspan, int align, Color color, BaseFont font,
                                   float fontSize) {
        if (text == null) {
            text = "";
        }
        final Phrase content = new Phrase(text, new Font(font, fontSize));
        final PdfPCell cell = new PdfPCell(content);
        cell.setColspan(colspan);
        cell.setBorderWidth(border);
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(color);

        return cell;
    }

    public static Chunk getChunk(String text, int maxWidth) {
        return getChunk(text, maxWidth, FadingSunsTheme.LINE_FONT_SIZE);
    }

    public static Chunk getChunk(String text, int maxWidth, int fontSize) {
        return getChunk(text, maxWidth, null, FadingSunsTheme.getLineFont(), fontSize);
    }

    public static Chunk getChunk(String text, int maxWidth, BaseFont font, int fontSize) {
        return getChunk(text, maxWidth, null, font, fontSize);
    }


    public static Chunk getChunk(String text) {
        return getChunk(text, null, FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE);
    }


    public static Chunk getChunk(String text, BaseFont font, int fontSize) {
        return getChunk(text, null, font, fontSize);
    }


    public static Chunk getChunk(String text, Color color, BaseFont font, int fontSize) {
        if (text == null) {
            text = "";
        }
        final Chunk content = new Chunk(text, new Font(font, fontSize));
        if (color != null) {
            content.setBackground(color);
        }

        return content;
    }

    public static Chunk getChunk(String text, int maxWidth, Color color, BaseFont font, int fontSize) {
        if (text == null || text.equals("null")) {
            text = "";
        }
        final String remainingText = CellUtils.getSubStringFitsIn(text, font, fontSize, maxWidth);
        return getChunk(remainingText, color, font, fontSize);
    }


    public static PdfPCell getCell(Paragraph paragraph, int border, int colspan, int align, Color color) {
        final PdfPCell cell = new PdfPCell(paragraph);
        cell.setColspan(colspan);
        cell.setBorderWidth(border);
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(color);

        return cell;
    }

    protected PdfPCell getCell(String text, int border, int colspan, int align, Color color, String font,
                               int fontSize, int fontType) {
        if (text == null) {
            text = "";
        }
        final Paragraph p = new Paragraph(text, FontFactory.getFont(font, fontSize, fontType));
        final PdfPCell cell = new PdfPCell(p);
        cell.setColspan(colspan);
        cell.setBorderWidth(border);
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(color);

        return cell;
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        final Image img = Image.getInstance(path);
        final PdfPCell cell = new PdfPCell(img, true);
        setCellProperties(cell);
        return cell;
    }

    public static PdfPCell createLogoCell() throws DocumentException, IOException {
        final Image image = Image.getInstance(BaseElement.class.getResource("/"
                + FadingSunsTheme.LOGO_IMAGE));
        final PdfPCell cell = new PdfPCell(image, true);
        setCellProperties(cell);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(TOP_PADDING);
        return cell;
    }

    public static void setCellProperties(PdfPCell cell) {
        cell.setBorder(0);
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public static PdfPCell createSeparator() {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(WIDTH);
        table.addCell(createWhiteSeparator());
        table.addCell(createBlackSeparator());
        // table.addCell(createWhiteSeparator());

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        setCellProperties(cell);

        return cell;
    }

    public static PdfPCell createBigSeparator() {
        return createBigSeparator(WIDTH - 2);
    }

    public static PdfPCell createBigSeparator(int width) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(width);
        table.addCell(createWhiteSeparator());
        table.addCell(createBlackSeparator());
        table.addCell(createWhiteSeparator());

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        setCellProperties(cell);

        return cell;
    }

    public static PdfPCell createSectionTitle(String title, int colspan) {
        final Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.SECTION_TITLE_FONT_SIZE);
        font.setColor(Color.WHITE);
        final Phrase content = new Phrase(title, font);
        final PdfPCell titleCell = new PdfPCell(content);
        titleCell.setPaddingTop(TITLE_PADDING_CORRECTION);
        titleCell.setPaddingLeft(FadingSunsTheme.SECTION_TITLE_PADDING);
        titleCell.setColspan(colspan);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleCell.setVerticalAlignment(Element.ALIGN_TOP);
        titleCell.setBackgroundColor(Color.BLACK);
        titleCell.setMinimumHeight(TITLE_MIN_HEIGHT);
        return titleCell;
    }

    public static PdfPCell createBlackSeparator() {
        final PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        setCellProperties(cell);
        cell.setMinimumHeight(MIN_HEIGHT);
        return cell;
    }

    public static PdfPCell createWhiteSeparator(int height) {
        final PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        setCellProperties(cell);
        cell.setMinimumHeight(height);
        return cell;
    }

    public static PdfPCell createWhiteSeparator() {
        return createWhiteSeparator(SEPARATOR_MIN_HEIGHT);
    }

    public static PdfPCell createBigWhiteSeparator() {
        return createWhiteSeparator(BIG_SEPARATOR_MIN_HEIGHT);
    }

    public static void setTableProperties(PdfPTable table) {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBorder(0);
        table.setWidthPercentage(WIDTH);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
    }

    public static PdfPCell createRectangle(int value) {
        return createRectangle(value + "");
    }

    public static PdfPCell createRectangle(String value) {
        // Rectangle
        final PdfPCell rectangle;
        if (value == null) {
            rectangle = CustomPdfTable.createRectangle();
        } else {
            rectangle = CustomPdfTable.createRectangle("");
        }
        rectangle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        return rectangle;
    }

    protected static PdfPCell createEmptyElementLine(String text) {
        return createEmptyElementLine(Element.ALIGN_CENTER, text);
    }

    protected static PdfPCell createEmptyElementLine(int alignment, String text) {
        final PdfPCell cell = BaseElement.getCell(text, 0, 1, alignment, Color.WHITE,
                FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE);
        cell.setMinimumHeight(MIN_HEIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    protected static PdfPCell createEmptyChunk(int alignment, String text) {
        final PdfPCell cell = BaseElement.getCell(text, 0, 1, alignment, Color.WHITE,
                FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE);
        cell.setMinimumHeight(MIN_HEIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public static PdfPCell createEmptyElementLine(String text, int maxWidth) {
        return createEmptyElementLine(text, maxWidth, FadingSunsTheme.TABLE_LINE_FONT_SIZE);
    }

    public static PdfPCell createEmptyElementLine(String text, int maxWidth, int fontSize) {
        final String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getLineFont(), fontSize,
                maxWidth);
        return createEmptyElementLine(remainingText);
    }

    protected static PdfPCell createElementLine(String text, int maxWidth) {
        return createElementLine(text, maxWidth,
                FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
    }

    protected static PdfPCell createElementLine(Integer value, int maxWidth) {
        return createElementLine(value, maxWidth,
                FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
    }

    protected static PdfPCell createElementLine(Integer value, int maxWidth, int fontSize) {
        if (value == null) {
            return createElementLine("", maxWidth, fontSize);
        }
        return createElementLine((value > 0 ? "+" + value : value + ""), maxWidth, fontSize);
    }

    protected static PdfPCell createElementLine(String text, int maxWidth, int fontSize) {
        return createElementLine(text, maxWidth, fontSize, FadingSunsTheme.getHandwrittingFont());
    }

    protected static PdfPCell createElementLine(String text, int maxWidth, int fontSize, BaseFont font) {
        if (text == null || text.equals("null")) {
            text = "";
        }
        final String remainingText = CellUtils.getSubStringFitsIn(text, font, fontSize, maxWidth);
        return createBasicElementLine(remainingText, fontSize, Element.ALIGN_CENTER, font);
    }

    private static PdfPCell createBasicElementLine(String text, int fontSize, int horizontalAlignment, BaseFont font) {
        final PdfPCell cell = BaseElement.getCell(text, 0, 1, horizontalAlignment, Color.WHITE,
                font, fontSize);
        cell.setMinimumHeight(MIN_HEIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
