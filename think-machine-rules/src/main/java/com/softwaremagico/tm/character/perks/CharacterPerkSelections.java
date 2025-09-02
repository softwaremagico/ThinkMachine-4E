package com.softwaremagico.tm.character.perks;

import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.skills.Specialization;

import java.util.ArrayList;
import java.util.List;

public class CharacterPerkSelections {
    private int totalOptions = 1;
    private List<Selection> finalOptions;

    public CharacterPerkSelections(PerkOptions perkOptions) {
        setTotalOptions(perkOptions.getTotalOptions());
        setAvailableSelections(perkOptions);
    }


    private void setAvailableSelections(PerkOptions perkOptions) {
        finalOptions = new ArrayList<>();

        if (perkOptions.getSourceOptions() != null) {
            final List<PerkOption> options = new ArrayList<>(perkOptions.getSourceOptions());
            for (PerkOption option : options) {
                if (option.getSpecializations() != null) {
                    for (Specialization specialization : option.getSpecializations()) {
                        finalOptions.add(new Selection(option.getId(), specialization));
                    }
                } else {
                    finalOptions.add(new Selection(option.getId()));
                }
            }
        }
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<Selection> getFinalOptions() {
        return finalOptions;
    }

    public void setFinalOptions(List<Selection> finalOptions) {
        this.finalOptions = finalOptions;
    }
}
