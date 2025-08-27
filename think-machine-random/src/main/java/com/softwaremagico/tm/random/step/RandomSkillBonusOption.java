package com.softwaremagico.tm.random.step;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.log.RandomStepLog;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Set;

public class RandomSkillBonusOption extends RandomSkill {

    private final SkillBonusOptions skillOptions;

    public RandomSkillBonusOption(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                  SkillBonusOptions skillOptions) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.skillOptions = skillOptions;
    }

    @Override
    protected Collection<Skill> getAllElements() throws InvalidXmlElementException {
        return skillOptions.getOptions().stream().map(Option::getElement).toList();
    }


    @Override
    protected int getWeight(Skill element) throws InvalidRandomElementSelectedException {
        //Max skill at some levels.
        try {
            getCharacterPlayer().checkMaxValueByLevel(element, getCharacterPlayer().getSkillValue(element)
                    + skillOptions.getBonus());
            RandomStepLog.debug(this.getClass(), "Max level '" + element + "' value: "
                    + getCharacterPlayer().getSkillValue(element)
                    + " + " + skillOptions.getBonus() + " from " + skillOptions.getOptions());
        } catch (InvalidXmlElementException | MaxValueExceededException e) {
            return 0;
        }
        if (getCharacterPlayer().getSkillValue(element) + skillOptions.getBonus() >= Skill.SKILL_MAX_VALUE) {
            return 0;
        }
        return super.getWeight(element);
    }

}
