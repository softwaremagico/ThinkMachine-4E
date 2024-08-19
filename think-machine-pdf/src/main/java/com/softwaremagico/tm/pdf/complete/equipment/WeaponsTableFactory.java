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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class WeaponsTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final int ROWS = 6;
    private static final String GAP = "__________________";
    private static final int NAME_COLUMN_WIDTH = 65;
    private static final int ROLL_COLUMN_WIDTH = 45;
    private static final int GOAL_COLUMN_WIDTH = 15;
    private static final int DAMAGE_COLUMN_WIDTH = 30;
    private static final int STRENGTH_COLUMN_WIDTH = 30;
    private static final int RANGE_COLUMN_WIDTH = 30;
    private static final int SHOTS_COLUMN_WIDTH = 30;
    private static final int RATE_COLUMN_WIDTH = 30;
    private static final int SIZE_COLUMN_WIDTH = 15;
    private static final int OTHERS_COLUMN_WIDTH = 75;

    public static PdfPTable getWeaponsTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createWeaponsValues(characterPlayer));

        return table;
    }

    private static PdfPTable createWeaponsValues(CharacterPlayer characterPlayer) {
        final float[] widths = {6f, 5f, 2f, 3f, 3f, 3f, 3f, 3f, 2f, 7f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);


        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("weapons").getName(), widths.length));

        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weapon").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponRoll").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponGoal").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponDamage").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponStrength").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponRange").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponShots").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponRate").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("size").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("weaponsOthers").getName()));

        int addedWeapons = 0;
        if (characterPlayer != null) {
            for (final Weapon weapon : characterPlayer.getWeapons()) {
                if (weapon.getWeaponDamages().isEmpty()) {
                    continue;
                }
                setDamageLine(table, weapon, weapon.getWeaponDamages().get(0),
                        weapon.getSize() != null ? weapon.getSize().toString() : "", weapon.getWeaponOthersText());
                addedWeapons++;

                //Secondary damages.
                if (weapon.getWeaponDamages().size() > 1) {
                    for (int i = 1; i < weapon.getWeaponDamages().size(); i++) {
                        setDamageLine(table, weapon, weapon.getWeaponDamages().get(i), "", "");
                        addedWeapons++;
                    }
                }

                for (final Ammunition ammunition : AmmunitionFactory.getInstance().getElements(weapon.getAmmunition())) {
                    table.addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine("", ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine((ammunition.getGoal() != null ? weapon.getWeaponDamages().get(0).getGoal() : ""), GOAL_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(ammunition.getDamage(), DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(ammunition.getStrength(), STRENGTH_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(ammunition.getRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine("", RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(ammunition.getSize() != null ? ammunition.getSize().toString() : "",
                            SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine("", OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addedWeapons++;
                }
            }
        }

        for (int i = 0; i < ROWS - addedWeapons; i++) {
            table.addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, ROLL_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, GOAL_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, DAMAGE_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, STRENGTH_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, RANGE_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, SHOTS_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, RATE_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, SIZE_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, OTHERS_COLUMN_WIDTH));
        }

        return table;
    }

    private static void setDamageLine(PdfPTable table, Weapon weapon, WeaponDamage weaponDamage, String size, String others) {
        table.addCell(createElementLine(weaponDamage.getName() != null ? weaponDamage.getName().getTranslatedText() : weapon.getName().getTranslatedText(),
                NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(weaponDamage.getRoll(), ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine((weaponDamage.getGoal() != null ? weaponDamage.getGoal() : ""), GOAL_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(weaponDamage.getDamage(), DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(String.valueOf(weaponDamage.getStrength()), STRENGTH_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(weaponDamage.getRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(weaponDamage.getShots() == null || weaponDamage.getShots() == 0 ? "" : weaponDamage.getShots() + "", SHOTS_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(weaponDamage.getRate(), RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(size, SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        table.addCell(createElementLine(others, OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
    }
}
