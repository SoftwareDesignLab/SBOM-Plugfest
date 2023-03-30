package plugfest.tooling.metrics;

/**
 * Imports SPDX Tools
 */

import org.spdx.tools.CompareSpdxDocs;
import org.spdx.tools.Verify;
import org.spdx.tools.SpdxToolsHelper;

/**
 * Imports Java Native Libraries
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Class for SPDX SBOM Metrics
 */
public class SPDXMetrics extends Metric{
    

    private String filepath;
    private final String sbom;

    public SPDXMetrics(String _filepath, String _sbom) {
        this.filepath = _filepath;
        this.sbom = _sbom;
        this.score += this.testMetric();
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    public void compare(String[] sbom_files) {
        System.out.println("Running Comparison on SBOM Files: "+ Arrays.toString(sbom_files));
        Date date = new Date();
        long timestamp = date.getTime();
        String[] compareArgs = new String[(sbom_files.length+1)];
        String outputFile = "comparison_"+String.valueOf(timestamp)+".xlsx";
        compareArgs[0] = outputFile;
        for(int i = 0; i < sbom_files.length; i++) {
            compareArgs[i+1] = this.filepath+"/"+sbom_files[i];
        }

        CompareSpdxDocs.main(compareArgs);
    }

    public ArrayList<String> verifySPDX() {
        final String fullPath = this.filepath + "/" + this.sbom;
        System.out.println("Running Verification on SPDX SBOM File: " + fullPath);
        ArrayList<String> verificationResults = new ArrayList<>();
        final String extn = fullPath.substring(fullPath.indexOf('.'));
        try {
            switch (extn) {
                case "json" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.JSON);
                case "rdf.xml", "rdf" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.RDFXML);
                case "xml" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.XML);
                case "xls" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.XLS);
                case "xlsx" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.XLSX);
                case "yaml" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.YAML);
                case "tag", "spdx" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.TAG);
                case "rdf.ttl" -> verificationResults = (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.SerFileType.RDFTTL);
            }
        }
        catch(Exception ex) {
            System.out.println("EXCEPTION: "+ex);
            return null;
        }
        return verificationResults;
    }

    @Override
    protected int testMetric() {
        if(verifySPDX() != null){
            return 1;
        }
        else {
            return 0;
        }
    }
}
