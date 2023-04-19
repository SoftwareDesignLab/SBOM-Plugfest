package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * File: CompareFromAPITest.java
 * Unit test for API regarding Comparisons
 * <p>
 * Tests:<br>
 * - compareTest: Test that the API can compare three SBOMs
 *
 * @author Juan Francisco Patino
 */
public class CompareFromAPITest {
    /**
     *  Example SBOMs to use for testing
     */
    private final MultipartFile alpineSBOM = new MockMultipartFile(System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.alpine-compare.2-3.spdx");
    private final MultipartFile pythonSBOM = new MockMultipartFile(System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.python.2-3.spdx");
    private final MultipartFile dockerSBOM = new MockMultipartFile(System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.docker.2-2.spdx");

    /**
     * Controller to test
     */
    private APIController ctrl;

    /**
     * Test that the API can compare three SBOMs
     * @throws IOException - if the SBOM parsing is broken
     */
    @Test
    public void compareTest() throws IOException {

        List<MultipartFile> m = new ArrayList<>();
        m.add(alpineSBOM);
        m.add(pythonSBOM);
        m.add(dockerSBOM);
        ResponseEntity<Comparison> report = ctrl.compare(m);
        assertEquals(report.getStatusCode(), HttpStatus.OK);
        assertEquals(report.getBody().getDiffReports().size(), 2);
        assertNotEquals(report.getBody().getComparisons().size(),0);
    }

    /**
     * SETUP: Ensure API is running.
     */
    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
