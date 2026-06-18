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


import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkSource;
import com.softwaremagico.tm.character.perks.PerkType;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.restrictions.RestrictionMode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"perkFactory"})
public class PerkFactoryTests extends FactoryTest {

    private static final int DEFINED_PERKS = 345;
    private static final int DEFINED_FACTION_BOOK_PERKS = 43;
    private static final int DEFINED_IMPERIAL_DOSSIER_BROTHER_BATTLE_PERKS = 5;


    @Override
    @BeforeClass
    public void enableBasicModule() {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        ModuleManager.resetModules();
    }


    @Test
    public void readPerks() throws InvalidXmlElementException {
        Assert.assertEquals(PerkFactory.getInstance().getElements().size(),
                DEFINED_PERKS + DEFINED_FACTION_BOOK_PERKS + DEFINED_IMPERIAL_DOSSIER_BROTHER_BATTLE_PERKS);
    }

    @Test(dependsOnMethods = "readPerks")
    public void readPerksOnAllModules() throws InvalidXmlElementException {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        ModuleManager.resetModules();

        Assert.assertEquals(PerkFactory.getInstance().getElements().size(),
                DEFINED_PERKS + DEFINED_FACTION_BOOK_PERKS + DEFINED_IMPERIAL_DOSSIER_BROTHER_BATTLE_PERKS);
    }

    @Test
    public void getPerkRestrictionMode() throws InvalidXmlElementException {
        Assert.assertEquals(PerkFactory.getInstance().getElement("chartophylax").getRestrictions().getMode(),
                RestrictionMode.ANY_FROM_GROUP);
    }

    @Test
    public void getPerkType() throws InvalidXmlElementException {
        Assert.assertEquals(PerkFactory.getInstance().getElement("absolution").getType(),
                PerkType.PRIVILEGE);
    }

    @Test
    public void getPerkTypeDestinator() throws InvalidXmlElementException {
        Assert.assertEquals(PerkFactory.getInstance().getElement("destinator").getType(),
                PerkType.CYBERDEVICE);
    }

    @Test
    public void getPerkClass() throws InvalidXmlElementException {
        Assert.assertEquals(PerkFactory.getInstance().getElement("absolution").getSource(),
                PerkSource.CALLING);
    }
}
