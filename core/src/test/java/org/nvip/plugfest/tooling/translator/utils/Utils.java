package org.nvip.plugfest.tooling.translator.utils;

import org.nvip.plugfest.tooling.sbom.SBOM;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File: Utils.java
 * Utility class for Translator tests
 *
 * @author Juan Francisco Patino
 */
public class Utils {

    /**
     * Helper method to check that SBOM metadata does not contain app tools
     * @param sbom to check
     */
    public static boolean toolsInMetadata(SBOM sbom) {
        for (String m: sbom.getMetadata()
        ) {
            if(sbom.checkForTool(m) != null)
                return true;
        }
        return false;
    }

}
