package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.random.character.callings.RandomCalling;
import com.softwaremagico.tm.random.character.names.RandomName;
import com.softwaremagico.tm.random.character.names.RandomSurname;
import com.softwaremagico.tm.random.character.planets.RandomPlanet;
import com.softwaremagico.tm.random.character.upbringings.RandomUpbringing;
import com.softwaremagico.tm.random.definition.ProbabilityMultiplier;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomCharacteristics;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"random"})
public class RandomTests {

    @Test
    public void readRandomPreferences() {
        final Specie human = SpecieFactory.getInstance().getElement("human");
        Assert.assertEquals(human.getRandomDefinition().getProbabilityMultiplier(), ProbabilityMultiplier.DOMINANT);
    }

    @Test
    public void randomNobleNames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.getInfo().setPlanet("sutek");
        characterPlayer.getInfo().setGender(Gender.MALE);
        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
    }

    @Test
    public void randomNonNobleNames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.getInfo().setPlanet("istakhr");
        characterPlayer.getInfo().setGender(Gender.MALE);
        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
        final RandomSurname randomSurname = new RandomSurname(characterPlayer, null);
        randomSurname.assign();
    }

    @Test
    public void randomObunNames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vagabonds");
        characterPlayer.getInfo().setPlanet("sutek");
        characterPlayer.getInfo().setGender(Gender.MALE);
        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
        Assert.assertTrue(SpecieFactory.getInstance().getAllNames().contains(characterPlayer.getInfo().getNames().get(0)));
    }

    @Test
    public void randomSurnames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.getInfo().setPlanet("sutek");
        final RandomSurname randomSurname = new RandomSurname(characterPlayer, null);
        randomSurname.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
    }

    @Test
    public void randomObunSurnames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vagabonds");
        characterPlayer.getInfo().setPlanet("sutek");
        characterPlayer.getInfo().setGender(Gender.MALE);
        final RandomSurname randomName = new RandomSurname(characterPlayer, null);
        randomName.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
        Assert.assertTrue(SpecieFactory.getInstance().getAllSurnames().contains(characterPlayer.getInfo().getSurname()));
    }


    @Test
    public void randomPlanet() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        final RandomPlanet randomPlanet = new RandomPlanet(characterPlayer, null);
        randomPlanet.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
    }

    @Test
    public void randomPrimaryCharacteristics() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("vorox");
        final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, null);
        randomCharacteristics.assign();
        Assert.assertTrue(SpecieFactory.getInstance().getElement("vorox").getPrimaryCharacteristics().contains(characterPlayer.getPrimaryCharacteristic())
                || SpecieFactory.getInstance().getElement("vorox").getPrimaryCharacteristics().contains(characterPlayer.getSecondaryCharacteristic()));
    }

    @Test
    public void randomUpbringingOptions() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");

        final RandomUpbringing randomUpbringing = new RandomUpbringing(characterPlayer, null);
        randomUpbringing.assign();
        randomUpbringing.complete();

        Assert.assertEquals(characterPlayer.getUpbringing().getSelectedCharacteristicOptions().size(), 4);
        Assert.assertEquals(CharacteristicsDefinitionFactory.getInstance().getElement(characterPlayer.getUpbringing().getSelectedCharacteristicOptions().get(0).getSelections().iterator().next().getId()).getType(), CharacteristicType.BODY);
        Assert.assertEquals(CharacteristicsDefinitionFactory.getInstance().getElement(characterPlayer.getUpbringing().getSelectedCharacteristicOptions().get(1).getSelections().iterator().next().getId()).getType(), CharacteristicType.SPIRIT);
    }

    @Test
    public void randomFavouredCallingHasBetterWeight() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("societyOfStPaulus");

        final RandomCalling randomCalling = new RandomCalling(characterPlayer, null);
        randomCalling.updateWeights();

        Assert.assertEquals(randomCalling.getAssignedWeight(CallingFactory.getInstance().getElement("explorer")), Integer.valueOf(21000));
        Assert.assertEquals(randomCalling.getAssignedWeight(CallingFactory.getInstance().getElement("spy")), Integer.valueOf(1000));
    }
}
