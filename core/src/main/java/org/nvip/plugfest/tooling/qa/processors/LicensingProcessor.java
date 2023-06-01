package org.nvip.plugfest.tooling.qa.processors;


import org.nvip.plugfest.tooling.qa.tests.HasLicenseDataTest;
import org.nvip.plugfest.tooling.qa.tests.ValidSPDXLicenseTest;

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
        this.metricTests.add(new ValidSPDXLicenseTest());
    }
}