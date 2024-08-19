package com.softwaremagico.tm.pdf.complete.capabilities;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CapabilitiesTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "_________________________";
    private static final int NAME_COLUMN_WIDTH = 115;
    private static final int TOTAL_CELLS = 18;

    public static PdfPTable getCapabilitiesTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(getCapabilitiesValues(characterPlayer));

        return table;
    }

    private static PdfPTable getCapabilitiesValues(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("capabilities").getName().getTranslatedText(), widths.length));

        int totalElements = 0;
        if (characterPlayer != null && !characterPlayer.getCapabilities().isEmpty()) {
            final List<Capability> capabilities = new ArrayList<>(characterPlayer.getCapabilities());
            Collections.sort(capabilities);
            for (Capability capability : capabilities) {
                final PdfPCell cell = new PdfPCell(new Paragraph(BaseElement.getChunk(capability.getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                        FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.HANDWRITTING_DEFAULT_FONT_SIZE)));
                //cell.setPaddingLeft(FadingSunsTheme.LINE_PADDING);
                cell.setBorder(0);
                cell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
                table.addCell(cell);
                totalElements++;
            }
        }
        for (int j = 0; j < (TOTAL_CELLS - totalElements); j++) {
            final PdfPCell emptyCell = new PdfPCell(new Paragraph(BaseElement.getChunk(GAP, NAME_COLUMN_WIDTH)));
            //emptyCell.setPaddingLeft(FadingSunsTheme.LINE_PADDING);
            emptyCell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
            emptyCell.setBorder(0);
            table.addCell(emptyCell);
        }
        return table;
    }
}
