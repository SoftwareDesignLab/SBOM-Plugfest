package org.nvip.plugfest.tooling.metrics;

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
 *
 * @author (names)
 */
public class SPDXMetrics extends Metric{

    /**
     * The filepath of the SBOM file.
     */
    private String filepath;

    /**
     * The text of the SBOM file.
     */
    private final String sbom;

    /**
     * Constructor for SPDXMetrics
     * @param filepath - the filepath of the SBOM file
     * @param sbom - the text of the SBOM file
     */
    public SPDXMetrics(String filepath, String sbom) {
        this.filepath = filepath;
        this.sbom = sbom;
        this.score += this.testMetric();
    }

    /**
     * Method that compares two+ SPDX SBOM files.
     * @param sbom_files - the SPDX SBOM files to be compared. The baseline SBOM file should be the first file in the array.
     */
    public void compare(String[] sbom_files) {
        System.out.println("Running Comparison on SBOM Files: "+ Arrays.toString(sbom_files));

        //get the timestamp for the filename
        Date date = new Date();
        long timestamp = date.getTime();

        //create the arguments for the comparison
        String[] compareArgs = new String[(sbom_files.length+1)];


        String outputFile = "comparison_"+String.valueOf(timestamp)+".xlsx";
        compareArgs[0] = outputFile; //the first argument is the output file

        //the rest of the arguments are the SPDX files to be compared
        for(int i = 0; i < sbom_files.length; i++) {
            compareArgs[i+1] = this.filepath+"/"+sbom_files[i];
        }

        //run the comparison
        CompareSpdxDocs.main(compareArgs);
    }

    /**
     * Method that verifies whether a provided SPDX SBOM file is valid according to the SPDX SBOM file format schema.
     * This method relies on SPDXMetrics being instantiated with a valid SPDX SBOM file.
     *
     * @return - an ArrayList of verification results.
     */
    public ArrayList<String> verifySPDX() {
        //setup
        final String fullPath = this.filepath + "/" + this.sbom;
        System.out.println("Running Verification on SPDX SBOM File: " + fullPath);
        ArrayList<String> verificationResults = null;

        //todo: call the verify method from <official SPDX tools?>
        try {
            verificationResults =
                (ArrayList<String>)Verify.verify(fullPath, SpdxToolsHelper.fileToFileType(new File(fullPath)));
        }
        catch(Exception ex) { System.out.println("Error Verifying SPDX: " + ex); }
        return verificationResults;
    }

    ///
    /// getters and setters
    ///

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    ///
    /// overrides
    ///

    @Override
    protected int testMetric() {
        return verifySPDX() != null ? 1 : 0;
    }
}
