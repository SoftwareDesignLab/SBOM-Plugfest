package org.nvip.plugfest.tooling.qa.processors;


import org.xml.sax.helpers.AttributesImpl;

/**
 * file: RegisteredProcessor.java
 *
 * Collection of tests to ensure the SBOM's package manager are registered\
 * @author Matthew Morrison
 */
public class RegisteredProcessor extends AttributeProcessor{
    public RegisteredProcessor(){
        // todo
        // isRegistered: Check packagemanager using purl to look for value, just a 200 return
    }
}
