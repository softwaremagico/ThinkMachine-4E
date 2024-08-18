package com.softwaremagico.tm.pdf.complete.resistances;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.txt.TextFactory;

public class ResistanceTableFactory extends BaseElement {

    public static PdfPTable getResistancesAndProtectionsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(getResistancesTable(characterPlayer));

        final PdfPTable vitalityTable = new PdfPTable(new float[]{1f});
        setTableProperties(vitalityTable);
        vitalityTable.addCell(getVitalityTable(characterPlayer));
        vitalityTable.addCell(getRevivalsTable(characterPlayer));

        table.addCell(vitalityTable);

        return table;
    }

    public static PdfPTable getResistancesTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 0.9f, 1.1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("resistances").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("bodyResistance").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(createRectangle(null));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("mindResistance").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getMindResistance() : null));
        table.addCell(createRectangle(null));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("spiritResistance").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getSpiritResistance() : null));
        table.addCell(createRectangle(null));

        return table;
    }

    private static PdfPCell createRectangle(Integer value) {
        // Rectangle
        final PdfPCell rectangle;
        if (value == null) {
            rectangle = CustomPdfTable.createRectangle();
        } else {
            rectangle = CustomPdfTable.createRectangle(value + "");
        }
        rectangle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        return rectangle;
    }

    public static PdfPTable getVitalityTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("vitality").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("maximumAbbreviation").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("current").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(null));

        return table;
    }

    public static PdfPTable getRevivalsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("revivals").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("quantity").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("number").getName().getTranslatedText(),
                FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
        table.addCell(createRectangle(null));

        return table;
    }
}
