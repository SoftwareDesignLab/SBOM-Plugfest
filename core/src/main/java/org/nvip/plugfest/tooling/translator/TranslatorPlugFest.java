package org.nvip.plugfest.tooling.translator;

import org.apache.jena.atlas.json.JSON;
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

        SBOM sbom = null;

        // TODO check the contents of the file rather than trusting the file extension
        // TODO address the parser exception rather than ignoring it

        final String extension = path.substring(path.toString().toLowerCase().lastIndexOf('.'));

        try {

            switch (extension) {

                case ".xml"  -> sbom = TranslatorCDXXML.translatorCDXXML(path);

                case ".json" -> {
                    if (new JSONObject(new String(Files.readAllBytes(Paths.get(path)))).toMap().get("bomFormat").equals("CycloneDX")) {
                        sbom = TranslatorCDXJSON.translatorCDXJSON(path.toString());
                    }
                }

                case ".spdx" -> sbom = TranslatorSPDX.translatorSPDX(path);

                default      -> System.err.println("\nInvalid SBOM format found at: " + path);

            }

        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return sbom;
    }
}