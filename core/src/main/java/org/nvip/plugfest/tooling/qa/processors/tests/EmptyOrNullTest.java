package org.nvip.plugfest.tooling.qa.processors.tests;

import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Signature;

import java.util.*;

public class EmptyOrNullTest extends MetricTest {
    private static final String TEST_NAME = "EmptyOrNull";
    
    @Override
    public List<Result> test(SBOM sbom) {

        List<Result> results = new ArrayList<>(testSBOMFields(sbom));

        for(Component c : sbom.getAllComponents()){
            if(c.isUnpackaged()) continue;
            results.addAll(testComponentFields(c));

        }

        return results;
    }

    // test SBOm fields
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

    // test component fields
    private List<Result> testComponentFields(Component c){

        List<Result> results = new ArrayList<>();
        Result r;
        r = resultEmptyOrNull(c.getName());
        r.addContext(c,"name");
        results.add(r);

        r = resultEmptyOrNull(c.getPublisher());
        r.addContext(c,"publisher");
        results.add(r);

        r = resultEmptyOrNull(c.getVersion());
        r.addContext(c,"version");
        results.add(r);

        if(c.getLicenses() == null){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");
            r.addContext(c,"licenses");
            results.add(r);
        } else {
            for(String l : c.getLicenses()){
                r = resultEmptyOrNull(l);
                r.addContext(c,"license");
                results.add(r);
            }
        }

        if(c.getCpes() == null){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");
            r.addContext(c,"cpes");
            results.add(r);
        } else {
            for(String l : c.getCpes()){
                r = resultEmptyOrNull(l);
                r.addContext(c,"cpe");
                results.add(r);
            }
        }

        if(c.getPurls() == null){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");
            r.addContext(c,"purls");
            results.add(r);
        } else {
            for(PURL p : c.getPurls()){
                r = resultEmptyOrNull(p);
                r.addContext(c,"purl");
                results.add(r);
            }
        }

        if(c.getSwids() == null){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Null");
            r.addContext(c,"swids");
            results.add(r);
        } else {
            for(String s : c.getSwids()){
                r = resultEmptyOrNull(s);
                r.addContext(c,"swid");
                results.add(r);
            }
        }

        r = resultEmptyOrNull(c.getUniqueID());
        r.addContext(c,"uniqueID");
        results.add(r);

        return results;
    }


    // check if object is empty or not
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
