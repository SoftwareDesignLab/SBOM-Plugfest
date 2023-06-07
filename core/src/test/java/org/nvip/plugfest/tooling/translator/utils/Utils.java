package org.nvip.plugfest.tooling.translator.utils;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * Helper method to check that SBOMs that should have no licenses do not have that field with any licenses added
     * @param sbom to check
     */
    public static boolean noLicensesCheck(SBOM sbom){
        for (Component c: sbom.getAllComponents())
            if(c.getLicenses().size() > 0)
                return false;
        return true;
    }

}
