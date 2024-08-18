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

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.txt.TextFactory;

public class ArmorTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "___________________";
    private static final int NAME_COLUMN_WIDTH = 70;

    public static PdfPTable getArmorTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createArmorValueTable(characterPlayer));

        return table;
    }

    private static PdfPTable createArmorValueTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 0.9f, 1.1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("armor").getName().getTranslatedText(), widths.length));

        final Paragraph armor = new Paragraph();
        if (characterPlayer == null || characterPlayer.getArmor() == null) {
            armor.add(BaseElement.getChunk(GAP));
        } else {
            armor.add(BaseElement.getChunk(characterPlayer.getArmor().getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                    FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE));
        }
        PdfPCell armorCell = new PdfPCell(armor);
        armorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        armorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        armorCell.setBorder(0);

        table.addCell(armorCell);
        table.addCell(getResistance(characterPlayer));
        table.addCell(getPenalties(characterPlayer));

        final PdfPCell damageCell = new PdfPCell(getDamageProtection(characterPlayer));
        damageCell.setColspan(widths.length);
        damageCell.setBorder(0);
        table.addCell(damageCell);
        return table;
    }

    private static PdfPCell getPenalties(CharacterPlayer characterPlayer) {
        final PdfPCell malusCell = new PdfPCell();
        malusCell.setBorder(0);
        malusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        malusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (characterPlayer == null || characterPlayer.getArmor() == null) {
            final Paragraph paragraph = new Paragraph();
            paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getTranslatedText("strengthAbbreviation") + ":__  "));
            paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getTranslatedText("dexterityAbbreviation") + ":__  "));
            paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getTranslatedText("enduranceAbbreviation") + ":__ "));
            paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getTranslatedText("initiativeAbbreviation") + ":__"));
            malusCell.setPhrase(paragraph);
        } else {
            final Paragraph paragraph = new Paragraph();
            paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getTranslatedText("strengthAbbreviation") + ":",
                    FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE));
            paragraph.add(BaseElement.getChunk(characterPlayer.getArmor().getStandardPenalization().getStrengthModification()
                    + " ", FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE));

            paragraph.add(BaseElement.getChunk(" " + TextFactory.getInstance().getTranslatedText("dexterityAbbreviation") + ":",
                    FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE));
            paragraph.add(BaseElement.getChunk(characterPlayer.getArmor().getStandardPenalization()
                    .getDexterityModification()
                    + " ", FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE));

            malusCell.setPhrase(paragraph);
        }
        return malusCell;
    }

    private static PdfPTable getResistance(CharacterPlayer characterPlayer) {
        final PdfPTable resistanceTable = new PdfPTable(new float[]{2f, 1f});
        setTableProperties(resistanceTable);

        final Paragraph resistance = new Paragraph();
        resistance.add(BaseElement.getChunk(TextFactory.getInstance().getElement("resistance").getName().getTranslatedText() + ": "));
        resistance.setAlignment(Element.ALIGN_RIGHT);
        resistanceTable.addCell(resistance);
        // Rectangle
        final PdfPCell rectangle;
        if (characterPlayer == null) {
            rectangle = CustomPdfTable.createRectangle();
        } else {
            rectangle = CustomPdfTable.createRectangle(characterPlayer.getArmor().getProtection() + "");
        }
        rectangle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        resistanceTable.addCell(rectangle);
        return resistanceTable;
    }

    private static PdfPTable getArmorProperty(String text, boolean selected) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (!selected) {
            table.addCell(createRectangle(""));
        } else {
            table.addCell(createRectangle("X"));
        }
        table.addCell(createEmptyElementLine(text, NAME_COLUMN_WIDTH));

        return table;
    }

    private static PdfPTable getDamageProtection(CharacterPlayer characterPlayer) {
        final PdfPTable damages = new PdfPTable(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f});
        setTableProperties(damages);
        if (characterPlayer == null || characterPlayer.getArmor() == null) {
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorHardAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorFlameAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorLaserAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorBlasterAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorShockAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorSlamAbbreviation"), false));
            damages.addCell(getArmorProperty(TextFactory.getInstance().getTranslatedText("armorSonicAbbreviation"), false));
        } else {
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorHardAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("hard").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorFlameAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("fire").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorLaserAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("laser").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorBlasterAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("blaster").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorShockAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("shock").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorSlamAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("slam").getId())));
            damages.addCell(getArmorProperty(
                    TextFactory.getInstance().getTranslatedText("armorSonicAbbreviation"),
                    characterPlayer
                            .getArmor()
                            .getDamageTypes()
                            .contains(
                                    DamageTypeFactory.getInstance().getElement("sonic").getId())));
        }
        return damages;
    }
}
