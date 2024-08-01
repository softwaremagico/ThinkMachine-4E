package com.softwaremagico.tm;

import java.util.Collection;
import java.util.StringTokenizer;

public class XmlData {


    protected void readCommaSeparatedTokens(Collection<String> collection, String collectionData) {
        if (collectionData != null) {
            final StringTokenizer collectionTokenizer = new StringTokenizer(collectionData, ",");
            while (collectionTokenizer.hasMoreTokens()) {
                collection.add(collectionTokenizer.nextToken().trim());
            }
        }
    }

}
