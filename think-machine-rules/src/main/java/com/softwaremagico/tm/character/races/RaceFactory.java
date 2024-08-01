package com.softwaremagico.tm.character.races;

import com.softwaremagico.tm.xml.XmlFactory;

import java.io.IOException;
import java.util.List;

public class RaceFactory extends XmlFactory<Race> {
    private static final String XML_FILE = "races.xml";

    private static final class RaceFactoryInit {
        public static final RaceFactory INSTANCE = new RaceFactory();
    }

    public static RaceFactory getInstance() {
        return RaceFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Race> getElements() throws IOException {
        return readXml(Race.class);
    }
}
