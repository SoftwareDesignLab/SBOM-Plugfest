package org.nvip.plugfest.tooling.qa.processors;

/**
 * Imports SPDX Tools
 */

import org.spdx.tools.SpdxToolsHelper;
import org.spdx.tools.Verify;

import java.util.ArrayList;

public class SPDXProcessor extends SchemaProcessor{
    /*
    todo
    check valid extension
    File conforms to schema
    Any schema specific tests
     */
    public ArrayList<String> testVerifySPDX(String filePath) {
        System.out.println("Running Verification on SPDX SBOM File: " + filePath);
        ArrayList<String> verificationResults = new ArrayList<>();
        final String extn = filePath.substring(filePath.indexOf('.'));
        try {
            SpdxToolsHelper.SerFileType fileType = null;
            switch (extn) {
                case "json" -> fileType = SpdxToolsHelper.SerFileType.JSON;
                case "rdf.xml", "rdf" -> fileType = SpdxToolsHelper.SerFileType.RDFXML;
                case "xml" -> fileType = SpdxToolsHelper.SerFileType.XML;
                case "xls" -> fileType = SpdxToolsHelper.SerFileType.XLS;
                case "xlsx" -> fileType = SpdxToolsHelper.SerFileType.XLSX;
                case "yaml" -> fileType = SpdxToolsHelper.SerFileType.YAML;
                case "tag", "spdx" -> fileType = SpdxToolsHelper.SerFileType.TAG;
                case "rdf.ttl" -> fileType = SpdxToolsHelper.SerFileType.RDFTTL;
            }

            if (fileType == null) {
                throw new Exception("Unsupported SPDX File Type");
            }
            verificationResults = (ArrayList<String>)Verify.verify(filePath, fileType);
        }
        catch(Exception ex) {
            System.out.println("EXCEPTION: " + ex);
            return null;
        }
        return verificationResults;
    }

}
