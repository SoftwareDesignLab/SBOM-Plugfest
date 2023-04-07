package plugfest.tooling.translator;

import plugfest.tooling.sbom.SBOM;


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
        try {
            if (path.toLowerCase().endsWith(".xml")) {
                sbom = TranslatorCDXXML.translatorCDXXML(path);
            } else if (path.toLowerCase().endsWith(".spdx")) {
                sbom = TranslatorSPDX.translatorSPDX(path);
            } else {
                System.err.println("\nInvalid SBOM format found at: " + path);
            }
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return sbom;
    }
}