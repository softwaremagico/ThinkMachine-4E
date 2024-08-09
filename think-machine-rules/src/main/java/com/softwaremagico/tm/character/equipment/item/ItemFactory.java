package com.softwaremagico.tm.character.equipment.item;

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class ItemFactory extends XmlFactory<Item> {
    private static final String XML_FILE = "items.xml";

    private static final class EquipmentFactoryInit {
        public static final ItemFactory INSTANCE = new ItemFactory();
    }

    public static ItemFactory getInstance() {
        return EquipmentFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Item> getElements() throws InvalidXmlElementException {
        return readXml(Item.class);
    }
}
