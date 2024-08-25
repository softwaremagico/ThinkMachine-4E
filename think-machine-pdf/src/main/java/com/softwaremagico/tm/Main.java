package com.softwaremagico.tm;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
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

import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.io.File;


public final class Main {
    private static final int LANGUAGE_ARG = 0;
    private static final int FILE_DESTINATION_PATH_ARG = 1;
    private static final int JSON_FILE_ARG = 2;
    private static String language;
    private static String destinationPath;
    private static String jsonFile;

    private Main() {

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            MachineLog.info(Main.class.getName(), "Execute with parameters:");
            MachineLog.info(Main.class.getName(), "\t\tlanguage\tThe language to print the sheet file.");
            MachineLog.info(Main.class.getName(), "\t\tpath\t\tThe path to store the file.");
            MachineLog.info(Main.class.getName(), "\t\tcharacter\tThe character definition (as Json) to fill up the sheet.");
            MachineLog.info(Main.class.getName(), "Example:");
            MachineLog.info(Main.class.getName(), "\tmvn exec:java -Dexec.args=\"en /tmp character.json\"");
            System.exit(0);
        }
        setArguments(args);

        final CharacterSheet sheet;
//        if (jsonFile == null) {
//            sheet = new CharacterSheet(language, PathManager.DEFAULT_MODULE_FOLDER);
//            sheet.createFile(destinationPath + "FadingSuns_" + language.toUpperCase() + ".pdf");
//        } else {
//            try {
//                final CharacterPlayer player = CharacterJsonManager.fromFile(jsonFile);
//                sheet = new CharacterSheet(player);
//                sheet.createFile(destinationPath + "FadingSuns_" + language.toUpperCase() + ".pdf");
//            } catch (IOException | InvalidJsonException e) {
//                PdfExporterLog.errorMessage(Main.class.getName(), e);
//            }
//        }
    }

    private static void setArguments(String[] args) {
        if (args.length <= LANGUAGE_ARG) {
            language = "en";
        } else {
            language = args[LANGUAGE_ARG];
        }

        if (args.length <= FILE_DESTINATION_PATH_ARG) {
            destinationPath = System.getProperty("java.io.tmpdir");
        } else {
            destinationPath = args[FILE_DESTINATION_PATH_ARG] + File.separator;
        }

        if (args.length <= JSON_FILE_ARG) {
            jsonFile = null;
        } else {
            jsonFile = args[JSON_FILE_ARG];
        }
    }
}
