package org.nvip.plugfest.tooling.qa.processors;


import org.nvip.plugfest.tooling.qa.tests.IsRegisteredTest;


/**
 * file: RegisteredProcessor.java
 *
 * Collection of tests to ensure the SBOM's package manager are registered\
 * @author Matthew Morrison
 */
public class RegisteredProcessor extends AttributeProcessor{
    public RegisteredProcessor(){
        this.attributeName = "Registered";
        this.metricTests.add(new IsRegisteredTest());
    }
}
