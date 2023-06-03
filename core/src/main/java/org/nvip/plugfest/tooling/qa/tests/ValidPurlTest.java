package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.qa.tests.Result;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.PURL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidPurlTest extends org.nvip.plugfest.tooling.qa.tests.MetricTest {
    private static final String TEST_NAME = "ValidPurl";

    private Pattern purlRegex;

    /***
     * Regex for purl
     */
    public ValidPurlTest() {
        this.purlRegex = Pattern.compile("^pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@([a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#([a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);
    }

    /***
     * Validates the PURL
     * @param sbom SBOM to test
     * @return
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        for (Component c : sbom.getAllComponents()) {
            for (PURL p : c.getPurls()) {
                if (p == null)
                    continue;
                Result r;
                if (this.purlRegex.matcher(p.toString().strip()).matches()) {
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Purl Passed");
                } else {
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Purl Failed");
                }
                r.addContext(sbom, "purl");
                results.add(r);
            }
        }


        // return findings
        return results;
    }
}
