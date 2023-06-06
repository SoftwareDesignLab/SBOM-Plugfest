package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * file: HasCreationInfoTest.java
 *
 * For an SPDX SBOM, test components to see if any contain extracted
 * licenses not on the SPDX license list and list them
 * @author Matthew Morrison
 */
public class HasExtractedLicensesTest extends MetricTest{
    public static final String TEST_NAME = "HasExtractedLicenses";

    /**
     * Check all components in a given SBOM for extracted licenses not on
     * the SPDX license list
     * @param sbom SBOM to test
     * @return a collection of results if any extracted licenses exist
     * in the SBOM
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        Result r;

        // for every component, check for any extracted licenses
        for(Component c : sbom.getAllComponents()){
            Map<String, Map<String, String>>  extractedLicenses = c.getExtractedLicenses();
            // skip if no extracted licenses are found for the component
            if(extractedLicenses.isEmpty()){
                continue;
            }
            else{
                for(Map<String, String> license : extractedLicenses.values()){
                    String message = "Extracted license found for component "
                            + c.getName() + ": " + license.get("name");
                    r = new Result(TEST_NAME, Result.STATUS.PASS, message);
                    r.addContext(c, "Extracted Licenses");
                    results.add(r);
                }
            }
        }

        if(results.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.PASS, "No Extracted " +
                    "Licenses found in SBOM");
            r.addContext(sbom, "Extracted Licenses");
            results.add(r);
        }

        return results;
    }
}
