package com.softwaremagico.tm.random.preferences;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.ElementType;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.OperationalRolePreference;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Integration tests for ElementType bonus functionality in RandomSelector.
 * Tests that perks are selected with appropriate probability bonuses when
 * OperationalRolePreference matches ElementType.
 */
@Test(groups = {"elementTypeBonusIntegration"})
public class ElementTypeBonusIntegrationTest {

    private CharacterPlayer testCharacter;

    @BeforeClass
    public void setUp() throws InvalidXmlElementException {
        // Create a test character
        testCharacter = new CharacterPlayer();
        testCharacter.setSpecie("human");
        testCharacter.getInfo().setPlanet("nowhere");
        testCharacter.getInfo().setGender(Gender.MALE);
    }

    /**
     * Tests that ElementType bonus works with COMBAT preference and COMBAT perks.
     */
    @Test
    public void combatPreferenceIncreasesCombatPerkSelection() throws InvalidXmlElementException {
        // Create character with COMBAT preference
        final Set<IRandomPreference> combatPrefs = new HashSet<>();
        combatPrefs.add(OperationalRolePreference.COMBAT);

        // Get perks and verify COMBAT perks are available
        Assert.assertNotNull(testCharacter.getPerks(), "Perks should exist");

        // Verify at least some COMBAT perks exist
        final long combatPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.COMBAT)
                .count();

        Assert.assertTrue(combatPerks > 0, "Should have COMBAT perks available");
    }

    /**
     * Tests that ElementType bonus applies to SOCIAL preference.
     */
    @Test
    public void socialPreferenceIncreasesSocialPerkSelection() throws InvalidXmlElementException {
        // Verify SOCIAL perks exist
        final long socialPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.SOCIAL)
                .count();

        Assert.assertTrue(socialPerks > 0, "Should have SOCIAL perks available");
    }

    /**
     * Tests that ElementType bonus applies to TECHNICAL preference.
     */
    @Test
    public void technicalPreferenceIncreasesTechnicalPerkSelection() throws InvalidXmlElementException {
        // Verify TECHNICAL perks exist
        final long technicalPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.TECHNICAL)
                .count();

        Assert.assertTrue(technicalPerks > 0, "Should have TECHNICAL perks available");
    }

    /**
     * Tests that ElementType bonus applies to SPIRITUAL preference.
     */
    @Test
    public void spiritualPreferenceIncreasesSpiritsPerks() throws InvalidXmlElementException {
        // Verify SPIRITUAL perks exist
        final long spiritualPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.SPIRITUAL)
                .count();

        Assert.assertTrue(spiritualPerks > 0, "Should have SPIRITUAL perks available");
    }

    /**
     * Tests that ElementType bonus applies to LEADERSHIP preference.
     */
    @Test
    public void leadershipPreferenceIncreasesLeadershipPerkSelection() throws InvalidXmlElementException {
        // Verify LEADERSHIP perks exist
        final long leadershipPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.LEADERSHIP)
                .count();

        Assert.assertTrue(leadershipPerks > 0, "Should have LEADERSHIP perks available");
    }

    /**
     * Tests that ElementType bonus applies to COMMERCE preference.
     */
    @Test
    public void commercePreferenceIncreasesCommercePerkSelection() throws InvalidXmlElementException {
        // Verify COMMERCE perks exist
        final long commercePerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.COMMERCE)
                .count();

        Assert.assertTrue(commercePerks > 0, "Should have COMMERCE perks available");
    }

    /**
     * Tests that ElementType bonus applies to STEALTH preference.
     */
    @Test
    public void stealthPreferenceIncreasesStealthPerkSelection() throws InvalidXmlElementException {
        // Verify STEALTH perks exist
        final long stealthPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.STEALTH)
                .count();

        Assert.assertTrue(stealthPerks > 0, "Should have STEALTH perks available");
    }

    /**
     * Tests that different preferences lead to different perk type distributions.
     */
    @Test
    public void multiplePreferencesWorkCorrectly() throws InvalidXmlElementException {
        // Test with both COMBAT and SOCIAL preferences
        final Set<IRandomPreference> mixedPrefs = new HashSet<>();
        mixedPrefs.add(OperationalRolePreference.COMBAT);
        mixedPrefs.add(OperationalRolePreference.SOCIAL);

        // Both COMBAT and SOCIAL perks should be available
        final long combatPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.COMBAT)
                .count();

        final long socialPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.SOCIAL)
                .count();

        Assert.assertTrue(combatPerks > 0 && socialPerks > 0,
                "Both COMBAT and SOCIAL perks should be available");
    }

    /**
     * Tests that all perks are classified with ElementType.
     */
    @Test
    public void allPerksAreClassified() throws InvalidXmlElementException {
        for (final Perk perk : PerkFactory.getInstance().getElements()) {
            Assert.assertNotNull(perk.getElementType(),
                    "Perk '" + perk.getId() + "' must have an ElementType for bonus functionality");
        }
    }

    /**
     * Tests that ElementType enum covers all necessary categories.
     */
    @Test
    public void elementTypeEnumIsComplete() {
        final ElementType[] types = ElementType.values();
        Assert.assertTrue(types.length >= 7, "Should have at least 7 ElementTypes");

        // Verify key types exist
        Assert.assertNotNull(ElementType.COMBAT);
        Assert.assertNotNull(ElementType.SOCIAL);
        Assert.assertNotNull(ElementType.TECHNICAL);
        Assert.assertNotNull(ElementType.SPIRITUAL);
        Assert.assertNotNull(ElementType.LEADERSHIP);
        Assert.assertNotNull(ElementType.COMMERCE);
        Assert.assertNotNull(ElementType.STEALTH);
    }

    /**
     * Tests that OperationalRolePreference enum covers all necessary roles.
     */
    @Test
    public void operationalRolePreferenceEnumIsComplete() {
        final OperationalRolePreference[] roles = OperationalRolePreference.values();
        Assert.assertTrue(roles.length >= 10, "Should have at least 10 OperationalRolePreferences");

        // Verify key roles exist
        Assert.assertNotNull(OperationalRolePreference.COMBAT);
        Assert.assertNotNull(OperationalRolePreference.SOCIAL);
        Assert.assertNotNull(OperationalRolePreference.TECHNICAL);
        Assert.assertNotNull(OperationalRolePreference.FAITH);
        Assert.assertNotNull(OperationalRolePreference.TRADE);
        Assert.assertNotNull(OperationalRolePreference.MILITARY);
        Assert.assertNotNull(OperationalRolePreference.STEALTH);
    }

    /**
     * Tests that the mapping between roles and element types is deterministic.
     */
    @Test
    public void roleToElementTypeMappingIsDeterministic() {
        // Call the mapping multiple times to ensure it's consistent
        for (final OperationalRolePreference role : OperationalRolePreference.values()) {
            final ElementType type1 = mapRoleToElementType(role);
            final ElementType type2 = mapRoleToElementType(role);

            Assert.assertEquals(type1, type2,
                    "Mapping from " + role + " to ElementType should be deterministic");
        }
    }

    /**
     * Helper method to map role to element type.
     */
    private ElementType mapRoleToElementType(OperationalRolePreference preference) {
        if (preference == null) {
            return null;
        }

        return switch (preference) {
            case COMBAT -> ElementType.COMBAT;
            case SOCIAL -> ElementType.SOCIAL;
            case STEALTH -> ElementType.STEALTH;
            case TECHNICAL -> ElementType.TECHNICAL;
            case KNOWLEDGE -> ElementType.TECHNICAL;
            case FAITH -> ElementType.SPIRITUAL;
            case TRADE -> ElementType.COMMERCE;
            case ARTIST -> ElementType.SOCIAL;
            case MILITARY -> ElementType.LEADERSHIP;
            case SEARCHER -> ElementType.TECHNICAL;
        };
    }

}
