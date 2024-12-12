package com.softwaremagico.tm.character.cybernetics;

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class CyberdeviceFactory extends XmlFactory<Cyberdevice> {
    private static final String XML_FILE = "cyberdevices.xml";

    private static final class CyberdeviceFactoryInit {
        public static final CyberdeviceFactory INSTANCE = new CyberdeviceFactory();
    }

    public static CyberdeviceFactory getInstance() {
        return CyberdeviceFactory.CyberdeviceFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Cyberdevice> getElements() throws InvalidXmlElementException {
        return readXml(Cyberdevice.class);
    }
}
