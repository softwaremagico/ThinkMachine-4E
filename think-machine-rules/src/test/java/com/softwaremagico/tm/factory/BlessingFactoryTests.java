package com.softwaremagico.tm.factory;

import com.softwaremagico.tm.character.blessings.BlessingFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"blessingFactory"})
public class BlessingFactoryTests {
    private static final String LANGUAGE = "es";

    private static final int DEFINED_BLESSINGS = 1;
    private static final int DEFINED_BONUS_MISSING_EYE = 2;

    @Test
    public void checkTotalElements() throws IOException {
        Assert.assertEquals(BlessingFactory.getInstance().getElements().size(),
                DEFINED_BLESSINGS);
    }

    @Test
    public void multiplesBonifications() {
        Assert.assertEquals(DEFINED_BONUS_MISSING_EYE,
                BlessingFactory.getInstance().getElement("bold").getBonifications().size());
    }
}
