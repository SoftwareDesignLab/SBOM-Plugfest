package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * file: HasMetaDataTest.java
 *
 * Test an SBOM to see if its metadata is present
 * @author Matthew Morrison
 */
public class HasMetadataTest extends MetricTest {

    private static final String TEST_NAME = "HasMetadata";

    /**
     * Test a given SBOM if its metadata is present
     * @param sbom SBOM to test
     * @return the result of checking for the metadata
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> result = new ArrayList<>();

        // get the head components UUID and attempt to get component
        UUID head_UUID = sbom.getHeadUUID();

        Result r;
        // if there is no metadata (no head component, UUID is null),
        // test fails
        if(isEmptyOrNull(head_UUID)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Metadata is " +
                    "not present");
        }
        // metadata was present, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "Metadata is " +
                    "present");
        }
        r.addContext(sbom, "Metadata Presence");
        r.updateInfo(Result.Context.FIELD_NAME, "Metadata");
        // add result to list and return the result
        result.add(r);
        return result;
    }
}
