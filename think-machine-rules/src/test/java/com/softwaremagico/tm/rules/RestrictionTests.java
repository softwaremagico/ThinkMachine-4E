package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.restrictions.RestrictionMode;
import com.softwaremagico.tm.restrictions.Restrictions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Test(groups = "restrictions")
public class RestrictionTests extends RulesTest {

    @Test
    public void restrictedWeaponByAgora() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hazat");
        Assert.assertTrue(WeaponFactory.getInstance().getElement("drexelFirehunter").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void restrictedWeaponByCapability() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        Assert.assertTrue(WeaponFactory.getInstance().getElement("mace").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void restrictedHandheldShieldByCapability() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        Assert.assertTrue(HandheldShieldFactory.getInstance().getElement("bodyShieldSteel").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void restrictedWarArmorByCapability() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        Assert.assertTrue(ArmorFactory.getInstance().getElement("ceramsteelExoframe").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void anyModeAllowsAnyMatchingRestriction() {
        final Restrictions restrictions = new Restrictions();
        restrictions.setRestrictedToSpecies(Set.of("human"));
        restrictions.setRestrictedToFactions(Set.of("alMalik"));

        Assert.assertFalse(restrictions.isRestricted(createValidCharacter()));
    }

    @Test
    public void allModeRequiresEveryRestriction() {
        final Restrictions restrictions = characterDefinitionRestrictions(RestrictionMode.ALL);

        Assert.assertFalse(restrictions.isRestricted(createValidCharacter()));

        final CharacterPlayer characterPlayer = createValidCharacter();
        characterPlayer.setFaction("alMalik");
        Assert.assertTrue(restrictions.isRestricted(characterPlayer));
    }

    @Test
    public void anyFromGroupModeRequiresAMatchFromEveryGroup() {
        final Restrictions restrictions = characterDefinitionRestrictions(RestrictionMode.ANY_FROM_GROUP);

        Assert.assertFalse(restrictions.isRestricted(createValidCharacter()));

        final CharacterPlayer characterPlayer = createValidCharacter();
        characterPlayer.setCalling("commander");
        Assert.assertTrue(restrictions.isRestricted(characterPlayer));
    }

    @Test
    public void anyCharacterDefinitionModeAllowsAnyMatchingDefinition() {
        final Restrictions restrictions = new Restrictions();
        restrictions.setMode(RestrictionMode.ANY_CHARACTER_DEFINITION);
        restrictions.setRestrictedToSpecies(Set.of("obun"));
        restrictions.setRestrictedToUpbringing(Set.of("merchant"));
        restrictions.setRestrictedToFactions(Set.of("alMalik"));
        restrictions.setRestrictedToCallings(Set.of("commander"));

        Assert.assertTrue(restrictions.isRestricted(createValidCharacter()));

        final CharacterPlayer characterPlayer = createValidCharacter();
        characterPlayer.setCalling("commander");
        Assert.assertFalse(restrictions.isRestricted(characterPlayer));
    }

    @Test
    public void wiseOneRequiresPriestUpbringingAndEligibleFaction() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("gjarti");

        Assert.assertFalse(PerkFactory.getInstance().getElement("wiseOne").getRestrictions().isRestricted(characterPlayer));
    }

    private Restrictions characterDefinitionRestrictions(RestrictionMode mode) {
        final Restrictions restrictions = new Restrictions();
        restrictions.setMode(mode);
        restrictions.setRestrictedToSpecies(Set.of("human"));
        restrictions.setRestrictedToUpbringing(Set.of("noble"));
        restrictions.setRestrictedToFactions(Set.of("hawkwood"));
        restrictions.setRestrictedToCallings(Set.of("amateur"));
        return restrictions;
    }

    private CharacterPlayer createValidCharacter() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("amateur");
        return characterPlayer;
    }
}
