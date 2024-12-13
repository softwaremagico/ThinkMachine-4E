package com.softwaremagico.tm.pdf.complete.equipment;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
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

        final List<Element> items = new ArrayList<>();

        items.addAll(characterPlayer != null ? new ArrayList<>(characterPlayer.getCyberdevices()) : new ArrayList<>());
        items.addAll(characterPlayer != null ? new ArrayList<>(characterPlayer.getItems()) : new ArrayList<>());
        final List<Element> itemsFirstColumn = items.subList(0, Math.min(items.size(), ROWS));
        List<Element> itemsSecondColumn;
        try {
            itemsSecondColumn = items.subList(Math.min(items.size(), ROWS), items.size());
        } catch (IndexOutOfBoundsException e) {
            itemsSecondColumn = new ArrayList<>();
        }
        table.addCell(getItemsValueTable(itemsFirstColumn));
        table.addCell(getItemsValueTable(itemsSecondColumn));

        return table;
    }

    private static PdfPTable getItemsValueTable(List<Element> items) {
        final float[] widths = {4f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("item").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("techLevel").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("size").getName()));

        int addedItems = 0;
        for (Element item : items) {
            table.addCell(createFirstElementLine(item.getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                    FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            if (item instanceof Item) {
                table.addCell(createElementLine((((Item) item).getTechLevel() != null ? String.valueOf(((Item) item).getTechLevel()) : ""),
                        TECH_LEVEL_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
                table.addCell(createElementLine((((Item) item).getSize() != null ? ((Item) item).getSize().name() : ""),
                        SIZE_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            } else if (item instanceof Cyberdevice) {
                table.addCell(createElementLine((((Cyberdevice) item).getTechLevel() != null ? String.valueOf(((Cyberdevice) item).getTechLevel()) : ""),
                        TECH_LEVEL_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
                table.addCell(createElementLine((((Cyberdevice) item).getSize() != null ? ((Cyberdevice) item).getSize().name() : ""),
                        SIZE_COLUMN_WIDTH, FadingSunsTheme.ITEMS_CONTENT_FONT_SIZE));
            }
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
