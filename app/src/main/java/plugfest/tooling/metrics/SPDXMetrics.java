package plugfest.tooling.metrics;

/**
 * Imports SPDX Tools
 */
import org.spdx.tools.CompareSpdxDocs;
import org.spdx.tools.Verify;

/**
 * Imports Native Java Libraries
 */
import java.util.Date;

/**
 * Class for SPDX SBOM Metrics
 */
public class SPDXMetrics extends Metric{

    private int score;
    public int getScore() { return this.score; }

    private String filepath;

    public SPDXMetrics(String _filepath) {
        this.filepath = _filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    public void compare(String[] sbom_files) {
        System.out.println("Running Comparison on SBOM Files: "+sbom_files);
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

    public void verify(String sbom){
        int score = 0;
        System.out.println("Running Verification on SPDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        String[] sboms = { sbom_file };
        Verify.main(sboms);
    }

    @Override
    protected int testMetric() {
        return 0;
    }

    protected int testMetric(String sbom) {
        verify(sbom);
        //Have a console reader verify and return 1 or 0 depending on results
        score += 1;
        return score;
    }
}
