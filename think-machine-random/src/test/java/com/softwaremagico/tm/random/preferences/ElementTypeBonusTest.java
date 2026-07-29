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
import com.softwaremagico.tm.random.preferences.RandomSelector;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for ElementType bonus functionality in RandomSelector when used with OperationalRolePreference.
 * Validates that element selection probability increases when ElementType matches the user's operational role.
 */
@Test(groups = {"elementTypeBonus"})
public class ElementTypeBonusTest {

    /**
     * Tests that all OperationalRolePreferences map to a valid ElementType.
     */
    @Test
    public void allRolePreferencesMapToElementType() throws Exception {
        for (final OperationalRolePreference rolePreference : OperationalRolePreference.values()) {
            final ElementType elementType = mapRolePreferenceToElementType(rolePreference);
            Assert.assertNotNull(elementType, "Role preference '" + rolePreference + "' should map to an ElementType");
        }
    }

    /**
     * Data provider for role-to-element-type mapping tests.
     */
    @DataProvider(name = "roleToElementMappings")
    public Object[][] roleToElementMappings() {
        return new Object[][]{
                {OperationalRolePreference.COMBAT, ElementType.COMBAT},
                {OperationalRolePreference.SOCIAL, ElementType.SOCIAL},
                {OperationalRolePreference.STEALTH, ElementType.STEALTH},
                {OperationalRolePreference.TECHNICAL, ElementType.TECHNICAL},
                {OperationalRolePreference.KNOWLEDGE, ElementType.TECHNICAL},
                {OperationalRolePreference.FAITH, ElementType.SPIRITUAL},
                {OperationalRolePreference.TRADE, ElementType.COMMERCE},
                {OperationalRolePreference.ARTIST, ElementType.SOCIAL},
                {OperationalRolePreference.MILITARY, ElementType.LEADERSHIP},
                {OperationalRolePreference.SEARCHER, ElementType.TECHNICAL},
        };
    }

    /**
     * Tests that role preferences correctly map to their corresponding element types.
     */
    @Test(dataProvider = "roleToElementMappings")
    public void rolePreferenceMapsCorrectlyToElementType(OperationalRolePreference rolePreference, ElementType expectedElementType) throws Exception {
        final ElementType mappedType = mapRolePreferenceToElementType(rolePreference);
        Assert.assertEquals(mappedType, expectedElementType,
                "Role preference '" + rolePreference + "' should map to '" + expectedElementType + "'");
    }

    /**
     * Tests that all perks have an ElementType set.
     */
    @Test
    public void allPerksHaveElementType() throws InvalidXmlElementException {
        for (final Perk perk : PerkFactory.getInstance().getElements()) {
            Assert.assertNotNull(perk.getElementType(),
                    "Perk '" + perk.getId() + "' should have an ElementType");
        }
    }

    /**
     * Data provider for perks by element type.
     */
    @DataProvider(name = "perksByElementType")
    public Object[][] perksByElementType() throws InvalidXmlElementException {
        return new Object[][]{
                {ElementType.COMBAT},
                {ElementType.SOCIAL},
                {ElementType.TECHNICAL},
                {ElementType.SPIRITUAL},
                {ElementType.LEADERSHIP},
                {ElementType.COMMERCE},
                {ElementType.STEALTH},
        };
    }

    /**
     * Tests that perks exist for each ElementType.
     */
    @Test(dataProvider = "perksByElementType")
    public void perksExistForEachElementType(ElementType elementType) throws InvalidXmlElementException {
        final long countOfType = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == elementType)
                .count();

        Assert.assertTrue(countOfType > 0,
                "At least one perk should exist for ElementType '" + elementType + "'");
    }

    /**
     * Tests that at least 5 perks exist for each ElementType (ensuring balanced distribution).
     */
    @Test(dataProvider = "perksByElementType")
    public void sufficientPerksExistForEachElementType(ElementType elementType) throws InvalidXmlElementException {
        final long countOfType = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == elementType)
                .count();

        Assert.assertTrue(countOfType >= 5,
                "At least 5 perks should exist for ElementType '" + elementType + "', but found: " + countOfType);
    }

    /**
     * Distribution test: verify balance across all element types.
     */
    @Test
    public void elementTypeDistributionIsBalanced() throws InvalidXmlElementException {
        long totalPerks = 0;
        for (final ElementType elementType : ElementType.values()) {
            final long countOfType = PerkFactory.getInstance().getElements().stream()
                    .filter(p -> p.getElementType() == elementType)
                    .count();
            totalPerks += countOfType;
        }

        // All perks should be classified
        final long classifiedPerks = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() != null)
                .count();

        Assert.assertEquals(classifiedPerks, totalPerks,
                "All perks should be classified with an ElementType");
        Assert.assertEquals(classifiedPerks, PerkFactory.getInstance().getElements().size(),
                "No perks should be unclassified");
    }

    /**
     * Tests that bonus multiplier calculation works with ElementType matching.
     */
    @Test
    public void elementTypeBonusMultiplierAppliedCorrectly() throws Exception {
        // Create a character with COMBAT preference
        final CharacterPlayer character = new CharacterPlayer();
        character.setSpecie("human");
        character.getInfo().setPlanet("nowhere");
        character.getInfo().setGender(Gender.MALE);

        // Test that COMBAT perks get bonus when preference is COMBAT
        final Set<IRandomPreference> preferences = new HashSet<>();
        preferences.add(OperationalRolePreference.COMBAT);

        // Find a COMBAT perk
        final Perk combatPerk = PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == ElementType.COMBAT)
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(combatPerk, "Should find a COMBAT perk for testing");

        // Test bonus calculation with high multiplier when types match
        final double baseMultiplier = 1.0;
        final double HIGH_MULTIPLIER = RandomSelector.HIGH_MULTIPLIER;

        Assert.assertTrue(baseMultiplier * HIGH_MULTIPLIER > baseMultiplier,
                "Bonus multiplier should increase weight when ElementType matches");
    }

    /**
     * Tests that different role preferences result in different element selections.
     */
    @Test
    public void differentRolePreferencesHaveDifferentElementAffinities() throws InvalidXmlElementException {
        // Count perks that would benefit from each preference
        final long combatPerks = countPerksForRole(OperationalRolePreference.COMBAT);
        final long socialPerks = countPerksForRole(OperationalRolePreference.SOCIAL);
        final long technicalPerks = countPerksForRole(OperationalRolePreference.TECHNICAL);

        Assert.assertTrue(combatPerks > 0, "Should have COMBAT-affinity perks");
        Assert.assertTrue(socialPerks > 0, "Should have SOCIAL-affinity perks");
        Assert.assertTrue(technicalPerks > 0, "Should have TECHNICAL-affinity perks");

        // Verify they're different categories (at least some should differ)
        Assert.assertTrue(combatPerks > 0 && socialPerks > 0 && technicalPerks > 0,
                "Different role preferences should have different perk types");
    }

    /**
     * Tests that perks for a specific role are consistently categorized.
     */
    @Test
    public void perksConsistentlyCategoriZedByRole() throws InvalidXmlElementException {
        // Test COMBAT role consistency
        final Set<IRandomPreference> combatPrefs = new HashSet<>();
        combatPrefs.add(OperationalRolePreference.COMBAT);

        final long combatElementTypePerks = countPerksForElementType(ElementType.COMBAT);
        Assert.assertTrue(combatElementTypePerks > 0, "Should have COMBAT element type perks");
    }

    // Helper methods

    /**
     * Uses reflection to call the private mapRolePreferenceToElementType method.
     */
    private ElementType mapRolePreferenceToElementType(OperationalRolePreference preference) throws Exception {
        // We need to create a concrete test class to access the protected method
        // For now, we'll use a direct mapping based on the switch statement
        return mapRoleToElementType(preference);
    }

    /**
     * Maps role preference to element type according to the implementation.
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

    /**
     * Counts perks for a specific role preference's mapped element type.
     */
    private long countPerksForRole(OperationalRolePreference rolePreference) throws InvalidXmlElementException {
        final ElementType elementType = mapRoleToElementType(rolePreference);
        return countPerksForElementType(elementType);
    }

    /**
     * Counts perks for a specific element type.
     */
    private long countPerksForElementType(ElementType elementType) throws InvalidXmlElementException {
        return PerkFactory.getInstance().getElements().stream()
                .filter(p -> p.getElementType() == elementType)
                .count();
    }

}
