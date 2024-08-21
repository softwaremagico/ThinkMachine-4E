package com.softwaremagico.tm.pdf.complete.info;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class DescriptionTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "_______________________________________________";
    private static final int COLUMN_WIDTH = 174;

    public static PdfPTable getDescriptionTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createDescriptionValue(characterPlayer));

        return table;
    }

    private static PdfPTable createDescriptionValue(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("description").getName(), widths.length));
        table.addCell(createLine(characterPlayer, "birthdate", (characterPlayer == null
                || characterPlayer.getInfo().getBirthdate() == null) ? null : characterPlayer.getInfo().getBirthdate()));
        table.addCell(createLine(characterPlayer, "hair", (characterPlayer == null
                || characterPlayer.getInfo().getHair() == null) ? null : characterPlayer.getInfo().getHair()));
        table.addCell(createLine(characterPlayer, "eyes", (characterPlayer == null
                || characterPlayer.getInfo().getEyes() == null) ? null : characterPlayer.getInfo().getEyes()));
        table.addCell(createLine(characterPlayer, "complexion", (characterPlayer == null
                || characterPlayer.getInfo().getComplexion() == null) ? null : characterPlayer.getInfo().getComplexion()));
        table.addCell(createLine(characterPlayer, "weight", (characterPlayer == null
                || characterPlayer.getInfo().getWeight() == null) ? null : characterPlayer.getInfo().getWeight()));
        table.addCell(createLine(characterPlayer, "appearance", null));
        table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
        table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
        table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));

        return table;
    }

    private static PdfPCell createLine(CharacterPlayer characterPlayer, String tag, String value) {
        final Paragraph paragraph = new Paragraph();

        final String text = getTranslatedTag(tag);
        // Spaces at the end are eliminated. For calculating width we can put
        // the characters in different order.
        final float textWidth = FadingSunsTheme.getLineFont().getWidthPoint(text + " :",
                FadingSunsTheme.LINE_FONT_SIZE);

        paragraph.add(BaseElement.getChunk(text + ": "));
        if (characterPlayer == null || value == null) {
            paragraph.add(BaseElement.getChunk(GAP, (int) (COLUMN_WIDTH - textWidth)));
        } else {
            paragraph.add(BaseElement.getChunk(value, (int) (COLUMN_WIDTH - textWidth)));
        }

        final PdfPCell cell = createEmptyElementLine("");
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPhrase(paragraph);

        return cell;
    }

    private static String getTranslatedTag(String tag) {
        return TextFactory.getInstance().getTranslatedText(tag);
    }
}
