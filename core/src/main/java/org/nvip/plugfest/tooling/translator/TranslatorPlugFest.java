/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.translator;

import org.json.JSONObject;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * file: Translator.java
 *
 * Driver class for SPDX and CDX Translators
 * @author Tyler Drake
 * @author Matt London
 */
public class TranslatorPlugFest {
    private final static String INVALID_FILE_CONTENTS = "Invalid SBOM file contents (could not assume schema).";
    private final static Function<String, String> INVALID_FILE_TYPE = (ext) -> "File type " + ext + " not supported.";

    /**
     * Parse an SBOM using the appropriate translator and return the object
     *
     * @param path Path to the SBOM to translate
     * @return SBOM object
     * @throws TranslatorException if translation failed
     */
    public static SBOM translate(String path) throws TranslatorException {
        // Read the contents at path into a string
        String contents = null;
        try {
            contents = new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            throw new TranslatorException(e.getMessage());
        }

        return translateContents(contents, path);
    }

    /**
     * Parse an SBOM using the appropriate translator and return the object based on the contents of the file
     *
     * @param contents contents of the bom
     * @param filePath path to the bom
     * @return SBOM object
     * @throws TranslatorException if translation failed
     */
    public static SBOM translateContents(String contents, String filePath) throws TranslatorException {
        TranslatorCore translator = getTranslator(filePath);

        SBOM result = translator.translateContents(contents, filePath);
        if (result == null) throw new TranslatorException("Unknown error while translating.");
        return result;
    }

    private static TranslatorCore getTranslator(String filePath) throws TranslatorException {
        String ext = filePath.substring(filePath.lastIndexOf('.') + 1).trim().toLowerCase();

        switch (ext.toLowerCase()) {
            case "json" -> { return new TranslatorCDXJSON(); }
            case "xml" -> { return new TranslatorCDXXML(); }
            case "spdx" -> { return new TranslatorSPDX(); }
            default -> { throw new TranslatorException(INVALID_FILE_TYPE.apply(ext)); }
        }
    }
}