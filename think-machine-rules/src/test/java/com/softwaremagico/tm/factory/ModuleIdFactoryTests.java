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
import com.softwaremagico.tm.character.equipment.armors.ArmorSpecificationFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
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
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.txt.TextFactory;
import com.softwaremagico.tm.xml.XmlFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Test(groups = {"factoryValidation"})
public class ModuleIdFactoryTests extends FactoryTest {
    private static final List<XmlFactory<? extends Element>> FACTORIES = List.of(
            AccessoryFactory.getInstance(),
            AmmunitionFactory.getInstance(),
            ArmorFactory.getInstance(),
            ArmorSpecificationFactory.getInstance(),
            CallingFactory.getInstance(),
            CapabilityFactory.getInstance(),
            CharacteristicsDefinitionFactory.getInstance(),
            CombatStyleFactory.getInstance(),
            CyberdeviceFactory.getInstance(),
            DamageTypeFactory.getInstance(),
            EquipmentTraitFactory.getInstance(),
            FactionFactory.getInstance(),
            HandheldShieldFactory.getInstance(),
            ItemFactory.getInstance(),
            OccultismPathFactory.getInstance(),
            OccultismTypeFactory.getInstance(),
            PerkFactory.getInstance(),
            AfflictionFactory.getInstance(),
            PlanetFactory.getInstance(),
            ShieldFactory.getInstance(),
            SkillFactory.getInstance(),
            SpecialValueFactory.getInstance(),
            SpecieFactory.getInstance(),
            TechCompulsionFactory.getInstance(),
            TextFactory.getInstance(),
            TheurgyComponentFactory.getInstance(),
            ThinkMachineFactory.getInstance(),
            TimeFactory.getInstance(),
            UpbringingFactory.getInstance(),
            WeaponFactory.getInstance()
    );

    @Test
    public void allLoadedElementsHaveConsistentModuleId() throws InvalidXmlElementException {
        for (XmlFactory<? extends Element> factory : FACTORIES) {
            for (Element element : factory.getElements()) {
                assertElementTree(element, null, factory.getClass().getSimpleName(), Collections.newSetFromMap(new IdentityHashMap<>()));
            }
        }
    }

    @Test
    public void allModuleElementsMatchTheirSourceModule() throws IOException {
        for (XmlFactory<? extends Element> factory : FACTORIES) {
            final Class<? extends Element> elementClass = getElementClass(factory);
            for (String moduleName : ModuleManager.getAllModules()) {
                if (!hasResource(moduleName, factory.getXmlFile())) {
                    continue;
                }
                for (Element element : readModuleElements(factory, elementClass, moduleName)) {
                    assertElementTree(element, moduleName, factory.getClass().getSimpleName(), Collections.newSetFromMap(new IdentityHashMap<>()));
                }
            }
        }
    }

    @Test
    public void elementModuleAllowsNameTranslationInSpanishAndEnglish() throws InvalidXmlElementException {
        final Element element = OccultismPathFactory.getInstance().getElement("beastBond");

        Assert.assertEquals(element.getModuleName(), ModuleManager.VULDROK_SPACE_MODULE,
                "The element must keep its source module folder name.");
        Assert.assertEquals(element.getModuleId(), "vuldrokSpace",
                "The element must resolve the expected module id.");

        Assert.assertEquals(ModuleManager.getModuleName(element.getModuleId(), "en"), "Vuldrok Space",
                "Module english name must be available from module id.");
        Assert.assertEquals(ModuleManager.getModuleName(element.getModuleId(), "es"), "Espacio Vuldrok",
                "Module spanish name must be available from module id.");
    }

    private void assertElementTree(Element element, String expectedModuleName, String source,
                                   Set<Object> visited) {
        assertElementTreeObject(element, expectedModuleName, source, visited);
    }

    private void assertElementTreeObject(Object value, String expectedModuleName, String source,
                                         Set<Object> visited) {
        if (value == null || !visited.add(value)) {
            return;
        }

        if (value instanceof Element element) {
            final String context = source + " -> " + element.getClass().getSimpleName() + ":" + element.getId();
            Assert.assertNotNull(element.getModuleName(), context + " must define moduleName.");
            Assert.assertFalse(element.getModuleName().isBlank(), context + " must define moduleName.");
            Assert.assertNotNull(element.getModuleId(), context + " must define moduleId.");
            Assert.assertFalse(element.getModuleId().isBlank(), context + " must define moduleId.");
            Assert.assertEquals(element.getModuleId(), ModuleManager.getModuleId(element.getModuleName()),
                    context + " must use the moduleId mapped for its module.");
            if (expectedModuleName != null) {
                Assert.assertEquals(element.getModuleName(), expectedModuleName,
                        context + " must keep the source module name.");
            }
        }

        if (value instanceof Map<?, ?> map) {
            map.values().forEach(mapValue -> assertElementTreeObject(mapValue, expectedModuleName, source, visited));
            return;
        }

        if (value instanceof Collection<?> collection) {
            collection.forEach(item -> assertElementTreeObject(item, expectedModuleName, source, visited));
            return;
        }

        final Class<?> valueClass = value.getClass();
        if (valueClass.isArray()) {
            final int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                assertElementTreeObject(Array.get(value, i), expectedModuleName, source, visited);
            }
            return;
        }

        if (!valueClass.getPackageName().startsWith("com.softwaremagico.tm")) {
            return;
        }

        for (Class<?> currentClass = valueClass; currentClass != null && currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            if (!currentClass.getPackageName().startsWith("com.softwaremagico.tm")) {
                break;
            }
            for (Field field : currentClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.getType().isPrimitive() || field.getType().isEnum()) {
                    continue;
                }
                try {
                    if (!field.trySetAccessible()) {
                        continue;
                    }
                    assertElementTreeObject(field.get(value), expectedModuleName, source, visited);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot inspect field '" + field.getName() + "' from '" + valueClass.getName() + "'.", e);
                }
            }
        }
    }

    private boolean hasResource(String moduleName, String xmlFile) {
        return ClassLoader.getSystemResource(PathManager.getModulePath(moduleName) + xmlFile) != null;
    }

    @SuppressWarnings("unchecked")
    private List<? extends Element> readModuleElements(XmlFactory<? extends Element> factory,
                                                       Class<? extends Element> elementClass,
                                                       String moduleName) throws IOException {
        return ((XmlFactory<Element>) factory).readXml((Class<Element>) elementClass, moduleName);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Element> getElementClass(XmlFactory<? extends Element> factory) {
        return (Class<? extends Element>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}


