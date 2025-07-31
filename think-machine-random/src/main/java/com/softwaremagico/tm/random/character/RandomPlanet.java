package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Set;

public class RandomPlanet extends RandomSelector<Planet> {
    private static final int FACTION_PLANET = 50;
    private static final int NEUTRAL_PLANET = 8;
    private static final int ENEMY_PLANET = 1;

    public RandomPlanet(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences)
            throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        getCharacterPlayer().getInfo().setPlanet(selectElementByWeight());
    }

    @Override
    protected Collection<Planet> getAllElements() throws InvalidXmlElementException {
        return PlanetFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Planet planet) {
        //By faction
        if (planet.getFactions().contains(getCharacterPlayer().getFaction().get().getId())) {
            return FACTION_PLANET;
        }
        for (final String factionsOfPlanet : planet.getFactions()) {
            if (factionsOfPlanet != null && getCharacterPlayer().getFaction() != null
                    && FactionFactory.getInstance().getElement(factionsOfPlanet).getRestrictions().isRestricted(getCharacterPlayer())) {
                return ENEMY_PLANET;
            }
        }
        return NEUTRAL_PLANET;
    }

    @Override
    protected void assignIfMandatory(Planet element) throws InvalidXmlElementException {
        return;
    }

    @Override
    protected void assignMandatoryValues(Set<Planet> mandatoryValues) throws InvalidXmlElementException {
        return;
    }
}
