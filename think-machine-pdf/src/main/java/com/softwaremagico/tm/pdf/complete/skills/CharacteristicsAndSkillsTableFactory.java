package com.softwaremagico.tm.pdf.complete.skills;

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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.Comparator;
import java.util.List;

public class CharacteristicsAndSkillsTableFactory extends BaseElement {

    public static PdfPTable getCharacteristicsAndSkillsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {2f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(getSkillsBasicsTable(characterPlayer));
        table.addCell(getCharacteristicsBasicsTable(characterPlayer));

        return table;
    }

    public static PdfPTable getSkillsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("skills").getName().getTranslatedText(), widths.length));

        final List<Skill> skills = SkillFactory.getInstance().getElements();
        skills.sort(Comparator.comparing(o -> o.getName().getTranslatedText()));
        table.addCell(SkillsColumn.createContent(characterPlayer, skills.subList(0, (skills.size() / 2))));
        table.addCell(SkillsColumn.createContent(characterPlayer, skills.subList(skills.size() / 2, skills.size())));

        return table;
    }

    public static PdfPTable getCharacteristicsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("bodyCharacteristics").getName().getTranslatedText(), 1));
        table.addCell(BaseElement.createWhiteSeparator());
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.BODY));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }
        table.addCell(BaseElement.createBigWhiteSeparator());
        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("mindCharacteristics").getName().getTranslatedText(), 1));
        table.addCell(BaseElement.createWhiteSeparator());
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.MIND));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }
        table.addCell(BaseElement.createBigWhiteSeparator());
        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("spiritCharacteristics").getName().getTranslatedText(), 1));
        table.addCell(BaseElement.createWhiteSeparator());
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.SPIRIT));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }

        return table;
    }
}
