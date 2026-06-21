package com.softwaremagico.tm.factory;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.stream.Collectors;

@Test(groups = {"callingFactory"})
public class CallingFactoryTests extends FactoryTest {

    private static final int DEFINED_FACTIONS_FS_4E_CALLINGS = 52;
    private static final int DEFINED_FACTION_BOOK_CALLINGS = 1;
    private static final int DEFINED_IMPERIAL_DOSSIER_BROTHER_BATTLE_CALLINGS = 2;
    private static final int DEFINED_IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_CALLINGS = 2;
    private static final int DEFINED_IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_CALLINGS = 3;
    private static final int DEFINED_IMPERIAL_DOSSIER_REEVES_GUILD_CALLINGS = 4;
    private static final int DEFINED_VULDROK_SPACE_CALLINGS = 7;

    private static final int DEFINED_TOTAL_CALLINGS = DEFINED_FACTIONS_FS_4E_CALLINGS + DEFINED_FACTION_BOOK_CALLINGS
            + DEFINED_IMPERIAL_DOSSIER_BROTHER_BATTLE_CALLINGS + DEFINED_IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_CALLINGS
            + DEFINED_IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_CALLINGS + DEFINED_IMPERIAL_DOSSIER_REEVES_GUILD_CALLINGS
            + DEFINED_VULDROK_SPACE_CALLINGS;

    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElements().size(), DEFINED_TOTAL_CALLINGS);
    }

    @Test
    public void getCapabilityOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCapabilityOptions().size(), 2);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCapabilityOptions().get(1).getOptions().size(), 3);
    }

    @Test
    public void getCharacteristicOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacteristicOptions().size(), 4);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacteristicOptions().get(0).getCharacteristicBonus("endurance").getBonus(), 1);
    }

    @Test
    public void getSkillOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getSkillOptions().size(), 7);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getSkillOptions().get(0).getSkillBonus("academia").getBonus(), 1);
    }

    @Test
    public void getPerksOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacterAvailablePerksOptions().size(), 1);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacterAvailablePerksOptions().get(0).getOptions().size(), 125);
    }

    @Test(dependsOnMethods = "getPerksOption")
    public void checkTotalElementsOnlyOnFactionBook() throws InvalidXmlElementException {
        this.enableFactionBookOnly();

        Assert.assertEquals(CallingFactory.getInstance().getElements().size(), DEFINED_FACTION_BOOK_CALLINGS);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFactionBook")
    public void checkTotalElementsWithAllModules() throws InvalidXmlElementException {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_REEVES_GUILD_MODULE);
        ModuleManager.enableModule(ModuleManager.VULDROK_SPACE_MODULE);
        ModuleManager.resetModules();
        ItemFactory.getInstance().reset();

        Assert.assertEquals(CallingFactory.getInstance().getElements().size(), DEFINED_TOTAL_CALLINGS);
    }

    @Test(dependsOnMethods = "checkTotalElementsWithAllModules")
    public void checkFactionBookCallingItemAndSkills() throws InvalidXmlElementException {
        final var cyborg = CallingFactory.getInstance().getElement("cyborg");

        Assert.assertNotNull(cyborg);
        Assert.assertEquals(cyborg.getMaterialAwards().size(), 1);
        Assert.assertEquals(cyborg.getMaterialAwards().get(0).getOptions().iterator().next().getElement().getId(), "cyberneticRepairKit");
        Assert.assertEquals(cyborg.getSkillOptions().get(3).getSkillBonus("techRedemption").getBonus(), 3);
    }

    @Test
    public void battleCurateCharacteristicOptionsByType() throws InvalidXmlElementException {
        final var battleCurate = CallingFactory.getInstance().getElement("battleCurate");

        //Three tipos por filtro + una característica explícita (theurgy, OCCULTISM).
         Assert.assertEquals(battleCurate.getCharacteristicOptions().size(), 1);
         Assert.assertEquals(battleCurate.getCharacteristicOptions().get(0).getSourceOptions().size(), 4);
        //Tres tipos (3*3) + 1 explícita.
         Assert.assertEquals(battleCurate.getCharacteristicOptions().get(0).getOptions().size(), 10);

         final Set<CharacteristicType> availableTypes = battleCurate.getCharacteristicOptions().get(0).getOptions().stream()
                 .map(option -> option.getElement().getType())
                 .collect(Collectors.toSet());

        Assert.assertEquals(availableTypes, Set.of(
                CharacteristicType.BODY,
                CharacteristicType.MIND,
                CharacteristicType.SPIRIT,
                CharacteristicType.OCCULTISM));
    }

    @Test
    public void battleCurateSkillOptionsAreOpen() throws InvalidXmlElementException {
        final var battleCurate = CallingFactory.getInstance().getElement("battleCurate");

        // Battle Curate should have exactly 1 skill bonus option
        Assert.assertEquals(battleCurate.getSkillOptions().size(), 1);

        // That skill option should allow selecting 10 skills
        final var skillOption = battleCurate.getSkillOptions().get(0);
        Assert.assertEquals(skillOption.getTotalOptions(), 10);

        // The option should allow any selectable skill (empty options means all skills are available)
        final Set<String> availableSkillIds = skillOption.getOptions().stream()
                .map(option -> option.getElement().getId())
                .collect(Collectors.toSet());
        final Set<String> selectableSkillIds = SkillFactory.getInstance().getSelectableElements().stream()
                .map(Element::getId)
                .collect(Collectors.toSet());

        Assert.assertEquals(availableSkillIds, selectableSkillIds,
                "Battle Curate must expose all selectable skills when options are empty");
    }

    private void enableFactionBookOnly() {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.disableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE);
        ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE);
        ModuleManager.disableModule(ModuleManager.IMPERIAL_DOSSIER_REEVES_GUILD_MODULE);
        ModuleManager.disableModule(ModuleManager.VULDROK_SPACE_MODULE);
        ModuleManager.resetModules();
        ItemFactory.getInstance().reset();
    }
}
