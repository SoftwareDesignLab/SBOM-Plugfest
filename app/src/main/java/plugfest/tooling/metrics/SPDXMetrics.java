package plugfest.tooling.metrics;

/**
 * Imports SPDX Tools
 */

import org.spdx.tools.CompareSpdxDocs;
import org.spdx.tools.Verify;

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
        System.out.println("Running Comparison on SBOM Files: "+ sbom_files);
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

    @Override
    protected int testMetric() {
        final String fullPath = this.filepath + "/" + this.sbom;
        System.out.println("Running Verification on SPDX SBOM File: " + this.filepath);
        Verify.main(new String[]{ fullPath });
        //Have a console reader verify and return 1 or 0 depending on results
        return 1;
    }
}
