package com.softwaremagico.tm.pdf.complete.resistances;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.equipment.ArmorTableFactory;
import com.softwaremagico.tm.pdf.complete.equipment.ShieldTableFactory;
import com.softwaremagico.tm.pdf.complete.occultism.OccultismValuesTableFactory;

public class ResistancesAndProtectionsTable extends BaseElement {
    private static final float[] WIDTHS = {1f, 1f, 1f};

    public static PdfPTable getResistancesAndProtectionsBasicsTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPTable resistancesAndProtectionsBasicsTable = ResistanceTableFactory.getResistancesAndProtectionsBasicsTable(characterPlayer);
        final PdfPCell resistanceAndProtectionCell = new PdfPCell(resistancesAndProtectionsBasicsTable);
        resistanceAndProtectionCell.setColspan(2);
        resistanceAndProtectionCell.setBorder(0);
        table.addCell(resistanceAndProtectionCell);

        final PdfPTable occultismValuesTable = OccultismValuesTableFactory.getOccultismValuesTable(characterPlayer);
        final PdfPCell occultismCell = new PdfPCell(occultismValuesTable);
        occultismCell.setBorder(0);
        table.addCell(occultismCell);

        final PdfPTable armorTable = ArmorTableFactory.getArmorTable(characterPlayer);
        final PdfPCell armorCell = new PdfPCell(armorTable);
        armorCell.setColspan(2);
        armorCell.setBorder(0);
        table.addCell(armorCell);

        final PdfPTable victoryPointBankTable = VictoryPointBankTableFactory.getVictoryPointBankTable(characterPlayer);
        final PdfPCell victoryPointsCell = new PdfPCell(victoryPointBankTable);
        victoryPointsCell.setColspan(1);
        victoryPointsCell.setRowspan(2);
        victoryPointsCell.setBorder(0);
        table.addCell(victoryPointsCell);

        final PdfPCell shieldTable = new PdfPCell(ShieldTableFactory.getShieldTable(characterPlayer));
        shieldTable.setColspan(2);
        shieldTable.setBorder(0);
        table.addCell(shieldTable);

        return table;
    }
}
