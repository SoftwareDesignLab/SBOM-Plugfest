package org.nvip.plugfest;

import plugfest.tooling.differ.Comparer;
import plugfest.tooling.differ.DiffReport;
import plugfest.tooling.qa.QAPipeline;
import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.SBOM;
import plugfest.tooling.translator.TranslatorSPDX;
import plugfest.tooling.translator.TranslatorSVIP;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final int MIN_ARGS = 2;

    private static final int MAX_ARGS = 3;

    /**
     * FileName: Main.java
     * Receives a command and one to two files.
     * Depending on the command, the app will do one of the following:
     *  -q : A quality check (for one file only)
     *  -m : A metrics run (for one file only))
     *  -d : A diff comparison (for two files only)
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < MIN_ARGS) {
            System.out.println(
                    "Too few arguments. Requires: a common, and one to two files.\n" +
                            "Example: tool.java -q users/admin/files/1.xml users/admin/files/2.xml");
            System.exit(0);
        } else if (args.length > MAX_ARGS) {
            System.out.println(
                    "Too many arguments. Requires: a common, and one to two files.\n" +
                            "Example: tool.java -q users/admin/files/1.xml users/admin/files/2.xml");
            System.exit(0);
        }

        if (!new File(args[1]).exists()) {
            System.out.println("Second file can not be found. Exiting...");
            System.exit(0);
        }

        try {
            // Parse SBOM Object from file
            SBOM sbom = TranslatorSPDX.translatorSPDX(args[1]);

            // Ensure SBOM Object was parsed without fail
            assert sbom != null;

            // Instantiate QA Pipeline
            QAPipeline qa = new QAPipeline();
            QualityReport qualityReport = qa.process(sbom);

            // Display QualityReport
            System.out.println(qualityReport.toString());

        } catch (IOException ignored) {
            System.out.println("Error translating file.");
            System.exit(-1);
        }

        /*
        1. new translator t
        2. SBOM[] sboms = t.parse("path/to/sboms/dir");
        3. QAPipeline qaPipeline = new QAPipeline();
        4.
            for( sbom : sboms )
                qaPipeline.process(sbom);   // apply metrics
        // not sure if this is how the next part works, but rough idea
        5. Differ diff = new Differ();
        6. diff.compare(sboms); // generate conflict objects
        7. return new Report(sboms);    // use SBOM data to turn into a report to be exported via file/used by front end
         */

        if (args[0].contains("-q")) {
            // Parse SBOM Object from file
            SBOM sbom = TranslatorSVIP.translate(args[1]);

            // Ensure SBOM Object was parsed without fail
            assert sbom != null;

            // Instantiate QA Pipeline
            QAPipeline qa = new QAPipeline();
            QualityReport qualityReport = qa.process(sbom);

            // Display QualityReport
            System.out.println(qualityReport.toString());
        } else if (args[0].contains("-m")) {
//            Metrics metrics = new Metrics(args[1]);
//            metrics.verify(args[1]);

        } else if (args[0].contains("-d")) {
            if (!new File(args[2]).exists()) {
                System.out.println("Second file can not be found. Exiting...");
                System.exit(0);
            }

            // Process first sbom
            SBOM sbomOne = TranslatorSVIP.translate(args[1]);
            SBOM sbomTwo = TranslatorSVIP.translate(args[2]);

            if (sbomOne == null || sbomTwo == null) {
                System.err.println("One or more of the SBOMs could not be parsed. Exiting...");
            } else {
                DiffReport report = Comparer.generateReport(sbomOne, sbomTwo);
                System.out.println(report);
            }

        } else {
            System.out.println("Invalid command given. Should be '-q' for quality check, '-m' for metrics, or 'd' for diff.");
        }
    }
}