package org.nvip.plugfest.tooling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.nvip.plugfest.tooling.differ.DiffReport;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.CompletenessProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorException;
import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
import org.nvip.plugfest.tooling.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.nvip.plugfest.tooling.utils.Utils.*;

import java.util.*;

/**
 * File: APIController.java
 * REST API Controller for SBOM Comparison and QA
 *
 * @author Juan Francisco Patino
 * @author Asa Horn
 * @author Justin Jantzi
 * @author Derek Garcia
 * @author Ian Dunn
 */
@RestController
@RequestMapping("/plugfest")
public class APIController {

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
            @RequestBody SBOMFile[] sboms)
    {
        // null/empty sboms check
        int nullCheck = Utils.sbomFileArrNullCheck(sboms);
        if(nullCheck > -1)
            return new ResponseEntity<>("Invalid SBOM at index " + nullCheck + ".",
                    HttpStatus.BAD_REQUEST);

        if (sboms.length < 2) return new ResponseEntity<>("SBOM array must contain at least 2 elements to compare.",
                HttpStatus.BAD_REQUEST);

        if (targetIndex < 0 || targetIndex > sboms.length - 1) return new ResponseEntity<>("Target Index out of " +
                "bounds (must be between 0 and " + (sboms.length - 1) + ", was " + targetIndex + ").", HttpStatus.BAD_REQUEST);

        // Attempt to load comparison queue
        List<SBOM> compareQueue = new ArrayList<>();
        for (SBOMFile sbom : sboms){
            try {
                compareQueue.add(TranslatorPlugFest.translateContents(sbom.contents, sbom.fileName));
            } catch (TranslatorException e){
                return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
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
     * @param sbomFile JSON object of sbom details
     * @return - wrapped QualityReport object, null if failed
     */
    @PostMapping("/qa")
    public ResponseEntity<?> qa(
            HttpServletRequest servletRequest,
            @RequestBody Utils.SBOMFile sbomFile)
    {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {
            // This will not happen as we are hardcoding UTF-8
            Debug.log(Debug.LOG_TYPE.ERROR, "Failed to set encoding");
        }

        SBOM sbom;

        try {
            sbom = TranslatorPlugFest.translateContents(sbomFile.contents, sbomFile.fileName);
        } catch (TranslatorException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // todo get tests/processors from user that they want to run?
        Set<AttributeProcessor> processors = new HashSet<>();
        processors.add(new CompletenessProcessor());

        //run the QA
        QualityReport report = QAPipeline.process(sbomFile.fileName, sbom, processors);

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Send post request to /parse and it will convert the file contents to an SBOM object, returns null if failed to parse
     *
     * @param sbomFile JSON object of sbom details
     * @return SBOM object, null if failed to parse
     */
    @PostMapping("/parse")
    public ResponseEntity<?> parse(@RequestBody SBOMFile sbomFile)
    {
        SBOM sbom;

        try {
            sbom = TranslatorPlugFest.translateContents(sbomFile.contents, sbomFile.fileName);
        } catch (TranslatorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO better status code?
        }

        try {
            return new ResponseEntity<>(sbom, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

