package org.nvip.plugfest.tooling.translator.utils;

import org.nvip.plugfest.tooling.sbom.SBOM;

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
    public static boolean checkForAppTools(SBOM sbom) {
        for (String m: sbom.getMetadata().values()
        ) {
            if(sbom.checkForTool(m) != null)
                return true;
        }
        return false;
    }

}
