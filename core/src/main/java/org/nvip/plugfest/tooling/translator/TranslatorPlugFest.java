package org.nvip.plugfest.tooling.translator;

import org.json.JSONObject;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * file: Translator.java
 *
 * Driver class for SPDX and CDX Translators
 * @author Tyler Drake
 * @author Matt London
 */
public class TranslatorPlugFest {
    /**
     * Parse an SBOM using the appropriate translator and return the object
     *
     * @param path Path to the SBOM to translate
     * @return SBOM object, null if failed
     */
    public static SBOM translate(String path) {
        // Read the contents at path into a string
        String contents = null;
        try {
            contents = new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return translateContents(contents, path);
    }

    /**
     * Parse an SBOM using the appropriate translator and return the object based on the contents of the file
     *
     * @param contents contents of the bom
     * @param filePath path to the bom
     * @return SBOM object, null if failed
     */
    public static SBOM translateContents(String contents, String filePath) {

        // Remove all non-ascii characters
        // TODO instead receive the file in utf8 so we can support these characters
        contents = contents.replaceAll("[^\\x00-\\x7F]", "");

        SBOM sbom = null;

        // TODO check the contents of the file rather than trusting the file extension
        // TODO address the parser exception rather than ignoring it

        final String extension = filePath.substring(filePath.toLowerCase().lastIndexOf('.'));

        try {

            switch (extension) {

                case ".xml"  -> sbom = TranslatorCDXXML.translatorCDXXMLContents(contents, filePath);

                case ".json" -> {
                    if (new JSONObject(new String(Files.readAllBytes(Paths.get(filePath)))).toMap().get("bomFormat").equals("CycloneDX")) {
                        sbom = TranslatorCDXJSON.translatorCDXJSONContents(contents, filePath);
                    }
                }

                case ".spdx" -> sbom = TranslatorSPDX.translatorSPDXContents(contents, filePath);

                default      -> System.err.println("\nInvalid SBOM format found at: " + filePath);

            }

        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return sbom;
    }
}