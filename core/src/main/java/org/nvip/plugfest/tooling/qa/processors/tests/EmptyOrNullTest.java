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
        List<Result> results = new ArrayList<>();

        results.add(resultEmptyOrNull(sbom.getSpecVersion()));
        results.add(resultEmptyOrNull(sbom.getSpecVersion()));
        results.add(resultEmptyOrNull(sbom.getSerialNumber()));
        results.add(resultEmptyOrNull(sbom.getSupplier()));
        results.add(resultEmptyOrNull(sbom.getTimestamp()));

        results.addAll(collectionEmptyOrNull(sbom.getSignature()));

        for(Component c : sbom.getAllComponents()){
            if(c.isUnpackaged()) continue;

            results.add(resultEmptyOrNull(c.getName()));
            results.add(resultEmptyOrNull(c.getPublisher()));
            results.add(resultEmptyOrNull(c.getVersion()));
            results.addAll(collectionEmptyOrNull(c.getLicenses()));
            results.addAll(collectionEmptyOrNull(c.getCpes()));
            results.addAll(collectionEmptyOrNull(c.getPurls()));
            results.addAll(collectionEmptyOrNull(c.getSwids()));
            results.add(resultEmptyOrNull(c.getUniqueID()));

        }

        return results;
    }

    private List<Result> collectionEmptyOrNull(Object collection){
        List<Result> results = new ArrayList<>();

        if(!(collection instanceof List) && !(collection instanceof Set)){
            results.add(resultEmptyOrNull(collection));
            return results;
        }

        for(Object o : ((Collection<?>) collection).toArray())
            results.addAll(collectionEmptyOrNull(o));

        return results;
    }
    
    
    private Result resultEmptyOrNull(Object o){
        if(o == null)
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");

        if(o instanceof String){
            if(!o.equals("")) {
                Result r = new Result(TEST_NAME, Result.STATUS.PASS, "Value is not an empty string");
                r.updateInfo("value", o.toString());
                return r;
            }
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is an empty string");
        }

        return new Result(TEST_NAME, Result.STATUS.PASS, "Value is not null");
    }
}
