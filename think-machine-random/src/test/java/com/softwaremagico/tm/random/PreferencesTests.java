package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.callings.RandomCalling;
import com.softwaremagico.tm.random.character.factions.RandomFaction;
import com.softwaremagico.tm.random.character.upbringings.RandomUpbringing;
import com.softwaremagico.tm.random.definition.ProbabilityMultiplier;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.AlignmentPreference;
import com.softwaremagico.tm.random.preferences.AttackPreferences;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.preferences.TechPreference;
import com.softwaremagico.tm.random.profile.RandomPreferences;
import com.softwaremagico.tm.random.profile.RandomProfile;
import com.softwaremagico.tm.random.profile.RandomProfileFactory;
import com.softwaremagico.tm.random.step.RandomCharacteristicBonusOption;
import com.softwaremagico.tm.random.step.RandomCharacteristics;
import com.softwaremagico.tm.xml.XmlFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Test(groups = {"preferences"})
public class PreferencesTests {

	private Set<IRandomPreference> convert(IRandomPreference... preferences) {
		if (preferences != null) {
			final List<IRandomPreference> customPreferences = Arrays.asList(preferences);
			customPreferences.removeIf(Objects::isNull);
			return new HashSet<>(customPreferences);
		} else {
			return new HashSet<>();
		}
	}

	private RandomProfile createRecommendedProfile() {
		final RandomProfile profile = new RandomProfile();
		profile.setId("testProfile");
		profile.setRecommendedUpbringings(Set.of("merchant"));
		profile.setRecommendedFactions(Set.of("reeves", "musters"));
		profile.setRecommendedCallings(Set.of("trader"));
		return profile;
	}

	@Test
	public void checkObunPreferences() {
		Assert.assertEquals(
				SpecieFactory.getInstance().getElement("obun").getRandomDefinition().getRecommendedPreferences().size(),
				2);
	}

	@DataProvider(name = "profilePreferences")
	public Object[][] profilePreferences() {
		return new Object[][]{{"thug", "occupation"}, {"highTechnology", "specialization"},
				{"heavyWeapons", "specialization"}, {"command", "specialization"}};
	}

	@Test(dataProvider = "profilePreferences")
	public void loadProfilePreferences(String profileId, String expectedGroup) {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement(profileId);

		Assert.assertEquals(profile.getGroup(), expectedGroup);
	}

	@DataProvider(name = "npcProfiles")
	public Object[][] npcProfiles() {
		return new Object[][]{{"serf"}, {"cavalryRaider"}, {"tankDriver"}, {"medic"}, {"intermediateCommand"},
				{"cybernetic"}, {"freedomFighter"}, {"sailor"}, {"spaceFighterPilot"}, {"combatAircraftPilot"}};
	}

	@Test(dataProvider = "npcProfiles")
	public void loadNpcProfilePreferences(String profileId) {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement(profileId);

		Assert.assertNotNull(profile);
	}

	@DataProvider(name = "operationalRoleProfiles")
	public Object[][] operationalRoleProfiles() {
		return new Object[][]{{"combat", ElementType.COMBAT}, {"social", ElementType.SOCIAL},
				{"stealth", ElementType.STEALTH}, {"knowledge", ElementType.TECHNICAL},
				{"searcher", ElementType.TECHNICAL}, {"faith", ElementType.SPIRITUAL}, {"trade", ElementType.COMMERCE},
				{"technical", ElementType.TECHNICAL}, {"artist", ElementType.SOCIAL},
				{"military", ElementType.LEADERSHIP}};
	}

	@Test(dataProvider = "operationalRoleProfiles")
	public void loadOperationalRoleProfile(String profileId, ElementType expectedElementType) {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement(profileId);

		Assert.assertEquals(profile.getGroup(), "operationalRole");
		Assert.assertEquals(profile.getElementType(), expectedElementType);
	}

	@Test
	public void loadProfileElementPriorities() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("heavyWeapons");

		Assert.assertEquals(profile.getMandatorySkills(), Set.of("shoot"));
		Assert.assertEquals(profile.getSuggestedCapabilities(),
				Set.of("gunnery", "artillery", "slugGuns", "militaryWeapons", "combatArmor"));
	}

	@Test
	public void profilesAreKeptOutsideRandomPreferences() throws InvalidXmlElementException {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("heavyWeapons");
		final RandomPreferences preferences = new RandomPreferences(Set.of(AttackPreferences.RANGED), Set.of(profile));

		Assert.assertEquals(preferences, Set.of(AttackPreferences.RANGED));
		Assert.assertEquals(preferences.getProfiles(), Set.of(profile));
	}

	@Test
	public void loadDirectProfileRecommendations() throws Exception {
		final String xml = """
				<profiles>
				    <profile>
				        <id>xmlProfile</id>
				        <group>operationalRole</group>
				        <recommendedUpbringings>
				            <id>merchant</id>
				            <id>noble</id>
				        </recommendedUpbringings>
				        <recommendedFactions>
				            <faction>reeves</faction>
				            <faction>musters</faction>
				        </recommendedFactions>
				        <recommendedCallings>
				            <calling>trader</calling>
				            <calling>banker</calling>
				        </recommendedCallings>
				    </profile>
				</profiles>
				""";

		final List<RandomProfile> profiles = XmlFactory.getObjectMapper().readerForListOf(RandomProfile.class)
				.readValue(xml);
		final RandomProfile profile = profiles.get(0);

		Assert.assertEquals(profile.getRecommendedUpbringings(), Set.of("merchant", "noble"));
		Assert.assertEquals(profile.getRecommendedFactions(), Set.of("reeves", "musters"));
		Assert.assertEquals(profile.getRecommendedCallings(), Set.of("trader", "banker"));
	}

	@Test
	public void profileRecommendationIncreasesUpbringingWeight()
			throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		final Upbringing upbringing = UpbringingFactory.getInstance().getElement("merchant");
		final RandomProfile profile = this.createRecommendedProfile();
		final RandomUpbringing baseSelector = new RandomUpbringing(characterPlayer,
				new RandomPreferences(Set.of(), Set.of()));
		final RandomUpbringing selector = new RandomUpbringing(characterPlayer,
				new RandomPreferences(Set.of(), Set.of(profile)));

		Assert.assertEquals(selector.getElementWeight(upbringing),
				baseSelector.getElementWeight(upbringing) * RandomSelector.HIGH_MULTIPLIER);
	}

	@Test
	public void profileRecommendationIncreasesFactionWeight()
			throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("merchant");
		final Faction faction = FactionFactory.getInstance().getElement("reeves");
		final RandomProfile profile = this.createRecommendedProfile();
		final RandomFaction baseSelector = new RandomFaction(characterPlayer,
				new RandomPreferences(Set.of(), Set.of()));
		final RandomFaction selector = new RandomFaction(characterPlayer,
				new RandomPreferences(Set.of(), Set.of(profile)));

		Assert.assertEquals(selector.getElementWeight(faction),
				baseSelector.getElementWeight(faction) * RandomSelector.HIGH_MULTIPLIER);
	}

	@Test
	public void profileRecommendationIncreasesCallingWeight()
			throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("merchant");
		characterPlayer.setFaction("musters");
		final Calling calling = CallingFactory.getInstance().getElement("trader");
		final RandomProfile profile = this.createRecommendedProfile();
		final RandomCalling baseSelector = new RandomCalling(characterPlayer,
				new RandomPreferences(Set.of(), Set.of()));
		final RandomCalling selector = new RandomCalling(characterPlayer,
				new RandomPreferences(Set.of(), Set.of(profile)));

		Assert.assertTrue(selector.getElementWeight(calling) > baseSelector.getElementWeight(calling));
	}

	@Test
	public void validateProfileReferences() throws InvalidXmlElementException {
		RandomProfileFactory.getInstance().validate();
	}

	@Test
	public void cambiadoProfileHasMandatorySpecies() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("cambiado");

		Assert.assertEquals(profile.getMandatorySpecies(), Set.of("animalized", "mutant", "tweaked", "clone",
				"metonym", "grimson", "inhuman"));
	}

	@Test
	public void cambiadoProfileHasChangedGroupSuggestedPerks() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("cambiado");

		Assert.assertEquals(profile.getSuggestedPerks().size(), 27);
		Assert.assertTrue(profile.getSuggestedPerks().containsAll(Set.of("claws", "gills", "horns", "immunity")));
	}

	@Test
	public void ocultistaProfileHasMandatoryOccultismPerks() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("ocultista");

		Assert.assertEquals(profile.getMandatoryPerks(), Set.of("psychicPowers", "theurgicRites"));
	}

	@Test
	public void ocultistaProfileRecommendsPsiAndTheurgyCharacteristics() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("ocultista");

		Assert.assertEquals(profile.getRecommendedCharacteristics(), Set.of("theurgy", "psi"));
	}

	@Test
	public void scoutProfileWasRenamedToWanderer() {
		final RandomProfile profile = RandomProfileFactory.getInstance().getElement("scout");

		Assert.assertEquals(profile.getName().getEnglish(), "Wanderer");
		Assert.assertEquals(profile.getName().getSpanish(), "Errante");
	}

	@Test
	public void loadDirectProfileMandatorySpeciesAndRecommendedCharacteristics() throws Exception {
		final String xml = """
				<profiles>
				    <profile>
				        <id>xmlSpeciesProfile</id>
				        <group>specialization</group>
				        <mandatorySpecies>
				            <specie>human</specie>
				            <specie>obun</specie>
				        </mandatorySpecies>
				        <recommendedCharacteristics>
				            <characteristic>psi</characteristic>
				            <characteristic>theurgy</characteristic>
				        </recommendedCharacteristics>
				        <mandatoryPerks>
				            <perk>psychicPowers</perk>
				            <perk>theurgicRites</perk>
				        </mandatoryPerks>
				    </profile>
				</profiles>
				""";

		final List<RandomProfile> profiles = XmlFactory.getObjectMapper().readerForListOf(RandomProfile.class)
				.readValue(xml);
		final RandomProfile profile = profiles.get(0);

		Assert.assertEquals(profile.getMandatorySpecies(), Set.of("human", "obun"));
		Assert.assertEquals(profile.getRecommendedCharacteristics(), Set.of("psi", "theurgy"));
		Assert.assertEquals(profile.getMandatoryPerks(), Set.of("psychicPowers", "theurgicRites"));
	}

	@Test
	public void recommendedCharacteristicIncreasesWeight() throws InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("priest");

		final RandomProfile profile = new RandomProfile();
		profile.setId("recommendedPsiProfile");
		profile.setRecommendedCharacteristics(Set.of("psi"));

		final RandomCharacteristics baseSelector = new RandomCharacteristics(characterPlayer,
				new RandomPreferences(Set.of(), Set.of()));
		final RandomCharacteristics selector = new RandomCharacteristics(characterPlayer,
				new RandomPreferences(Set.of(), Set.of(profile)));

		final var psi = CharacteristicsDefinitionFactory.getInstance().getElement("psi");

		Assert.assertTrue(selector.getElementWeight(psi) > baseSelector.getElementWeight(psi));
	}

	@Test
	public void checkCharacteristicsPreferencesWeightsForPrimitive() {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("noble");

		final RandomCharacteristicBonusOption randomCharacteristicBonusOption = new RandomCharacteristicBonusOption(
				characterPlayer, this.convert(TechPreference.PRIMITIVE),
				characterPlayer.getUpbringing().getCharacteristicOptions().get(0));

		randomCharacteristicBonusOption.updateWeights();
		// 3 characteristics plus the latest null value for selection.
		Assert.assertEquals(randomCharacteristicBonusOption.getWeightedElements().size(), 4);
		// Dexterity is recommended to faction.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(0).intValue(),
				(int) ProbabilityMultiplier.NORMAL.getValue() * RandomSelector.BASIC_PROBABILITY);
		// Endurance is a preferred characteristic for primitive.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(1).intValue(),
				(int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER)
						* RandomSelector.BASIC_PROBABILITY);
		// Strength is a preferred characteristic for primitive.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(2).intValue(),
				(int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER)
						* RandomSelector.BASIC_PROBABILITY);
	}

	@Test
	public void checkCharacteristicsPreferencesWeightsForVorox() {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("vorox");
		characterPlayer.setUpbringing("noble");

		final RandomCharacteristicBonusOption randomCharacteristicBonusOption = new RandomCharacteristicBonusOption(
				characterPlayer, this.convert(TechPreference.PRIMITIVE),
				characterPlayer.getUpbringing().getCharacteristicOptions().get(0));

		randomCharacteristicBonusOption.updateWeights();
		// 3 characteristics plus the latest null value for selection.
		Assert.assertEquals(randomCharacteristicBonusOption.getWeightedElements().size(), 4);
		// Dexterity is recommended to faction.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(0).intValue(),
				(int) ProbabilityMultiplier.NORMAL.getValue() * RandomSelector.BASIC_PROBABILITY);
		// Endurance is a preferred characteristic for primitive.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(1).intValue(),
				(RandomSelector.USER_SELECTION_MULTIPLIER + RandomSelector.HIGH_MULTIPLIER)
						* RandomSelector.BASIC_PROBABILITY);
		// Strength is a preferred characteristic for primitive.
		Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(2).intValue(),
				(RandomSelector.USER_SELECTION_MULTIPLIER + RandomSelector.HIGH_MULTIPLIER)
						* RandomSelector.BASIC_PROBABILITY);
	}

	@Test
	public void checkFactionPreferencesWeightsForEvil() throws InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("merchant");
		characterPlayer.setFaction("musters");
		final RandomFaction randomFaction = new RandomFaction(characterPlayer, this.convert(AlignmentPreference.EVIL));
		randomFaction.updateWeights();
		// Musters has a plus for evil preference.
		final Faction musters = FactionFactory.getInstance().getElement("musters");
		Assert.assertEquals(randomFaction.getElementWeight(musters),
				(int) (musters.getRandomDefinition().getProbabilityMultiplier().getValue()
						+ RandomSelector.USER_SELECTION_MULTIPLIER) * RandomSelector.BASIC_PROBABILITY);

		final Faction reeves = FactionFactory.getInstance().getElement("reeves");
		Assert.assertEquals(randomFaction.getElementWeight(reeves),
				(int) (musters.getRandomDefinition().getProbabilityMultiplier().getValue())
						* RandomSelector.BASIC_PROBABILITY);
	}

	@Test
	public void checkCharacteristicsWeightsForPrimitive() throws InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer();
		characterPlayer.setSpecie("human");
		characterPlayer.setUpbringing("merchant");
		characterPlayer.setFaction("musters");
		final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer,
            this.convert(TechPreference.PRIMITIVE));
		randomCharacteristics.updateWeights();
		Assert.assertEquals(randomCharacteristics
				.getElementWeight(CharacteristicsDefinitionFactory.getInstance().getElement("strength")), 25000);
		Assert.assertEquals(randomCharacteristics
				.getElementWeight(CharacteristicsDefinitionFactory.getInstance().getElement("dexterity")), 10000);
		Assert.assertEquals(randomCharacteristics
				.getElementWeight(CharacteristicsDefinitionFactory.getInstance().getElement("endurance")), 25000);
		Assert.assertEquals(randomCharacteristics
				.getElementWeight(CharacteristicsDefinitionFactory.getInstance().getElement("wits")), 2000);
	}
}
