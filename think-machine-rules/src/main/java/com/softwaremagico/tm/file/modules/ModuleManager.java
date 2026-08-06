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

import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.EquipmentTraitFactory;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.occultism.TheurgyComponentFactory;
import com.softwaremagico.tm.character.perks.AfflictionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.character.values.SpecialValueFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.txt.TextFactory;
import com.softwaremagico.tm.TranslatedText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ModuleManager {
    private static final String MODULES_DEFINITION_FILE = PathManager.getModulePath(null) + "modules.xml";
    public static final String FADING_SUNS_PLAYER_GUIDE_MODULE = "Fading Suns 4E";
    public static final String FADING_SUNS_REVISED_EDITION_MODULE = "Fading Suns Revised Edition";
    public static final String FACTION_BOOK_MODULE = "Faction Book";
    public static final String LOST_WORLDS_BOOK_MODULE = "Lost Worlds";
    public static final String IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE = "Imperial Dossier - Brother Battle";
    public static final String IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE = "Imperial Dossier - Charioteers Guild";
    public static final String IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE = "Imperial Dossier - House Hawkwood";
    public static final String IMPERIAL_DOSSIER_REEVES_GUILD_MODULE = "Imperial Dossier - Reeves Guild";
    public static final String VULDROK_SPACE_MODULE = "Vuldrok Space";
    private static final String[] TOTAL_MODULES = {FADING_SUNS_PLAYER_GUIDE_MODULE, FADING_SUNS_REVISED_EDITION_MODULE,
            FACTION_BOOK_MODULE, LOST_WORLDS_BOOK_MODULE,
            IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE, IMPERIAL_DOSSIER_CHARIOTEERS_GUILD_MODULE, IMPERIAL_DOSSIER_HOUSE_HAWKWOOD_MODULE,
            IMPERIAL_DOSSIER_REEVES_GUILD_MODULE, VULDROK_SPACE_MODULE};
    private static final Set<String> ENABLED_MODULES = new HashSet<>(Arrays.asList(TOTAL_MODULES));
    private static Map<String, String> moduleIds;
    private static Map<String, TranslatedText> moduleNames;

    private ModuleManager() {

    }

    public static String[] getAllModules() {
        return TOTAL_MODULES.clone();
    }

    public static Set<String> getEnabledModules() {
        return ENABLED_MODULES;
    }

    public static String getModuleId(String moduleName) {
        if (moduleName == null || moduleName.isBlank()) {
            return "";
        }
        loadModuleIds();
        return moduleIds.getOrDefault(moduleName, "");
    }

    public static String getModuleName(String moduleId, String language) {
        final TranslatedText translatedText = getModuleNameTranslations(moduleId);
        if (translatedText == null) {
            return "";
        }
        if ("es".equalsIgnoreCase(language)) {
            return translatedText.getSpanish();
        }
        return translatedText.getEnglish();
    }

    public static TranslatedText getModuleNameTranslations(String moduleId) {
        if (moduleId == null || moduleId.isBlank()) {
            return null;
        }
        loadModuleIds();
        final TranslatedText translatedText = moduleNames.get(moduleId);
        if (translatedText == null) {
            return null;
        }
        return new TranslatedText(translatedText.getSpanish(), translatedText.getEnglish());
    }

    private static synchronized void loadModuleIds() {
        if (moduleIds != null) {
            return;
        }
        moduleIds = new HashMap<>();
        moduleNames = new HashMap<>();
        try (InputStream inputStream = getModulesDefinitionStream()) {
            if (inputStream == null) {
                throw new IllegalStateException("Module definition file not found at '" + MODULES_DEFINITION_FILE + "'.");
            }

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);

            final Document document = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
            final NodeList modules = document.getElementsByTagName("module");
            for (int i = 0; i < modules.getLength(); i++) {
                final Element module = (Element) modules.item(i);
                final String id = getChildText(module, "id");
                final String folder = getChildText(module, "folder");
                final Element nameElement = getFirstChildElement(module, "name");
                final String moduleNameEn = getChildText(nameElement, "en");
                final String moduleNameEs = getChildText(nameElement, "es");
                if (id != null && !id.isBlank() && folder != null && !folder.isBlank()) {
                    moduleIds.put(folder, id);
                    moduleNames.put(id, new TranslatedText(moduleNameEs != null ? moduleNameEs : "",
                            moduleNameEn != null ? moduleNameEn : ""));
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            MachineLog.errorMessage(ModuleManager.class, e);
            throw new IllegalStateException("Cannot load module ids from '" + MODULES_DEFINITION_FILE + "'.", e);
        }
    }

    private static InputStream getModulesDefinitionStream() throws IOException {
        final ClassLoader classLoader = ModuleManager.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(MODULES_DEFINITION_FILE);
        if (inputStream != null) {
            return inputStream;
        }

        final File file = new File(MODULES_DEFINITION_FILE);
        if (file.exists()) {
            return file.toURI().toURL().openStream();
        }

        inputStream = ClassLoader.getSystemResourceAsStream(MODULES_DEFINITION_FILE);
        if (inputStream != null) {
            return inputStream;
        }
        return null;
    }

    private static String getChildText(Element parent, String tagName) {
        if (parent == null) {
            return null;
        }
        final NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0 || nodes.item(0) == null) {
            return null;
        }
        return nodes.item(0).getTextContent();
    }

    private static Element getFirstChildElement(Element parent, String tagName) {
        if (parent == null) {
            return null;
        }
        final NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0 || nodes.item(0) == null) {
            return null;
        }
        return (Element) nodes.item(0);
    }

    public static void enableModule(String module) {
        ENABLED_MODULES.add(module);
    }

    public static void resetModules() {

        PlanetFactory.getInstance().reset();

        SpecieFactory.getInstance().reset();
        UpbringingFactory.getInstance().reset();
        FactionFactory.getInstance().reset();
        CallingFactory.getInstance().reset();

        CharacteristicsDefinitionFactory.getInstance().reset();
        SkillFactory.getInstance().reset();
        CapabilityFactory.getInstance().reset();
        PerkFactory.getInstance().reset();
        AfflictionFactory.getInstance().reset();

        CombatStyleFactory.getInstance().reset();

        WeaponFactory.getInstance().reset();
        ArmorFactory.getInstance().reset();
        ShieldFactory.getInstance().reset();
        HandheldShieldFactory.getInstance().reset();
        AccessoryFactory.getInstance().reset();
        AmmunitionFactory.getInstance().reset();
        DamageTypeFactory.getInstance().reset();
        EquipmentTraitFactory.getInstance().reset();
        SpecialValueFactory.getInstance().reset();

        CyberdeviceFactory.getInstance().reset();
        TechCompulsionFactory.getInstance().reset();
        ThinkMachineFactory.getInstance().reset();

        OccultismPathFactory.getInstance().reset();
        OccultismTypeFactory.getInstance().reset();
        TheurgyComponentFactory.getInstance().reset();

        TimeFactory.getInstance().reset();
        TextFactory.getInstance().reset();
    }

    public static void disableModule(String module) {
        ENABLED_MODULES.remove(module);
    }
}
