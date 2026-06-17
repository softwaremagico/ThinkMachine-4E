package com.softwaremagico.tm;

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

import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.file.configurator.BooleanValueConverter;
import com.softwaremagico.tm.file.configurator.DoubleValueConverter;
import com.softwaremagico.tm.file.configurator.IntegerValueConverter;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.structures.SelectionList;
import com.softwaremagico.tm.structures.SelectionSet;
import com.softwaremagico.tm.utils.ComparableUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Test(groups = "supportClasses")
public class SupportClassesTests {

    @Test
    public void selectionListNotifiesAddedListenersOnMutatingAddOperations() {
        final SelectionList<String> selectionList = new SelectionList<>();
        final AtomicInteger addedNotifications = new AtomicInteger(0);
        final AtomicInteger removedNotifications = new AtomicInteger(0);
        selectionList.addSelectionAddedListeners(addedNotifications::incrementAndGet);
        selectionList.addSelectionRemovedListeners(removedNotifications::incrementAndGet);

        selectionList.add("alpha");
        selectionList.add(0, "beta");
        selectionList.addAll(List.of("gamma", "delta"));
        selectionList.set(1, "epsilon");

        Assert.assertEquals(selectionList, List.of("beta", "epsilon", "gamma", "delta"));
        Assert.assertEquals(addedNotifications.get(), 4);
        Assert.assertEquals(removedNotifications.get(), 0);
    }

    @Test
    public void selectionListNotifiesRemovedListenersOnMutatingRemoveOperations() {
        final SelectionList<String> selectionList = new SelectionList<>(List.of("alpha", "beta", "gamma"));
        final AtomicInteger addedNotifications = new AtomicInteger(0);
        final AtomicInteger removedNotifications = new AtomicInteger(0);
        selectionList.addSelectionAddedListeners(addedNotifications::incrementAndGet);
        selectionList.addSelectionRemovedListeners(removedNotifications::incrementAndGet);

        selectionList.remove("alpha");
        selectionList.removeAll(List.of("gamma"));

        Assert.assertEquals(selectionList, List.of("beta"));
        Assert.assertEquals(addedNotifications.get(), 0);
        Assert.assertEquals(removedNotifications.get(), 2);
    }

    @Test
    public void selectionSetNotifiesUpdatedListenersOnMutations() {
        final SelectionSet<String> selectionSet = new SelectionSet<>();
        final AtomicInteger updatedNotifications = new AtomicInteger(0);
        selectionSet.addSelectionUpdatedListeners(updatedNotifications::incrementAndGet);

        selectionSet.add("alpha");
        selectionSet.remove("alpha");

        Assert.assertTrue(selectionSet.isEmpty());
        Assert.assertEquals(updatedNotifications.get(), 2);
    }

    @Test
    public void booleanValueConverterInterpretsSupportedValues() {
        final BooleanValueConverter converter = new BooleanValueConverter();

        Assert.assertTrue(converter.convertFromString(null));
        Assert.assertTrue(converter.convertFromString("true"));
        Assert.assertFalse(converter.convertFromString("false"));
        Assert.assertFalse(converter.convertFromString("unexpected"));
        Assert.assertEquals(converter.convertToString(Boolean.TRUE), "true");
        Assert.assertNull(converter.convertToString(null));
    }

    @Test
    public void numericConvertersConvertValuesAndPreserveNulls() {
        final IntegerValueConverter integerConverter = new IntegerValueConverter();
        final DoubleValueConverter doubleConverter = new DoubleValueConverter();

        Assert.assertEquals(integerConverter.convertFromString("42"), Integer.valueOf(42));
        Assert.assertEquals(integerConverter.convertToString(42), "42");
        Assert.assertNull(integerConverter.convertFromString(null));
        Assert.assertNull(integerConverter.convertToString(null));

        Assert.assertEquals(doubleConverter.convertFromString("42.5"), Double.valueOf(42.5));
        Assert.assertEquals(doubleConverter.convertToString(42.5d), "42.5");
        Assert.assertNull(doubleConverter.convertFromString(null));
        Assert.assertNull(doubleConverter.convertToString(null));
    }

    @Test
    public void translatorStoresLanguageInLowerCase() {
        final String originalLanguage = Translator.getLanguage();

        try {
            Translator.setLanguage("ES");

            Assert.assertEquals(Translator.getLanguage(), "es");
        } finally {
            Translator.setLanguage(originalLanguage);
        }
    }

    @Test
    public void languageUsesNameForStringRepresentationEqualityAndOrdering() {
        final Language english = new Language("English", "EN", "en.png");
        final Language sameName = new Language("English", "GB", "gb.png");
        final Language spanish = new Language("Spanish", "ES", "es.png");

        Assert.assertEquals(english.toString(), "English");
        Assert.assertEquals(english, sameName);
        Assert.assertEquals(english.hashCode(), sameName.hashCode());
        Assert.assertTrue(english.compareTo(spanish) < 0);
        Assert.assertFalse(english.equals(null));
        Assert.assertFalse(english.equals("English"));
    }

    @Test
    public void comparableUtilsCreatesComparisonIdsWithOptionalSpecialization() {
        final Specialization specialization = new Specialization("duelist");

        Assert.assertEquals(ComparableUtils.getComparisonId("fight", specialization), "fight_duelist");
        Assert.assertEquals(ComparableUtils.getComparisonId("fight", (Specialization) null), "fight");
        Assert.assertEquals(ComparableUtils.getComparisonId("fight", "melee"), "fight_melee");
        Assert.assertEquals(ComparableUtils.getComparisonId("fight", (String) null), "fight");
    }
}

