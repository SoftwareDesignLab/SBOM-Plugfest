package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.List;
import java.util.regex.Pattern;

/**
 * For a given SBOM, check if the metadata purl matches regex (if applicable)
 */
public class ValidPURLTest extends MetricTest {

    private final Pattern PURL_REGEX = Pattern.compile("^pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@([a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#([a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);
    @Override
    public List<Result> test(SBOM sbom) {
        return null;
    }
}
