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

import java.io.File;
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

        ArrayList<SBOM> sboms = multiFilesToSboms(boms);

        if(sboms.size() < 2){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SBOM target = sboms.get(0); // target SBOM

        Comparison report = new Comparison(target); // report to return

        report.runComparison(sboms);

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Helper function to convert a list of files to a list of SBOM objects
     *
     * @param boms - list of files to compare
     * @return a list of SBOM objects created from the files
     */
    private static ArrayList<SBOM> multiFilesToSboms(List<MultipartFile> boms) {
        ArrayList<SBOM> sboms = new ArrayList<>();

        int i = 0;
        try{
            for (MultipartFile file: boms
            ) {

                // write contents to temp file
                String originalName = file.getOriginalFilename();
                assert originalName != null;
                String extension = originalName.substring(originalName.toLowerCase().lastIndexOf('.'));
                String path = System.getProperty("user.dir") + "/tmp" + i + extension;

                // read and add to SBOM list
                FileWriter w = new FileWriter(path);
                w.write(new String(file.getBytes(), StandardCharsets.UTF_8));
                sboms.add(TranslatorPlugFest.translate(path));

                // delete temporary file
                if (!Files.deleteIfExists(Paths.get(path)))
                    System.out.println("Failed to delete " + path);
                i++;
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return sboms;
    }

    /**
     * USAGE. Send POST request to /qa with a single jackson serialized SBOM in the body.
     * The API will respond with an HTTP 200 and a jackson serialized QualityReport object.
     *
     * @param boms - list of files to compare
     * @return - jackson serialized QualityReport object
     */
    @RequestMapping(value="qa", method=RequestMethod.POST)
    public ResponseEntity<QualityReport> qa(@RequestBody List<MultipartFile> boms) {
        ArrayList<SBOM> sboms;

        //decode post body
        sboms = multiFilesToSboms(boms);

        if(sboms.size() < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        //run the QA
        QualityReport report = pipeline.process(sboms.get(0));

        //encode and send report
        try {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
