package plugfest.tooling.qa.processors;

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
            switch (extn) {
                case "json" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.JSON);
                case "rdf.xml", "rdf" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.RDFXML);
                case "xml" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.XML);
                case "xls" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.XLS);
                case "xlsx" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.XLSX);
                case "yaml" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.YAML);
                case "tag", "spdx" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.TAG);
                case "rdf.ttl" -> verificationResults = (ArrayList<String>)Verify.verify(filePath, SpdxToolsHelper.SerFileType.RDFTTL);
            }
        }
        catch(Exception ex) {
            System.out.println("EXCEPTION: "+ex);
            return null;
        }
        return verificationResults;
    }

}
