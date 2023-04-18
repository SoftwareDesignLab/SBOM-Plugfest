package org.nvip.plugfest.tooling;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class APIController {

    private static ObjectMapper mapper;

    private static QAPipeline pipeline;

    public APIController() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        pipeline = new QAPipeline();
    }

    /**
     * USAGE. Send POST request to /compare with a list of jackson serialized SBOMs in the body, one per line.
     * The API will respond with an HTTP 200 and a jackson serialized DiffReport object.
     *
     * @param boms - list of files to compare
     * @return - jackson serialized DiffReport object
     */
    @RequestMapping(value="compare", method=RequestMethod.POST)
    public ResponseEntity<Comparison> compare(@RequestBody List<MultipartFile> boms) throws IOException {
        // Convert the SBOMs to SBOM objects
        ArrayList<SBOM> sboms = new ArrayList<>();

        for (MultipartFile file: boms) {
            // Get contents of the file
            String contents = new String(file.getBytes(), StandardCharsets.UTF_8);

            sboms.add(TranslatorPlugFest.translateContents(contents, file.getOriginalFilename()));
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
     * @return - jackson serialized QualityReport object
     */
    @RequestMapping(value="qa", method=RequestMethod.POST)
    public ResponseEntity<QualityReport> qa(@RequestBody MultipartFile bom) {
        // Get file contents into a string
        String contents;
        try {
            contents = new String(bom.getBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SBOM sbom = TranslatorPlugFest.translateContents(contents, bom.getOriginalFilename());

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
