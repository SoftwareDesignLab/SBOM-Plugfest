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
        try {
            TranslatorCore translator = getTranslator(contents, filePath);

            if (translator == null) throw new TranslatorException(INVALID_FILE_CONTENTS + " File: " + filePath);

            return translator.translateContents(contents, filePath); // TODO test invalid test cases
        }
        catch (Exception e) {
            throw new TranslatorException(e.getMessage());
        }
    }

    private static TranslatorCore getTranslator(String contents, String filePath) throws TranslatorException {
        String ext = filePath.substring(filePath.lastIndexOf('.') + 1).trim().toLowerCase();

        switch (ext.toLowerCase()) { // TODO possibly scan for an entire file header to ensure validity?
            case "json" -> {
                if (contents.contains("\"bomFormat\": \"CycloneDX\"")) return new TranslatorCDXJSON();
//                else if (contents.contains("\"SPDXID\" : \"SPDXRef-DOCUMENT\"")) return new TranslatorSPDXJSON();
                else throw new TranslatorException(INVALID_FILE_CONTENTS + " File: " + filePath);
            }
            case "xml" -> {
                if (contents.contains("<bom xmlns=\"http://cyclonedx.org/schema/bom/")) return new TranslatorCDXXML();
//                else if (contents.contains("<SPDXID>SPDXRef-DOCUMENT</SPDXID>")) return new TranslatorSPDXXML();
                else throw new TranslatorException(INVALID_FILE_CONTENTS + " File: " + filePath);
            }
//            case "yml" -> { return new TranslatorSPDXYAML(); }
            case "spdx" -> { return new TranslatorSPDX(); }
            default -> { throw new TranslatorException(INVALID_FILE_TYPE.apply(ext)); }
        }
    }
}