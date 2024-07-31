package com.softwaremagico.tm.factory;


import com.softwaremagico.tm.character.planets.PlanetFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"planetsFactory"})
public class PlanetsFactoryTests {
    private static final String LANGUAGE = "en";

    private static final int DEFINED_PLANETS = 37;;


    @Test
    public void readPlanets() throws IOException {
        Assert.assertEquals(PlanetFactory.getInstance().getElements().size(), DEFINED_PLANETS);
    }

    @Test
    public void readPlanetsFactions() {
        Assert.assertEquals(PlanetFactory.getInstance().getElement("stigmata")
                .getFactions().size(), 3);
    }
}
