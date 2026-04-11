package com.softwaremagico.tm.file.modules;

/*-
 * #%L
 * Think Machine (Rules)
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ModuleManager {
    public static final String FADING_SUNS_PLAYER_GUIDE_MODULE = "Fading Suns 4E";
    public static final String FACTION_BOOK_MODULE = "Faction Book";
    private static final String[] TOTAL_MODULES = {FADING_SUNS_PLAYER_GUIDE_MODULE, FACTION_BOOK_MODULE};
    private static final Set<String> ENABLED_MODULES = new HashSet<>(Arrays.asList(TOTAL_MODULES));

    private ModuleManager() {

    }

    public static String[] getAllModules() {
        return TOTAL_MODULES.clone();
    }

    public static Set<String> getEnabledModules() {
        return ENABLED_MODULES;
    }

    public static void enableModule(String module) {
        ENABLED_MODULES.add(module);
    }

    public static void disableModule(String module) {
        ENABLED_MODULES.remove(module);
    }
}
