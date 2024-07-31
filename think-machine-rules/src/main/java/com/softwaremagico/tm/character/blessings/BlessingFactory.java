package com.softwaremagico.tm.character.blessings;

import com.softwaremagico.tm.xml.XmlFactory;

import java.io.IOException;
import java.util.List;

public class BlessingFactory extends XmlFactory<Blessing> {
    private static final String XML_FILE = "blessings.xml";

    private static final class BlessingFactoryInit {
        public static final BlessingFactory INSTANCE = new BlessingFactory();
    }

    public static BlessingFactory getInstance() {
        return BlessingFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Blessing> getElements() throws IOException {
        return readXml(Blessing.class);
    }
}
