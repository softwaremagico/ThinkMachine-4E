package com.softwaremagico.tm.character.equipment.item.handheldshield;


import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class HandheldShieldFactory extends XmlFactory<HandheldShield> {
    private static final String XML_FILE = "handheldShield.xml";

    private static final class HandheldShieldFactoryInit {
        public static final HandheldShieldFactory INSTANCE = new HandheldShieldFactory();
    }

    public static HandheldShieldFactory getInstance() {
        return HandheldShieldFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<HandheldShield> getElements() throws InvalidXmlElementException {
        return readXml(HandheldShield.class);
    }
}
