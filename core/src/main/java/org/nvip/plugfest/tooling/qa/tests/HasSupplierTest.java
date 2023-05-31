package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

/**
 * file: HasSupplierTest.java
 *
 * Test an SBOM's metadata if there is information on the supplier
 * or author of the SBOM
 * @author Matthew Morrison
 */
public class HasSupplierTest extends MetricTest{

    private static final String TEST_NAME = "HasSupplier";

    /**
     * Test a given SBOM if there is information present about the
     * supplier/author of the SBOM
     * @param sbom SBOM to test
     * @return the result of checking for the SBOM supplier information
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> result = new ArrayList<>();
        Result r;
        // check if the sbom's supplier is empty or null
        // if so, information is missing, test fails
        if(isEmptyOrNull(sbom.getSupplier())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Metadata does " +
                    "not have supplier information");
        }
        // information about sbom's supplier is present, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "Metadata has " +
                    "supplier information");
        }
        r.addContext(sbom, "Metadata Supplier Information");
        result.add(r);

        return result;
    }
}
