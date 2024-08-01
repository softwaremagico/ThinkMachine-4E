package com.softwaremagico.tm.factory;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinitionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"skillFactory"})
public class SkillFactoryTests {
    private static final int NATURAL_SKILLS = 9;
    private static final int LEARNED_SKILLS = 48;

    @Test
    public void readSkills() throws InvalidXmlElementException {
        Assert.assertEquals(SkillDefinitionFactory.getInstance().getElements().size(), NATURAL_SKILLS + LEARNED_SKILLS);
    }

    @Test
    public void readSkillSpecialization() throws InvalidXmlElementException {
        Assert.assertEquals(AvailableSkillsFactory.getInstance().getElement("lore", "thinkMachineLore").getCompleteName().getSpanish(),
                "Saber [Máquina Pensante]");
    }

    @Test
    public void availableSkillNoOfficial() throws InvalidXmlElementException {
        Assert.assertFalse(AvailableSkillsFactory.getInstance().getElement("fly").isOfficial());
    }

    @Test
    public void availableSkillSpecializationNoOfficial() throws InvalidXmlElementException {
        Assert.assertFalse(AvailableSkillsFactory.getInstance().getElement("lore", "godLore").isOfficial());
    }
}
