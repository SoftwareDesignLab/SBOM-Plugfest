package org.nvip.plugfest.tooling.qa.processors.tests;

import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Signature;

import java.util.*;

public class EmptyOrNullTest extends MetricTest {
    private static final String TEST_NAME = "EmptyOrNull";
    
    @Override
    public List<Result> test(SBOM sbom) {

        List<Result> results = new ArrayList<>(testSBOMFields(sbom));

//        for(Component c : sbom.getAllComponents()){
//            if(c.isUnpackaged()) continue;
//
//            results.add(testField(c.getName(), "name"));
//            results.add(testField(c.getPublisher(), "publisher"));
//            results.add(testField(c.getVersion(), "version"));
//            results.addAll(collectionEmptyOrNull(c.getLicenses()));
//            results.addAll(collectionEmptyOrNull(c.getCpes()));
//            results.addAll(collectionEmptyOrNull(c.getPurls()));
//            results.addAll(collectionEmptyOrNull(c.getSwids()));
//            results.add(testField(c.getUniqueID(), "uniqueID"));
//
//        }

        return results;
    }
    
    private List<Result> testSBOMFields(SBOM sbom){
        List<Result> results = new ArrayList<>();
        Result r;
        r = resultEmptyOrNull(sbom.getSpecVersion());
        r.addContext(sbom,"specVersion");
        results.add(r);

        r = resultEmptyOrNull(sbom.getSerialNumber());
        r.addContext(sbom,"serialNumber");
        results.add(r);

        r = resultEmptyOrNull(sbom.getSupplier());
        r.addContext(sbom,"supplier");
        results.add(r);

        r = resultEmptyOrNull(sbom.getTimestamp());
        r.addContext(sbom,"timestamp");
        results.add(r);

        if(sbom.getSignature() == null){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");
            r.addContext(sbom,"signatures");
            results.add(r);

        } else if (sbom.getSignature().isEmpty()) {
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Signatures are empty");
            r.addContext(sbom,"signatures");
            results.add(r);
        } else {
            for(Signature s : sbom.getSignature()){
                r = resultEmptyOrNull(s);
                r.addContext(sbom,"signature");
                results.add(r);
            }
        }

        return results;
    }

    
    private Result resultEmptyOrNull(Object o){
        if(o == null)
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");

        if(o instanceof String){
            if(!o.equals("")) {
                Result r = new Result(TEST_NAME, Result.STATUS.PASS, "Value is not an empty string");
                r.updateInfo(Result.Context.STRING_VALUE, o.toString());
                return r;
            }
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is an empty string");
        }

        return new Result(TEST_NAME, Result.STATUS.PASS, "Value is not null");
    }
}
