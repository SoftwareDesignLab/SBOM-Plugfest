package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * File: QAFromAPITest.java
 * Unit tests for API regarding QA
 *
 * @author Juan Francisco Patino
 */
public class QAFromAPITest {

    private final MultipartFile smallDockerSBOM = new MockMultipartFile(System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.docker.2-2_small.spdx");
    private final MultipartFile pythonSBOM = new MockMultipartFile(System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.python.2-3.spdx");
    private APIController ctrl;

    @Test
    public void qaTest() {
        ResponseEntity<QualityReport> qa = ctrl.qa(smallDockerSBOM);
        assertEquals(qa.getStatusCode(), HttpStatus.OK);
        assertNotEquals(qa.getBody().getPassedComponents(), 0);
    }

    @Test
    public void qaFastTest() {
        ResponseEntity<QualityReport> qa = ctrl.qa(pythonSBOM);
        assertEquals(qa.getStatusCode(), HttpStatus.OK);
        assertEquals(qa.getBody().getPassedComponents(), 0);
    }

    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
