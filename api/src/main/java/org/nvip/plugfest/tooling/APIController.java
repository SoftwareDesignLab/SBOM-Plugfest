package org.nvip.plugfest.tooling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.CompletenessProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;
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
@RequestMapping("plugfest")
public class APIController {

    private static List<SBOM> COMPARE_QUEUE = new ArrayList<>();

    /**
     * Send post request to /updateQueue to add a file to the compare queue
     * todo better endpoint name?
     *
     * @param fileName Name of the file that the SBOM contents came from
     * @param contents File contents of the SBOM file to parse
     * @return Status Message
     */
    @PostMapping("/updateQueue")
    public ResponseEntity<String> updateQueue(@RequestParam("fileName") String fileName, @RequestParam("contents") String contents) {
        SBOM sbom = TranslatorPlugFest.translateContents(contents, fileName);
        try {
            // report failure if failed to parse SBOM
            if (sbom == null)
                return new ResponseEntity<>("Failed to parse SBOM", HttpStatus.BAD_REQUEST);    // todo better status code?
            // Add to queue
            COMPARE_QUEUE.add(sbom);    // todo better way to store sboms? trying to avoid making stateful
            // report success
            return new ResponseEntity<>("SBOM Enqueued", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * USAGE. Send POST request to /compare given SBOM file against the current comparison Queue
     *
     * @param fileName Name of SBOM file
     * @param contents Content of SBOM
     * @return Wrapped Comparison object or error message
     */
    @PostMapping("compare")
    public ResponseEntity<?> compare(@RequestParam("fileName") String fileName, @RequestParam("contents") String contents) throws IOException {

        // Check queue is ready
        if(COMPARE_QUEUE.isEmpty())
            return new ResponseEntity<>("Comparison Queue is empty", HttpStatus.BAD_REQUEST);   // todo better status code?

        // Convert given SBOM file
        SBOM target = TranslatorPlugFest.translateContents(contents, fileName);
        if(target == null)
            return new ResponseEntity<>("Failed to parse SBOM", HttpStatus.BAD_REQUEST);   // todo better status code?

        // Add to queue
        COMPARE_QUEUE.add(0, target);   // todo shouldn't assume head of queue is target

        // Run comparison
        Comparison report = new Comparison(COMPARE_QUEUE); // report to return
        report.runComparison();
        COMPARE_QUEUE.clear();      // flush list

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
     * @param contents - File content of the SBOM to run metrics on
     * @param fileName - Name of the SBOM file
     * @return - wrapped QualityReport object, null if failed
     */
    @PostMapping("qa")
    public ResponseEntity<QualityReport> qa(@RequestParam("contents") String contents, @RequestParam("fileName") String fileName, HttpServletRequest servletRequest) {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
        }
        catch (Exception e) {
            // This will not happen as we are hardcoding UTF-8
            System.out.println("Failed to set encoding");
        }

        SBOM sbom = TranslatorPlugFest.translateContents(contents, fileName);

        // Check if the sbom is null
        if (sbom == null) {
            // todo return why sbom failed to parse, not just null
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // todo get tests/processors from user that they want to run?
        Set<AttributeProcessor> processors = new HashSet<>();
        processors.add(new CompletenessProcessor());

        //run the QA
        QualityReport report = QAPipeline.process(fileName, sbom, processors);

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
     * @param contents File contents of the SBOM file to parse
     * @param fileName Name of the file that the SBOM contents came from
     * @return SBOM object, null if failed to parse
     */
    @PostMapping("parse")
    public ResponseEntity<SBOM> parse(@RequestParam("contents") String contents, @RequestParam("fileName") String fileName) {
        SBOM sbom = TranslatorPlugFest.translateContents(contents, fileName);

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
