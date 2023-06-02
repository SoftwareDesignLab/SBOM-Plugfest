package org.nvip.plugfest.tooling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.nvip.plugfest.tooling.differ.DiffReport;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.CompletenessProcessor;
import org.nvip.plugfest.tooling.qa.processors.UniquenessProcessor;
import org.nvip.plugfest.tooling.qa.processors.RegisteredProcessor;
import org.nvip.plugfest.tooling.qa.processors.LicensingProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * File: APIController.java
 * REST API Controller for SBOM Comparison and QA
 *
 * @author Juan Francisco Patino
 * @author Asa Horn
 * @author Justin Jantzi
 * @author Derek Garcia
 */
@RestController
@RequestMapping("/plugfest")
public class APIController {

    /**
     * Utility Class for sending SBOM JSON objects
     * todo better name?
     */
    private static class SBOMArgument{
        @JsonProperty
        private String fileName;
        @JsonProperty
        private String contents;
    }

    /**
     * USAGE. Send POST request to /compare with a collection of SBOM Json objects and a selected target
     *
     * @param targetIndex index of the target SBOM
     * @param sboms collection of SBOMs to compare
     * @return Wrapped Comparison object or error message
     */
    @PostMapping("/compare")
    public ResponseEntity<?> compare(
            @RequestParam("targetIndex") Integer targetIndex,
            @RequestBody SBOMArgument[] sboms)
    {
        // Attempt to load comparison queue
        List<SBOM> compareQueue = new ArrayList<>();
        for(SBOMArgument sbom : sboms){
            try{
                compareQueue.add(TranslatorPlugFest.translateContents(sbom.contents, sbom.fileName));
            } catch (Exception e){
                return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);  // todo better status code?
            }
        }
        // Get and remove target from queue
        SBOM targetSBOM = compareQueue.get(targetIndex);
        compareQueue.remove(targetIndex);

        // Run comparison
        DiffReport dr = new DiffReport(sboms[targetIndex].fileName, targetSBOM);

        // Compare against all sboms in the queue
        for(int i = 0; i < compareQueue.size(); i++)
            dr.compare(sboms[i].fileName, compareQueue.get(i));



        //encode and send report
        try {
            return new ResponseEntity<>(dr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * USAGE. Send POST request to /qa with a single sbom file
     * The API will respond with an HTTP 200 and a serialized report in the body.
     *
     * @param servletRequest
     * @param sbomArgument JSON object of sbom details
     * @return - wrapped QualityReport object, null if failed
     */
    @PostMapping("/qa")
    public ResponseEntity<QualityReport> qa(
            HttpServletRequest servletRequest,
            @RequestBody SBOMArgument sbomArgument)
    {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {
            // This will not happen as we are hardcoding UTF-8
            System.out.println("Failed to set encoding");
        }

        SBOM sbom = TranslatorPlugFest.translateContents(sbomArgument.contents, sbomArgument.fileName);

        // Check if the sbom is null
        if (sbom == null) {
            // todo return why sbom failed to parse, not just null
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // todo get tests/processors from user that they want to run?
        Set<AttributeProcessor> processors = new HashSet<>();
        processors.add(new CompletenessProcessor());
        processors.add(new UniquenessProcessor());
        processors.add(new RegisteredProcessor());
        processors.add(new UniquenessProcessor());
        processors.add(new RegisteredProcessor());
        processors.add(new LicensingProcessor());
        // add metadata processor
        // add registered processor
        // add cdx metrics
        // add spdx metrics

        //run the QA
        QualityReport report = QAPipeline.process(sbomArgument.fileName, sbom, processors);

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Send post request to /parse and it will convert the file contents to an SBOM object, returns null if failed to parse
     * todo make translation a private method that calls use?
     * @param sbomArgument JSON object of sbom details
     * @return SBOM object, null if failed to parse
     */
    @PostMapping("/parse")
    public ResponseEntity<SBOM> parse(@RequestBody SBOMArgument sbomArgument)
    {
        SBOM sbom = TranslatorPlugFest.translateContents(sbomArgument.contents, sbomArgument.fileName);

        try {
            // Explicitly return null if failed
            if (sbom == null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(sbom, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

