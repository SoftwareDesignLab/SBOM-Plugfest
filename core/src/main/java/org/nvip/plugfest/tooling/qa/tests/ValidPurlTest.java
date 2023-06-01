package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.qa.tests.Result;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidPurlTest extends org.nvip.plugfest.tooling.qa.tests.MetricTest {
    private static final String TEST_NAME = "ValidPurl";

    private Pattern purlRegex;

    public ValidPurlTest() {
        this.purlRegex = Pattern.compile("^pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@([a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#([a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);
    }

    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        Set<String> purlStrings = new HashSet<>();

        for (Component c : sbom.getAllComponents()) {
            for (PURL p : c.getPurls()) {
                purlStrings.add(p.toString());
            }
        }

        for (String p : purlStrings) {
            if (!this.purlRegex.matcher(p.strip()).matches()) {
                results.add(new Result(TEST_NAME, Result.STATUS.FAIL, "TODO: MESSAGE"));
            } else {
                results.add(new Result(TEST_NAME, Result.STATUS.PASS, "TODO: MESSAGE"));
            }
        }

        // return findings
        return results;
    }
}
