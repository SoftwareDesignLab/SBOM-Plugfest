/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.nvip.plugfest.tooling.differ.DiffReport;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.*;
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
    public ResponseEntity<?> compare(@RequestParam("targetIndex") Integer targetIndex, @RequestBody SBOMFile[] sboms)
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
        // Get target from queue
        SBOM targetSBOM = compareQueue.get(targetIndex);

        // Run comparison
        DiffReport dr = new DiffReport(sboms[targetIndex].fileName, targetSBOM);

        // Compare against all sboms in the queue
        for(int i = 0; i < compareQueue.size(); i++){
            // skip target
            if(targetSBOM.equals(compareQueue.get(i)))
                continue;
            dr.compare(sboms[i].fileName, compareQueue.get(i));
        }


        //encode and send report
        return Utils.encodeResponse(dr);
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
    public ResponseEntity<?> qa(HttpServletRequest servletRequest, @RequestBody SBOMFile sbomFile) {
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
        processors.add(new UniquenessProcessor());
        processors.add(new RegisteredProcessor());
        processors.add(new LicensingProcessor());   // Add origin specific processors

        // Add CDX processor if relevant
        if(sbom.getOriginFormat() == SBOM.Type.CYCLONE_DX)
            processors.add(new CDXMetricsProcessor());

        // Add SPDX Processor if relevant
        if(sbom.getOriginFormat() == SBOM.Type.SPDX)
            processors.add(new SPDXMetricsProcessor());

        //run the QA
        QualityReport report = QAPipeline.process(sbomFile.fileName, sbom, processors);

        //encode and send report
        return Utils.encodeResponse(report);
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

        return Utils.encodeResponse(sbom);
    }
}

