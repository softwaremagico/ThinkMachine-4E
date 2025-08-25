package com.softwaremagico.tm.factory;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"capabilityFactory"})
public class CapabilityFactoryTest {
    public static final int TOTAL_ELEMENTS = 76;

    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(CapabilityFactory.getInstance().getElements().size(),
                TOTAL_ELEMENTS);
    }

    @Test
    public void checkSpecializations() throws InvalidXmlElementException {
        Assert.assertEquals(CapabilityFactory.getInstance().getElement("beastLore").getSpecializations().size(),
                2);
        Assert.assertFalse(CapabilityFactory.getInstance().getElement("speak").getSpecializations().isEmpty());
    }

    @Test
    public void checkSpecializationRestriction() throws InvalidXmlElementException {
        Assert.assertTrue(CapabilityFactory.getInstance().getElement("read").getSpecialization("urthtech")
                .getRestrictions().isRequiredCapability("read", "urthish"));
    }


    @Test
    public void anyCharacterDefinitionRestrictionAllowed() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("musters");
        characterPlayer.setCalling("techRedeemer");
        characterPlayer.getCalling().getSelectedCapabilityOptions().get(1).getSelections()
                .add(new Selection("thinkMachines"));
        Assert.assertFalse(CapabilityFactory.getInstance().getElement("artificialIntelligence").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void anyCharacterDefinitionRestrictionRestricted() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("spy");
        Assert.assertTrue(CapabilityFactory.getInstance().getElement("artificialIntelligence").getRestrictions().isRestricted(characterPlayer));
    }
}
