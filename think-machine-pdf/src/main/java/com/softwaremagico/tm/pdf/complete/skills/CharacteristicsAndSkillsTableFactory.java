package com.softwaremagico.tm.pdf.complete.skills;


import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

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

        return table;
    }

    public static PdfPTable getCharacteristicsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("bodyCharacteristics").getName().getTranslatedText(), 1));
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.BODY));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }
        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("mindCharacteristics").getName().getTranslatedText(), 1));
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.MIND));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }
        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("spiritCharacteristics").getName().getTranslatedText(), 1));
        try {
            table.addCell(CharacteristicsColumn.createContent(characterPlayer, CharacteristicType.SPIRIT));
        } catch (NullPointerException npe) {
            PdfExporterLog.errorMessage(CharacteristicsAndSkillsTableFactory.class.getName(), npe);
        }

        return table;
    }
}
