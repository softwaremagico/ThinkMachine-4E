package com.softwaremagico.tm.factory;

import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.occultism.TheurgyComponentFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"occultismFactory"})
public class OccultismFactoryTests {
    private static final int DEFINED_PSI_PATHS = 12;
    private static final int DEFINED_THEURGY_PATHS = 12;
    private static final int DEFINED_RANGES = 5;
    private static final int DEFINED_DURATIONS = 7;
    private static final int DEFINED_THEURGY_COMPONENTS = 3;
    private static final int OCCULTISM_TYPES = 2;

    @Test
    public void readPaths() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismPathFactory.getInstance().getElements().size(),
                DEFINED_PSI_PATHS + DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readPsiPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getPsiPaths().size(), DEFINED_PSI_PATHS);
    }

    @Test
    public void readTheurgyPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getTheurgyPaths()
                .size(), DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readRanges() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismRangeFactory.getInstance().getElements().size(), DEFINED_RANGES);
    }

    @Test
    public void readDurations() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismDurationFactory.getInstance().getElements()
                .size(), DEFINED_DURATIONS);
    }

    @Test
    public void readTheurgyComponents() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_THEURGY_COMPONENTS,
                TheurgyComponentFactory.getInstance().getElements().size());
    }

    @Test
    public void getOccultismTypes() throws InvalidXmlElementException {
        Assert.assertEquals(OCCULTISM_TYPES, OccultismTypeFactory.getInstance().getElements().size());
    }

    @Test
    public void getClassifications() throws InvalidXmlElementException {
        Assert.assertEquals(ElementClassification.ENHANCEMENT,
                OccultismPathFactory.getInstance().getElement("sixthSense").getClassification());
        Assert.assertEquals(ElementClassification.COMBAT,
                OccultismPathFactory.getInstance().getElement("soma").getClassification());
        Assert.assertEquals(ElementClassification.OTHERS,
                OccultismPathFactory.getInstance().getElement("sympathy").getClassification());
        Assert.assertEquals(ElementClassification.ALTERATION,
                OccultismPathFactory.getInstance().getElement("templeAvestiRituals")
                        .getClassification());
    }


    @Test
    public void checkNonOfficialPaths() throws InvalidXmlElementException {
        Assert.assertFalse(OccultismPathFactory.getInstance()
                .getElement("bedlam").getOccultismPowers()
                .get("prana").isOfficial());
    }
}
