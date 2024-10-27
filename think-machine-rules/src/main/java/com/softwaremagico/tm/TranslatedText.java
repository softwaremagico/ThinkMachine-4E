package com.softwaremagico.tm;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.language.Translator;

import java.util.Objects;

public class TranslatedText implements Comparable<TranslatedText> {
    @JsonProperty("es")
    private String spanish;
    @JsonProperty("en")
    private String english;

    public TranslatedText() {
        spanish = "";
        english = "";
    }

    public TranslatedText(TranslatedText translatedText, String suffix) {
        spanish = (translatedText != null ? translatedText.getSpanish() : "") + suffix;
        english = (translatedText != null ? translatedText.getEnglish() : "") + suffix;
    }

    public TranslatedText(TranslatedText translatedText, TranslatedText suffix) {
        spanish = (translatedText != null ? translatedText.getSpanish() : "")
                + (suffix != null && !suffix.getSpanish().isEmpty() ? " (" + suffix.getSpanish() + ")" : "");
        english = (translatedText != null ? translatedText.getEnglish() : "")
                + (suffix != null && !suffix.getEnglish().isEmpty() ? " (" + suffix.getEnglish() + ")" : "");
    }

    public TranslatedText(String value) {
        spanish = value;
        english = value;
    }

    public TranslatedText(String spanish, String english) {
        this.spanish = spanish;
        this.english = english;
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getTranslatedText() {
        if (Objects.equals("es", Translator.getLanguage())) {
            return getSpanish();
        }
        return getEnglish();
    }

    @Override
    public int compareTo(TranslatedText translatedText) {
        if (getTranslatedText() == null) {
            if (translatedText.getTranslatedText() == null) {
                return 0;
            }
            return -1;
        }
        if (translatedText.getTranslatedText() == null) {
            return 1;
        }
        return getTranslatedText().compareTo(translatedText.getTranslatedText());
    }

    @Override
    public String toString() {
        return getTranslatedText();
    }
}
