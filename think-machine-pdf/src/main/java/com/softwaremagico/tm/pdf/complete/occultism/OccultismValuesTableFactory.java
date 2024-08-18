package com.softwaremagico.tm.pdf.complete.occultism;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.txt.TextFactory;

public class OccultismValuesTableFactory extends BaseElement {


    public static PdfPTable getOccultismValuesTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("occultism").getName().getTranslatedText(), widths.length));

        table.addCell(createPsiTable(characterPlayer));
        table.addCell(createTheurgyTable(characterPlayer));

        return table;
    }

    private static PdfPTable createPsiTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createContent(characterPlayer, CharacteristicName.PSI, false));
        table.addCell(createContent(characterPlayer, CharacteristicName.URGE, true));

        return table;
    }

    private static PdfPTable createTheurgyTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createContent(characterPlayer, CharacteristicName.THEURGY, false));
        table.addCell(createContent(characterPlayer, CharacteristicName.HUBRIS, true));

        return table;
    }

    public static PdfPCell createContent(CharacterPlayer characterPlayer, CharacteristicName characteristicName, boolean inverse) {
        final float[] widths;
        if (!inverse) {
            widths = new float[]{3f, 1.5f, 0.1f};
        } else {
            widths = new float[]{1.5f, 3f, 0.1f};
        }
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

        if (characteristicName != null) {
            final Paragraph paragraph = new Paragraph();
            paragraph.add(new Paragraph(TextFactory.getInstance().getElement(characteristicName.getId()).getName().getTranslatedText(),
                    new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
            final PdfPCell characteristicTitle = new PdfPCell(paragraph);
            characteristicTitle.setBorder(0);
            characteristicTitle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
            characteristicTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            if (!inverse) {
                characteristicTitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
            } else {
                characteristicTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            }

            // Rectangle
            final PdfPCell rectangle;
            if (characterPlayer == null) {
                rectangle = CustomPdfTable.createRectangle();
            } else {
                rectangle = CustomPdfTable.createRectangle(characterPlayer.getCharacteristicValue(characteristicName) + "");
            }

            if (!inverse) {
                table.addCell(characteristicTitle);
                table.addCell(rectangle);
            } else {
                table.addCell(rectangle);
                table.addCell(characteristicTitle);
            }

            // Margin
            final PdfPCell margin = new PdfPCell();
            margin.setBorder(0);
            table.addCell(margin);
        }

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        BaseElement.setCellProperties(cell);

        return cell;
    }
}
