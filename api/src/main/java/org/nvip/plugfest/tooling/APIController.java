package org.nvip.plugfest.tooling;

import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * File: APIController.java
 * REST API Controller for SBOM Comparison and QA
 *
 * @author Juan Francisco Patino, Asa Horn
 */
@RestController
@RequestMapping("plugfest")
public class APIController {

    private static QAPipeline pipeline;

    public APIController() {
        pipeline = new QAPipeline();
    }

    /**
     * USAGE. Send POST request to /compare with a list of jackson serialized SBOMs in the body, one per line.
     * The API will respond with an HTTP 200 and a jackson serialized DiffReport object.
     *
     * @param boms - list of files to compare
     * @return - wrapped Comparison object
     */
    @PostMapping("compare")
    public ResponseEntity<Comparison> compare(@RequestParam("contents") List<String> contents, @RequestParam("filenames") List<String> filenames) throws IOException {
        // Convert the SBOMs to SBOM objects
        ArrayList<SBOM> sboms = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {
            // Get contents of the file
            sboms.add(TranslatorPlugFest.translateContents(contents.get(i), filenames.get(i)));
        }

        if(sboms.size() < 2){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Comparison report = new Comparison(sboms); // report to return
        report.runComparison();

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * USAGE. Send POST request to /qa with a single jackson serialized SBOM in the body.
     * The API will respond with an HTTP 200 and a jackson serialized QualityReport object.
     *
     * @param bom - SBOM to run metrics on
     * @return - wrapped QualityReport object
     */
    @PostMapping("qa")
    public ResponseEntity<QualityReport> qa(@RequestParam("contents") String contents, @RequestParam("fileName") String fileName) {

        SBOM sbom = TranslatorPlugFest.translateContents(contents, fileName);

        //run the QA
        QualityReport report = pipeline.process(sbom);

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
