package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.qa.processors.MetadataProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.List;

/**
 * file: HasMetaDataTest.java
 *
 * Test an SBOM to see if its metadata is present
 * @author Matthew Morrison
 */
public class HasMetadataTest extends MetricTest {
    @Override
    public List<Result> test(SBOM sbom) {

        return null;
    }
}
