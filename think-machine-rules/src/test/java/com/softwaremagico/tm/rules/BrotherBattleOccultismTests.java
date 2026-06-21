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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "occultism")
public class BrotherBattleOccultismTests extends RulesTest {

    @Test
    public void brotherBattleCanPickNewAndExistingRites() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");

        // Force Theurgy high enough to validate high-level rites in this focused test.
        characterPlayer.setPrimaryCharacteristic("theurgy");

        // Select theurgy bonuses from all Brother Battle faction characteristic options.
        for (int i = 0; i < 3; i++) {
            characterPlayer.getFaction().getSelectedCharacteristicOptions().get(i)
                    .getSelections().add(new Selection(CharacteristicsDefinitionFactory.getInstance().getElement("theurgy")));
        }

        characterPlayer.getCalling().getSelectedPerksOptions().get(0)
                .getSelections().add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.THEURGY_RITES_PERK)));

        Assert.assertTrue(characterPlayer.getCharacteristicValue("theurgy") >= 8,
                "The test character must reach Theurgy 8 or higher.");

        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance()
                        .getElement("brotherBattleDossierRituals").getOccultismPowers().get("sentinelsWatchfulEye")),
                "Brother Battle should be able to pick the new Sentinel's Watchful Eye rite.");

        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance()
                        .getElement("brotherBattleDossierRituals").getOccultismPowers().get("fortressOfThePilgrimsDefense")),
                "Brother Battle should be able to pick the new Fortress of the Pilgrim's Defense rite.");

        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance()
                        .getElement("brotherBattleRituals").getOccultismPowers().get("armorOfThePancreator")),
                "Brother Battle should also be able to pick existing Brother Battle rites.");
    }
}



