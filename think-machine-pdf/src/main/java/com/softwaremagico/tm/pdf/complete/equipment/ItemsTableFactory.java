package com.softwaremagico.tm.pdf.complete.equipment;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemsTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f, 1f};
    private static final String GAP = "_______________";
    private static final int NAME_COLUMN_WIDTH = 290;
    private static final int TECH_LEVEL_COLUMN_WIDTH = 30;
    private static final int SIZE_COLUMN_WIDTH = 30;
    private static final int ROWS = 6;

    public static PdfPTable getItemsTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("items").getName(), WIDTHS.length));

        final List<Item> items = characterPlayer.getItems();
        final List<Item> itemsFirstColumn = items.subList(0, Math.min(items.size(), ROWS));
        List<Item> itemsSecondColumn;
        try {
            itemsSecondColumn = items.subList(Math.min(items.size(), ROWS), items.size());
        } catch (IndexOutOfBoundsException e) {
            itemsSecondColumn = new ArrayList<>();
        }
        table.addCell(getItemsValueTable(itemsFirstColumn));
        table.addCell(getItemsValueTable(itemsSecondColumn));

        return table;
    }

    private static PdfPTable getItemsValueTable(List<Item> items) {
        final float[] widths = {4f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("item").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("techLevel").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("size").getName()));

        int addedItems = 0;
        for (Item item : items) {
            table.addCell(createFirstElementLine(item.getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                    FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            table.addCell(createElementLine((item.getTechLevel() != null ? String.valueOf(item.getTechLevel()) : ""),
                    TECH_LEVEL_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            table.addCell(createElementLine((item.getSize() != null ? item.getSize().name() : ""),
                    SIZE_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            addedItems++;
        }

        for (int i = 0; i < ROWS - addedItems; i++) {
            table.addCell(createEmptyElementLine(GAP + GAP + GAP, NAME_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, TECH_LEVEL_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, SIZE_COLUMN_WIDTH));
        }

        return table;
    }

}
