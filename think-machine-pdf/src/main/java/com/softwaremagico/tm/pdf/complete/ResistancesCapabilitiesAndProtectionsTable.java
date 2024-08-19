package com.softwaremagico.tm.pdf.complete;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.capabilities.CapabilitiesTableFactory;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.equipment.ArmorTableFactory;
import com.softwaremagico.tm.pdf.complete.equipment.ShieldTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.VictoryPointBankTableFactory;

public class ResistancesCapabilitiesAndProtectionsTable extends BaseElement {
    private static final float[] WIDTHS = {1f, 1f, 1f};

    public static PdfPTable getResistancesAndProtectionsBasicsTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPTable resistancesAndProtectionsBasicsTable = CapabilitiesTableFactory.getCapabilitiesTable(characterPlayer);
        final PdfPCell resistanceAndProtectionCell = new PdfPCell(resistancesAndProtectionsBasicsTable);
        resistanceAndProtectionCell.setColspan(2);
        resistanceAndProtectionCell.setBorder(0);
        table.addCell(resistanceAndProtectionCell);

        final PdfPTable rightColumn = RightColumn.getElements(characterPlayer);
        final PdfPCell rightColumnCell = new PdfPCell(rightColumn);
        rightColumnCell.setRowspan(3);
        rightColumnCell.setBorder(0);
        table.addCell(rightColumnCell);

        final PdfPTable armorTable = ArmorTableFactory.getArmorTable(characterPlayer);
        final PdfPCell armorCell = new PdfPCell(armorTable);
        armorCell.setColspan(2);
        armorCell.setBorder(0);
        table.addCell(armorCell);

        final PdfPCell shieldTable = new PdfPCell(ShieldTableFactory.getShieldTable(characterPlayer));
        shieldTable.setColspan(2);
        shieldTable.setBorder(0);
        table.addCell(shieldTable);

        return table;
    }
}
