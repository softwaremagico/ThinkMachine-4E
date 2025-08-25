package com.softwaremagico.tm.rules;

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

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.skills.SkillBonusOption;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;
import com.softwaremagico.tm.exceptions.MaxInitialValueExceededException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = "characterRules")
public class CharacterRulesTests {

    @Test(expectedExceptions = InvalidSpecieException.class)
    public void invalidVoroxCharacteristics() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("vorox");
        characterPlayer.setPrimaryCharacteristic("dexterity");
        characterPlayer.setSecondaryCharacteristic("wits");
        characterPlayer.getSpecie().validate();
    }


    @Test(expectedExceptions = InvalidUpbringingException.class)
    public void invalidUpbringing() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("ukar");
        characterPlayer.setUpbringing("noble");
    }

    public void validUpbringing() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
    }

    @Test(expectedExceptions = InvalidFactionException.class)
    public void invalidFaction() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("brotherBattle");
    }

    public void validFaction() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
    }

    @Test(expectedExceptions = InvalidCallingException.class)
    public void invalidCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("commander");
    }

    @Test
    public void validCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.setCalling("commander");
    }

    @Test(expectedExceptions = MaxInitialValueExceededException.class)
    public void characterCreationCharacteristicsMaxValueRebased() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");

        characterPlayer.setUpbringing("noble");
        final Upbringing upbringing = UpbringingFactory.getInstance().getElement("noble");
        for (int i = 0; i < upbringing.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < upbringing.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(upbringing.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }

        characterPlayer.setFaction("alMalik");
        final Faction faction = FactionFactory.getInstance().getElement("alMalik");
        for (int i = 0; i < faction.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < faction.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(faction.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }


        characterPlayer.setCalling("commander");
        final Calling calling = CallingFactory.getInstance().getElement("commander");
        for (int i = 0; i < calling.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < calling.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(calling.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        characterPlayer.getCharacteristicValue(CharacteristicName.PRESENCE.getId());
    }

    @Test
    public void checkHumanNobleDecadosCommander() throws MaxInitialValueExceededException {
        CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.STRENGTH), 3);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.DEXTERITY), 7);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.ENDURANCE), 4);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.WITS), 6);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PERCEPTION), 4);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.WILL), 6);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PRESENCE), 8);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.INTUITION), 3);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.FAITH), 4);
    }

    @Test
    public void materialAwardsOptions() {
        final Faction faction = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertEquals(faction.getMaterialAwards().size(), 1);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getTotalOptions(), 1);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getOptions().size(), 1);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getOptions().get(0).getId(), "estheticOrb");
    }

    @Test
    public void materialAwardsOptionsSpecialized() {
        final Faction faction = FactionFactory.getInstance().getElement("hawkwood");
        Assert.assertEquals(faction.getMaterialAwards().size(), 1);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getTotalOptions(), 1);
        Assert.assertTrue(faction.getMaterialAwards().get(0).getOptions().size() > 8);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getOptions().get(0).getId(), "glankesh");
    }

    @Test
    public void materialAwardsOptionsSpecialized2() {
        final Faction faction = FactionFactory.getInstance().getElement("vuldrok");
        Assert.assertEquals(faction.getMaterialAwards().size(), 1);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getTotalOptions(), 1);
        Assert.assertTrue(faction.getMaterialAwards().get(0).getOptions().size() > 25);
        Assert.assertEquals(faction.getMaterialAwards().get(0).getOptions()
                .get(faction.getMaterialAwards().get(0).getOptions().size() - 1)
                .getType(), "handheldShield");
    }

    @Test
    public void speciePerksAsCallingPerks() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("vorox");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("far");
        characterPlayer.setCalling("spy");

        Assert.assertEquals(characterPlayer.getCalling().getNotRepeatedPerksOptions().get(0).getOptions().size(),
                CallingFactory.getInstance().getElement("spy").getPerksOptions().get(0).getOptions().size()
                        + SpecieFactory.getInstance().getElement("vorox").getPerks().size());
    }

    @Test
    public void materialAwardsKnightlyOrder() {
        final Calling calling = CallingFactory.getInstance().getElement("knightlyOrder");
        Assert.assertEquals(calling.getMaterialAwards().size(), 1);
        Assert.assertEquals(calling.getMaterialAwards().get(0).getOptions().size(), 10);
    }

    @Test
    public void callingSpecializedCapability() {
        final Calling calling = CallingFactory.getInstance().getElement("questingKnight");
        Assert.assertEquals(calling.getCapabilityOptions().size(), 2);
        Assert.assertEquals(calling.getCapabilityOptions().get(1).getOptions().size(), 2);
        Assert.assertEquals(calling.getCapabilityOptions().get(1).getOptions().get(0).getSelectedSpecialization().getId(), "barbarianWorlds");
    }

    @Test
    public void callingQuestKnightMaterialAwards() {
        final Calling calling = CallingFactory.getInstance().getElement("questingKnight");
        Assert.assertEquals(calling.getMaterialAwards().size(), 2);
        Assert.assertEquals(calling.getMaterialAwards().get(0).getOptions().size(), 6);
        Assert.assertTrue(calling.getMaterialAwards().get(1).getOptions().size() > 1);
    }

    @Test
    public void callingBrotherBattlerMaterialAwards() {
        final Calling calling = CallingFactory.getInstance().getElement("brotherBattle");
        Assert.assertEquals(calling.getMaterialAwards().size(), 1);
        Assert.assertTrue(calling.getMaterialAwards().get(0).getOptions().size() > 150);
    }

    @Test
    public void callingInquisitorMaterialAwards() {
        final Calling calling = CallingFactory.getInstance().getElement("inquisitor");
        Assert.assertEquals(calling.getMaterialAwards().size(), 1);
        Assert.assertTrue(calling.getMaterialAwards().get(0).getOptions().get(0).getExtras().contains("flameproof"));
    }


    @Test
    public void checkRaisedInSpace() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.getInfo().setNames("Raised");
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.getUpbringing().setRaisedInSpace(true);

        final CapabilityOption shipboardOperations = new CapabilityOption(CapabilityFactory.getInstance().getElement("shipboardOperations"));
        for (CapabilityOptions capabilityOptions : characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions()) {
            Assert.assertTrue(capabilityOptions.getOptions().contains(shipboardOperations));
        }

        final CapabilityOption thinkMachines = new CapabilityOption(CapabilityFactory.getInstance().getElement("thinkMachines"));
        for (CapabilityOptions capabilityOptions : characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions()) {
            Assert.assertTrue(capabilityOptions.getOptions().contains(thinkMachines));
        }

        final SkillBonusOption interfaceSkill = new SkillBonusOption(SkillFactory.getInstance().getElement("interface"));
        for (SkillBonusOptions skillBonusOptions : characterPlayer.getUpbringing().getSkillOptions()) {
            Assert.assertTrue(skillBonusOptions.getOptions().contains(interfaceSkill));
        }

        final SkillBonusOption techRedemptionSkill = new SkillBonusOption(SkillFactory.getInstance().getElement("techRedemption"));
        for (SkillBonusOptions skillBonusOptions : characterPlayer.getUpbringing().getSkillOptions()) {
            Assert.assertTrue(skillBonusOptions.getOptions().contains(techRedemptionSkill));
        }

        CharacterPlayer characterPlayer2 = new CharacterPlayer();
        characterPlayer2.getInfo().setNames("NotRaised");
        characterPlayer2.setSpecie("human");
        characterPlayer2.setUpbringing("noble");
        characterPlayer2.getUpbringing().setRaisedInSpace(false);

        for (CapabilityOptions capabilityOptions : characterPlayer2.getUpbringing().getNotRepeatedCapabilityOptions()) {
            Assert.assertFalse(capabilityOptions.getOptions().contains(shipboardOperations));
        }

        for (CapabilityOptions capabilityOptions : characterPlayer2.getUpbringing().getNotRepeatedCapabilityOptions()) {
            Assert.assertFalse(capabilityOptions.getOptions().contains(thinkMachines));
        }

        for (SkillBonusOptions skillBonusOptions : characterPlayer2.getUpbringing().getSkillOptions()) {
            Assert.assertFalse(skillBonusOptions.getOptions().contains(interfaceSkill));
        }

        for (SkillBonusOptions skillBonusOptions : characterPlayer2.getUpbringing().getSkillOptions()) {
            Assert.assertFalse(skillBonusOptions.getOptions().contains(techRedemptionSkill));
        }
    }

    public void avoidingCallingDuplicateCapability() {
        CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();
        Assert.assertTrue(characterPlayer.getCalling().getCapabilityOptions().get(0).getOptions().contains(
                new CapabilityOption(CapabilityFactory.getInstance().getElement("militaryWeapons"))));
        //Military weapons is not an option on commander calling
        Assert.assertTrue(characterPlayer.getCalling().getNotRepeatedCapabilityOptions().get(0).getOptions().size() > 60);
        Assert.assertFalse(characterPlayer.getCalling().getNotRepeatedCapabilityOptions().get(0).getOptions().contains(
                new CapabilityOption(CapabilityFactory.getInstance().getElement("militaryWeapons"))));
    }
}
