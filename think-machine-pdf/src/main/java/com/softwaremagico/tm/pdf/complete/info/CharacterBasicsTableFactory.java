package com.softwaremagico.tm.pdf.complete.info;

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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.awt.Color;

public abstract class CharacterBasicsTableFactory extends BaseElement {
    private static final int MIN_HEIGHT = 20;

    protected static final String LINE = "_______________";

    protected static PdfPCell createField(CharacterPlayer characterPlayer, String tag, int fontSize, int maxWidth) {
        final float[] widths = {0.7f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(getCell(getTranslatedTag(tag, maxWidth), Element.ALIGN_RIGHT, fontSize));
        if (characterPlayer == null) {
            table.addCell(getCell(LINE, Element.ALIGN_LEFT, fontSize));
        } else {
            switch (tag) {
                case "name":
                    table.addCell(getHandwrittingCell(characterPlayer.getCompleteNameRepresentation(), Element.ALIGN_LEFT,
                            fontSize - 1, maxWidth));
                    break;
                case "gender":
                    table.addCell(getHandwrittingCell(TextFactory.getInstance().getElement(characterPlayer.getInfo().getGender().name().toLowerCase())
                            .getName(), Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    break;
                case "specie":
                    if (characterPlayer.getSpecie() != null) {
                        table.addCell(getHandwrittingCell(SpecieFactory.getInstance().getElement(characterPlayer.getSpecie()).getName(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                case "age":
                    table.addCell(getHandwrittingCell(characterPlayer.getInfo().getAge() != null ? characterPlayer.getInfo().getAge() + ""
                            : "", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    break;
                case "level":
                    table.addCell(getHandwrittingCell(String.valueOf(characterPlayer.getLevel()), Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    break;
                case "upbringing":
                    if (characterPlayer.getUpbringing() != null) {
                        table.addCell(getHandwrittingCell(UpbringingFactory.getInstance().getElement(characterPlayer.getUpbringing().getId()).getName(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                case "faction":
                    if (characterPlayer.getFaction() != null) {
                        table.addCell(getHandwrittingCell(FactionFactory.getInstance().getElement(characterPlayer.getFaction().getId()).getName(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                case "calling":
                    if (characterPlayer.getCalling() != null) {
                        table.addCell(getHandwrittingCell(CallingFactory.getInstance().getElement(characterPlayer.getCalling().getId()).getName(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                case "rank":
                    if (characterPlayer.getRank() != null) {
                        table.addCell(getHandwrittingCell(characterPlayer.getRank().getName().getTranslatedText(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                case "planet":
                    if (characterPlayer.getInfo().getPlanet() != null) {
                        table.addCell(getHandwrittingCell(
                                PlanetFactory.getInstance().getElement(characterPlayer.getInfo().getPlanet()).getNameRepresentation(),
                                Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    } else {
                        table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    }
                    break;
                default:
                    table.addCell(getHandwrittingCell(
                            TextFactory.getInstance().getElement(tag).getName(), Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                    break;
            }
        }

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        setCellProperties(cell);
        cell.setMinimumHeight(MIN_HEIGHT);

        return cell;
    }

    protected static PdfPCell getCell(String text, int align, int fontSize) {
        return getCell(text, 0, 1, align, Color.WHITE, FadingSunsTheme.getLineFont(), fontSize);
    }

    protected static PdfPCell getHandwrittingCell(TranslatedText text, int align, int fontSize, int maxWidth) {
        if (text != null) {
            return getHandwrittingCell(text.getTranslatedText(), align, fontSize, maxWidth);
        } else {
            return getHandwrittingCell("", align, fontSize, maxWidth);
        }
    }

    protected static PdfPCell getHandwrittingCell(String text, int align, int fontSize, int maxWidth) {
        if (text != null && text.length() > maxWidth) {
            text = text.substring(0, maxWidth + 1);
        }
        final PdfPCell cell = getCell(text, 0, 1, align, Color.WHITE, FadingSunsTheme.getHandwrittingFont(),
                FadingSunsTheme.getHandWrittingFontSize(fontSize));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        return cell;
    }

    protected static String getTranslatedTag(String tag, int maxWidth) {
        final String value = TextFactory.getInstance().getElement(tag).getName().getTranslatedText();
        if (value != null && value.length() > maxWidth) {
            return value.substring(0, maxWidth + 1);
        }
        return value + ": ";
    }

}
