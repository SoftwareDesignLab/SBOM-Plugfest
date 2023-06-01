package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.qa.tests.Result;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidCPETest extends org.nvip.plugfest.tooling.qa.tests.MetricTest {
    private static final String TEST_NAME = "ValidCPE";

    private Pattern cpe23Regex;

    public ValidCPETest() {
        this.cpe23Regex = Pattern.compile("cpe:2\\.3:[aho\\*\\-](:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?" +
                "!\"#$$%&'\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){5}(:(([a-zA-Z]{2,3}" +
                "(-([a-zA-Z]{2}|[0-9]{3}))?)|[\\*\\-]))(:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?!\"#$$%&'" +
                "\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){4}", Pattern.MULTILINE);
    }

    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        Set<String> purlStrings = new HashSet<>();

        for (Component c : sbom.getAllComponents()) {
            for (String cpe : c.getCpes()) {
                purlStrings.add(cpe);
            }
        }

        for (String cpe : purlStrings) {
            if (!this.cpe23Regex.matcher(cpe.strip()).matches()) {
                results.add(new Result(TEST_NAME, Result.STATUS.FAIL, "TODO: MESSAGE"));
            } else {
                results.add(new Result(TEST_NAME, Result.STATUS.PASS, "TODO: MESSAGE"));
            }
        }

        // return findings
        return results;
    }
}
