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

        SBOM sbom = null;

        try {
            TranslatorCore translator = getTranslator(contents, filePath);

            if (translator == null) System.err.println("Error translating file: " + filePath + ".\nReason: Invalid " +
                    "SBOM file contents (could not assume schema).");
            else sbom = translator.translateContents(contents, filePath);
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return sbom;
    }

    private static TranslatorCore getTranslator(String contents, String filePath) {
        String ext = filePath.substring(filePath.lastIndexOf('.') + 1).trim().toLowerCase();

        switch (ext.toLowerCase()) {
            case "json" -> {
                if (contents.contains("\"bomFormat\": \"CycloneDX\"")) return new TranslatorCDXJSON();
//                else if (contents.contains("\"SPDXID\" : \"SPDXRef-DOCUMENT\"")) return new TranslatorSPDXJSON();
                else return null;
            }
            case "xml" -> {
                if (contents.contains("<bom xmlns=\"http://cyclonedx.org/schema/bom/")) return new TranslatorCDXXML();
//                else if (contents.contains("<SPDXID>SPDXRef-DOCUMENT</SPDXID>")) return new TranslatorSPDXXML();
                else return null;
            }
//            case "yml" -> { return new TranslatorSPDXYAML(); }
            case "spdx" -> { return new TranslatorSPDX(); }
            default -> { return null; }
        }
    }
}