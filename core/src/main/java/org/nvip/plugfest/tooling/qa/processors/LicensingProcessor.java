package org.nvip.plugfest.tooling.qa.processors;


import org.nvip.plugfest.tooling.qa.tests.HasLicenseDataTest;

/**
 * file: LicensingProcessor.java
 *
 * Collection of tests for components' licenses
 * @author Matthew Morrison
 */
public class LicensingProcessor extends AttributeProcessor{

    /**
     * Create new preset collection of tests
     */
    public LicensingProcessor(){
        this.attributeName = "Licensing";
        this.metricTests.add(new HasLicenseDataTest());
        /*
            TODO
             hasLicenseData: Check if component has license data
             validSPDXLicense: check if existing spdx license
         */
    }
}
