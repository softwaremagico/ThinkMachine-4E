package com.softwaremagico.tm.pdf.complete;

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


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.events.FooterEvent;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public abstract class PdfDocument {
    private static final int RIGHT_MARGIN = 30;
    private static final int LEFT_MARGIN = 30;
    private static final int TOP_MARGIN = 30;
    private static final int BOTTOM_MARGIN = 30;
    private final String language;
    private final String moduleName;

    protected PdfDocument(String language, String moduleName) {
        this.language = language;
        this.moduleName = moduleName;
    }

    protected Document addMetaData(Document document) {
        document.addTitle("Fading Suns Character Sheet");
        document.addAuthor("Software Magico");
        document.addCreator("Think Machine 4E");
        document.addSubject("RPG");
        document.addKeywords("RPG, Fading Suns, FS");
        document.addCreationDate();
        return document;
    }

    private void generatePDF(Document document, PdfWriter writer) throws EmptyPdfBodyException, InvalidXmlElementException, DocumentException {
        addMetaData(document);
        document.open();
        // createCharacterPDF(document);
        createContent(document);
        document.close();
    }

    protected abstract void createContent(Document document) throws InvalidXmlElementException, DocumentException;

    protected void addEvent(PdfWriter writer) {
        writer.setPageEvent(new FooterEvent());
    }

    /**
     * Pdf as byte array. Be careful with big PDF files.
     *
     * @return
     * @throws EmptyPdfBodyException
     * @throws DocumentException
     * @throws InvalidXmlElementException
     */
    public final byte[] generate() throws EmptyPdfBodyException, DocumentException, InvalidXmlElementException {
        final Document document = new Document(getPageSize(), RIGHT_MARGIN, LEFT_MARGIN, TOP_MARGIN, BOTTOM_MARGIN);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        addEvent(writer);
        generatePDF(document, writer);
        return baos.toByteArray();

    }

    protected abstract void addDocumentWriterEvents(PdfWriter writer);

    public int createFile(String path) {
        if (!path.endsWith(".pdf")) {
            path += ".pdf";
        }

        // DIN A6 105 x 148 mm
        final Document document = new Document(getPageSize(), RIGHT_MARGIN, LEFT_MARGIN, TOP_MARGIN, BOTTOM_MARGIN);

        // if (!MyFile.fileExist(path)) {
        try {
            final PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path)));
            addEvent(writer);
            generatePDF(document, writer);
            return writer.getPageNumber();
        } catch (Exception e) {
            PdfExporterLog.errorMessage(this.getClass().getName(), e);
            return 0;
        } finally {
            document.close();
        }
        // }
    }

    protected abstract Rectangle getPageSize();

    protected abstract void createCharacterPDF(Document document, CharacterPlayer character) throws Exception;

    public String getLanguage() {
        return language;
    }

    public String getModuleName() {
        return moduleName;
    }
}
