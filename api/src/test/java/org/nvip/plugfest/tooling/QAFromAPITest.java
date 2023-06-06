package org.nvip.plugfest.tooling;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nvip.plugfest.tooling.utils.Utils;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * File: QAFromAPITest.java
 * Unit tests for API regarding QA
 * <p>
 *     Tests:<br>
 *     - qaTest: Test that the API can QA a small SBOM<br>
 *     - qaFastTest: Test that the API can QA a large SBOM
 * </p>
 *
 * @author Juan Francisco Patino
 */
public class QAFromAPITest {

    private final String smallDockerSBOM = System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.docker.2-2_small.spdx";
    private final String pythonSBOM = System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.python.2-3.spdx";
    private APIController ctrl;
    private HttpServletRequest request;

    @BeforeEach
    public void setup(){
        ctrl = new APIController();
        request = Mockito.mock(HttpServletRequest.class);
    }

    // TODO FIX
//    @Test
//    public void qaTest() {
//        try {
//            String contents = new String(Files.readAllBytes(Paths.get(smallDockerSBOM)));
//
//            ResponseEntity<QualityReport> qa = ctrl.qa(contents, smallDockerSBOM);
//            assertEquals(qa.getStatusCode(), HttpStatus.OK);
//            assertNotEquals(qa.getBody().getPassedComponents(), 0);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//            assertEquals(1, 0);
//        }
//    }

    // TODO FIX
//    @Test
//    public void qaFastTest() {
//        try {
//            String contents = new String(Files.readAllBytes(Paths.get(pythonSBOM)));
//
//            ResponseEntity<QualityReport> qa = ctrl.qa(contents, pythonSBOM);
//            assertEquals(qa.getStatusCode(), HttpStatus.OK);
//            assertNotEquals(qa.getBody().getPassedComponents(), 0);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//            assertEquals(1, 0);
//        }
//    }

    @Test
    public void qaEncodingTest() throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(smallDockerSBOM)));
        Utils.SBOMFile file = new Utils.SBOMFile(smallDockerSBOM, contents);

        ResponseEntity<?> qa = ctrl.qa(request, file);
//        assertEquals(qa.getStatusCode(), HttpStatus.OK);
//        assertNotEquals(qa.getBody().getPassedComponents(), 0);
//        System.out.println(qa.getBody());
    }
}
