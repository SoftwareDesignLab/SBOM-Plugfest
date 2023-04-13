package org.nvip.plugfest.tooling;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nvip.plugfest.tooling.qa.QAPipeline;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

public class apiController {

    private static ObjectMapper mapper;

    private static QAPipeline pipeline;

    public apiController() {
        mapper = new ObjectMapper();
        pipeline = new QAPipeline();
    }

    /**
     * USAGE. Send POST request to /api/compare with a list of jackson serialized SBOMs in the body, one per line.
     * The API will respond with an HTTP 200 and a jackson serialized DiffReport object.
     *
     * @param encoded - jackson serialized SBOM on each line
     * @return - jackson serialized DiffReport object
     */
    @RequestMapping(value="compare", method=RequestMethod.POST)
    public ResponseEntity<String> compare(@RequestBody String encoded) {
        ArrayList<SBOM> sboms;

        //decode post body
        try {
            sboms = decode(encoded);
        } catch (JsonMappingException e) {
            return new ResponseEntity<>("Body was not valid SBOM object\n received body was " + encoded, HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Body was not valid JSON\n received body was " + encoded, HttpStatus.BAD_REQUEST);
        }

        //run the DiffReport
        //todo: uncomment when comparer changes are merged.
//        DiffReport report = Comparer.generateReport(sboms.get(0), sboms.subList(1, sboms.size()));

         //encode and send report
//        try {
//            return new ResponseEntity<>(encode(report), HttpStatus.OK);
//        } catch (JsonProcessingException e) {
//            return new ResponseEntity<>("Error processing report", HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        return new ResponseEntity<>("Not implemented", HttpStatus.NOT_IMPLEMENTED); //also remove this!!
    }

    /**
     * USAGE. Send POST request to /api/qa with a single jackson serialized SBOM in the body.
     * The API will respond with an HTTP 200 and a jackson serialized QualityReport object.
     *
     * @param encoded - jackson serialized SBOM object
     * @return - jackson serialized QualityReport object
     */
    @RequestMapping(value="qa", method=RequestMethod.POST)
    public ResponseEntity<String> qa(@RequestBody String encoded) {
        ArrayList<SBOM> sboms;

        //decode post body
        try {
            sboms = decode(encoded);
        } catch (JsonMappingException e) {
            return new ResponseEntity<>("Body was not valid SBOM object\n received body was " + encoded, HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Body was not valid JSON\n received body was " + encoded, HttpStatus.BAD_REQUEST);
        }

        //some basic validation
        if(sboms.size() != 1){
            return new ResponseEntity<>("QA request must contain only one SBOM", HttpStatus.BAD_REQUEST);
        }

        //run the QA
        QualityReport report = pipeline.process(sboms.get(0));

        //encode and send report
        try {
            return new ResponseEntity<>(encode(report), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error processing report", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ArrayList<SBOM> decode(String encoded) throws JsonProcessingException, JsonMappingException {
        ArrayList<SBOM> sboms = new ArrayList<>();
        for(String line : encoded.split("\n")){
            sboms.add(mapper.readValue(line, SBOM.class));
        }

        return sboms;
    }

    private String encode(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

}
