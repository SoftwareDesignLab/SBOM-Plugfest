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
     * @return whether metadata contains AppTools or not
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
     * Helper method to check that SBOMs that should
     * have no licenses do not have that field with any licenses added
     * @param sbom to check
     * @return true if SBOM contains no licenses throughout its components
     */
    public static boolean noLicensesCheck(SBOM sbom){
        for (Component c: sbom.getAllComponents())
            if(c.getLicenses().size() > 0)
                return false;
        return true;
    }

}
