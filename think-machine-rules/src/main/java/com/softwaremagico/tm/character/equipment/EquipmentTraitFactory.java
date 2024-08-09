package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class EquipmentTraitFactory extends XmlFactory<EquipmentTrait> {
    private static final String XML_FILE = "equipment_traits.xml";

    private static final class EquipmentTraitFactoryInit {
        public static final EquipmentTraitFactory INSTANCE = new EquipmentTraitFactory();
    }

    public static EquipmentTraitFactory getInstance() {
        return EquipmentTraitFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<EquipmentTrait> getElements() throws InvalidXmlElementException {
        return readXml(EquipmentTrait.class);
    }
}
