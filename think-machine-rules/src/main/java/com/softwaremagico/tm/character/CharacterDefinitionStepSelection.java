package com.softwaremagico.tm.character;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;

import java.util.Arrays;
import java.util.List;

public class CharacterDefinitionStepSelection extends Element<CharacterDefinitionStepSelection> {

    @JsonProperty("capabilities")
    private List<CharacterSelectedElement> capabilityOptions;
    @JsonProperty("characteristics")
    private List<CharacterSelectedElement> characteristicOptions;
    @JsonProperty("skills")
    private List<CharacterSelectedElement> skillOptions;
    @JsonProperty("perks")
    private List<CharacterSelectedElement> perksOptions;

    public CharacterDefinitionStepSelection(CharacterDefinitionStep<?> characterDefinitionStep) throws InvalidGeneratedCharacter {
        if (characterDefinitionStep == null) {
            throw new InvalidGeneratedCharacter("You cannot make a selection if there is no step defined.");
        }
        setId(characterDefinitionStep.getId());
        setName(characterDefinitionStep.getName());
        setDescription(characterDefinitionStep.getDescription());

        setCapabilityOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCapabilityOptions().size()]));
        setCharacteristicOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCharacteristicOptions().size()]));
        setSkillOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getSkillOptions().size()]));
        setPerksOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getPerksOptions().size()]));
    }

    public List<CharacterSelectedElement> getCapabilityOptions() {
        return capabilityOptions;
    }

    public void setCapabilityOptions(List<CharacterSelectedElement> capabilityOptions) {
        this.capabilityOptions = capabilityOptions;
    }

    public List<CharacterSelectedElement> getCharacteristicOptions() {
        return characteristicOptions;
    }

    public void setCharacteristicOptions(List<CharacterSelectedElement> characteristicOptions) {
        this.characteristicOptions = characteristicOptions;
    }

    public List<CharacterSelectedElement> getSkillOptions() {
        return skillOptions;
    }

    public void setSkillOptions(List<CharacterSelectedElement> skillOptions) {
        this.skillOptions = skillOptions;
    }

    public List<CharacterSelectedElement> getPerksOptions() {
        return perksOptions;
    }

    public void setPerksOptions(List<CharacterSelectedElement> perksOptions) {
        this.perksOptions = perksOptions;
    }
}
