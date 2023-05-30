package org.nvip.plugfest.tooling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.CompletenessProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorException;
import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        // todo check parameters so that we can return BAD_REQUEST?

        // Attempt to load comparison queue
        List<SBOM> compareQueue = new ArrayList<>();
        for(SBOMArgument sbom : sboms){
            try{
                compareQueue.add(TranslatorPlugFest.translateContents(sbom.contents, sbom.fileName));
            } catch (TranslatorException e){
                return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Run comparison
        Comparison report = new Comparison(targetIndex, compareQueue); // report to return
        report.runComparison();

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
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
    public ResponseEntity<?> qa(
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

        SBOM sbom;

        try {
            sbom = TranslatorPlugFest.translateContents(sbomArgument.contents, sbomArgument.fileName);
        } catch (TranslatorException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Check if the sbom is null
        if (sbom == null) {
            // todo return why sbom failed to parse, not just null
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // todo get tests/processors from user that they want to run?
        Set<AttributeProcessor> processors = new HashSet<>();
        processors.add(new CompletenessProcessor());

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
    public ResponseEntity<?> parse(@RequestBody SBOMArgument sbomArgument)
    {
        SBOM sbom;

        try {
            sbom = TranslatorPlugFest.translateContents(sbomArgument.contents, sbomArgument.fileName);
        } catch (TranslatorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO better status code?
        }

        try {
            // Explicitly return null if failed
            // TODO translateContents should only fail, not return null. Is this
            //  still needed?
            if (sbom == null) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(sbom, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

