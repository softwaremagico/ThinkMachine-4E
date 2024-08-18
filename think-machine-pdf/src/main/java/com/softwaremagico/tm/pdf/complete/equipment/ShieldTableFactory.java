package com.softwaremagico.tm.pdf.complete.equipment;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class ShieldTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "______";
    private static final int COLUMN_WIDTH = 90;

    public static PdfPTable getShieldTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createShieldValues(characterPlayer));

        return table;
    }

    private static PdfPTable createShieldValues(CharacterPlayer characterPlayer) {
        final float[] widths = {1.5f, 1f, 1.5f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("shield").getName().getTranslatedText(), widths.length));

        final PdfPCell nameCell;
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            nameCell = createEmptyElementLine(GAP + GAP + GAP, COLUMN_WIDTH);
        } else {
            nameCell = createElementLine(characterPlayer.getShield().getName().getTranslatedText(), COLUMN_WIDTH,
                    FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE);
        }
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(nameCell);

        table.addCell(getShieldRange(characterPlayer));
        table.addCell(getShieldDetails(characterPlayer));

        final Paragraph paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("shieldHits").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getHits() + " - ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        final PdfPCell protectionCell = new PdfPCell();
        protectionCell.setPhrase(paragraph);
        protectionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        protectionCell.setColspan(widths.length);
        protectionCell.setBorder(0);
        table.addCell(protectionCell);

        return table;
    }

    private static PdfPTable getShieldDetails(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


        Paragraph paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("burnOut").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getBurnOut() + "   ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        table.addCell(paragraph);

        paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("distortion").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getDistortion() + "   ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        table.addCell(paragraph);

        return table;
    }


    private static PdfPTable getShieldRange(CharacterPlayer characterPlayer) {
        final float[] widths = {3f, 1f, 3f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (characterPlayer == null || characterPlayer.getShield() == null) {
            table.addCell(createRectangle(""));
        } else {
            table.addCell(createRectangle(characterPlayer.getShield().getImpact()));
        }
        table.addCell(createEmptyElementLine("/"));
        final PdfPCell rectangle;
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            rectangle = createRectangle("");
        } else {
            rectangle = createRectangle(characterPlayer.getShield().getForce());
        }
        rectangle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        table.addCell(rectangle);

        return table;
    }
}
