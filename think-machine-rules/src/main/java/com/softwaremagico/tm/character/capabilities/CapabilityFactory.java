package com.softwaremagico.tm.character.capabilities;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class CapabilityFactory extends XmlFactory<Capability> {
    private static final String XML_FILE = "capabilities.xml";

    private static final class CapabilityFactoryInit {
        public static final CapabilityFactory INSTANCE = new CapabilityFactory();
    }

    public static CapabilityFactory getInstance() {
        return CapabilityFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Capability> getElements() throws InvalidXmlElementException {
        return readXml(Capability.class);
    }
}
