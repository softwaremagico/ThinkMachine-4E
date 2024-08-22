package com.softwaremagico.tm.pdf.complete.perks;

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

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.Collections;
import java.util.List;

public class PerksTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "_________________________";
    private static final int NAME_COLUMN_WIDTH = 115;
    private static final int TOTAL_CELLS = 21;

    public static PdfPTable getPerksTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(getPerksValues(characterPlayer));

        return table;
    }

    private static PdfPTable getPerksValues(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("perks").getName().getTranslatedText(), widths.length));

        int totalElements = 0;
        if (characterPlayer != null) {
            final List<Perk> perks = characterPlayer.getPerks();
            //Faction perks
            if (characterPlayer.getFaction() != null) {
                final Faction faction = FactionFactory.getInstance().getElement(characterPlayer.getFaction().getId());
                perks.addAll(faction.getPerks());
            }
            if (!perks.isEmpty()) {
                Collections.sort(perks);
                for (Perk perk : perks) {
                    final PdfPCell cell = new PdfPCell(new Paragraph(BaseElement.getChunk(perk.getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                            FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.HANDWRITTING_DEFAULT_FONT_SIZE)));
                    cell.setBorder(0);
                    cell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    totalElements++;
                }
            }
        }
        for (int j = 0; j < (TOTAL_CELLS - totalElements); j++) {
            final PdfPCell emptyCell = new PdfPCell(new Paragraph(BaseElement.getChunk(GAP, NAME_COLUMN_WIDTH)));
            emptyCell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
            emptyCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            emptyCell.setBorder(0);
            table.addCell(emptyCell);
        }
        return table;
    }
}
