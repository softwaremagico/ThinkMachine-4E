package com.softwaremagico.tm.factory;


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"raceFactory"})
public class RaceFactoryTests {
    private static final int DEFINED_RACES = 10;


    @Test
    public void readRaces() throws IOException {
        Assert.assertEquals(RaceFactory.getInstance().getElements()
                .size(), DEFINED_RACES);
    }


    @Test
    public void readAfflictions() throws InvalidXmlElementException {
        final Race vorox = RaceFactory.getInstance().getElement("vorox");
        Assert.assertTrue(vorox.getBenefices().contains("noOccult"));
        Assert.assertEquals((int) vorox.getRandomDefinition().getStaticProbability(), 10);
    }

    @Test
    public void readMoreAfflictions() throws InvalidXmlElementException {
        final Race hironem = RaceFactory.getInstance().getElement("hironem");
        Assert.assertTrue(hironem.getBenefices().contains("noPsi"));
    }

    @Test
    public void readRaceExtraPoints() throws InvalidXmlElementException {
        final Race vorox = RaceFactory.getInstance().getElement("vorox");
        Assert.assertEquals(vorox.getCost(), 9);
    }

    @Test
    public void readPlanets() throws InvalidXmlElementException {
        Assert.assertEquals(RaceFactory.getInstance().getElement("shantor")
                .getPlanets().size(), 1);
    }
}
