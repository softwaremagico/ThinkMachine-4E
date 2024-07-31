package com.softwaremagico.tm.character.planets;

import com.softwaremagico.tm.xml.XmlFactory;

import java.io.IOException;
import java.util.List;

public class PlanetFactory extends XmlFactory<Planet> {
    private static final String XML_FILE = "planets.xml";

    private static final class PlanetFactoryInit {
        public static final PlanetFactory INSTANCE = new PlanetFactory();
    }

    public static PlanetFactory getInstance() {
        return PlanetFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Planet> getElements() throws IOException {
        return readXml(Planet.class);
    }
}
