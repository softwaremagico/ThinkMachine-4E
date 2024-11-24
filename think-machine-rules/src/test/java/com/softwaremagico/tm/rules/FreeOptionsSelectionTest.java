package com.softwaremagico.tm.rules;

import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "freeOptionsSelectionTest")
public class FreeOptionsSelectionTest {

    @Test
    public void spyHasFreeSkillsOptions() {
        Assert.assertEquals(CallingFactory.getInstance().getElement("merchantSpy").getSkillOptions().get(4).getOptions().size(),
                SkillFactory.getInstance().getElements().size());
    }

    @Test
    public void spyHasFreeMaterialAwardsOptions() {
        Assert.assertEquals(CallingFactory.getInstance().getElement("merchantSpy").getMaterialAwards().get(0).getOptions().size(),
                ItemFactory.getInstance().getElements().size() + WeaponFactory.getInstance().getElements().size() +
                        ArmorFactory.getInstance().getElements().size() + ShieldFactory.getInstance().getElements().size() +
                        HandheldShieldFactory.getInstance().getElements().size() + ThinkMachineFactory.getInstance().getElements().size());
    }
}
