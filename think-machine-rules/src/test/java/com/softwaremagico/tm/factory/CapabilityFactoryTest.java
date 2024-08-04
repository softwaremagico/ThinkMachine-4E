package com.softwaremagico.tm.factory;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"capabilityFactory"})
public class CapabilityFactoryTest {
    private static final int TOTAL_ELEMENTS = 3;

    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(CapabilityFactory.getInstance().getElements().size(),
                TOTAL_ELEMENTS);
    }
}
