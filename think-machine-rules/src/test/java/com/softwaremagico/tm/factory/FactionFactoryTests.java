package com.softwaremagico.tm.factory;


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.random.RandomFactionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"factionsFactory"})
public class FactionFactoryTests {
    private static final int DEFINED_FACTIONS = 62;
    private static final int DEFINED_MALE_NAMES = 103;
    private static final int DEFINED_FEMALE_NAMES = 100;
    private static final int DEFINED_SURNAMES = 125;
    private static final String LANGUAGE = "es";
    private static final int VERSION = 1;


    @Test
    public void checkTotalElements() throws IOException {
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS);
    }


    @Test
    public void readNames() throws InvalidXmlElementException {
        final Faction hazat = FactionFactory.getInstance().getElement("hazat");
        Assert.assertNotNull(hazat);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElement("hazat").getNames(Gender.MALE).size() >= DEFINED_MALE_NAMES);
        Assert.assertTrue(
                RandomFactionFactory.getInstance().getElement("hazat").getNames(Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElement("hazat").getData().getSurnames().size() >= DEFINED_SURNAMES);
    }

    @Test
    public void checkSuggestedBenefices() throws InvalidXmlElementException {
        final Faction obun = FactionFactory.getInstance().getElement("obun");
        Assert.assertEquals(obun.getSuggestedBenefices().size(), 1);
        Assert.assertEquals(obun.getSuggestedBenefices().iterator().next().getId(), "refuge");
        Assert.assertEquals(obun.getSuggestedBenefices().iterator().next().getValue(), 4);
    }

//    @Test
//    public void checkRestrictedBenefices() throws InvalidXmlElementException {
//        final Faction amaltheans = FactionFactory.getInstance().getElement("amaltheans");
//        Assert.assertEquals(amaltheans.getRestrictedBenefices().size(), 1);
//        Assert.assertEquals(amaltheans.getRestrictedBenefices().iterator().next().getId(), "cash");
//        Assert.assertEquals((int) amaltheans.getRestrictedBenefices().iterator().next().getMaxValue(), 8);
//    }
}
