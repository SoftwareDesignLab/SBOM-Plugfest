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
import java.io.File;
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
        ArrayList<String> verificationResults = null;
        try {
            verificationResults =
                (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.fileToFileType(new File(fullPath)));
        }
        catch(Exception ex) { System.out.println("Error Verifying SPDX: " + ex); }
        return verificationResults;
    }

    @Override
    protected int testMetric() {
        return verifySPDX() != null ? 1 : 0;
    }
}
