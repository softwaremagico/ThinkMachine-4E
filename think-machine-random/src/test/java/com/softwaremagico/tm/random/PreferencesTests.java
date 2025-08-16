package com.softwaremagico.tm.random;

import com.softwaremagico.tm.character.specie.SpecieFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"preferences"})
public class PreferencesTests {

    @Test
    public void checkObunPreferences() {
        Assert.assertEquals(SpecieFactory.getInstance().getElement("obun").getRandomDefinition().getRecommendedPreferences().size(), 2);
    }
}
