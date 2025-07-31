package com.softwaremagico.tm.random.character.selectors;

public interface IRandomPreference<T> extends Comparable<T> {

    int maximum();

    int minimum();

    String name();


    IRandomPreference<T> getDefault();
}
