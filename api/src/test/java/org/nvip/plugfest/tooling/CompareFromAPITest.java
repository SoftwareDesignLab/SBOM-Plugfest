package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

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
    private final String alpineSBOM = System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.alpine-compare.2-3.spdx";
    private final String pythonSBOM = System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.python.2-3.spdx";
    private final String dockerSBOM = System.getProperty("user.dir")
            + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.docker.2-2.spdx";
    private final ArrayList<APIController.SBOMFile> sboms = new ArrayList<>();


    /**
     * Controller to test
     */
    private APIController ctrl;

    /**
     * Test that the API can compare three SBOMs
     * @throws IOException If the SBOM parsing is broken
     */
    @Test
    @DisplayName("Compare SBOMs Test")
    public void compareTest() throws IOException {
        sboms.add(new APIController.SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        sboms.add(new APIController.SBOMFile(pythonSBOM, new String(Files.readAllBytes(Paths.get(pythonSBOM)))));
        sboms.add(new APIController.SBOMFile(dockerSBOM, new String(Files.readAllBytes(Paths.get(dockerSBOM)))));

        APIController.SBOMFile[] arr = new APIController.SBOMFile[sboms.size()];

        for(int i = 0; i < sboms.size(); i++){
            arr[i] = sboms.get(i);
        }

        ResponseEntity<?> report =  ctrl.compare(0, arr);
        assertEquals(report.getStatusCode(), HttpStatus.OK);
        assertEquals(arr.length, ((Comparison) Objects.requireNonNull(report.getBody())).getDiffReports().size());
        assertNotEquals(arr.length,((Comparison) Objects.requireNonNull(report.getBody())).getComparisons().size());
    }

    /**
     * SETUP: Start API before testing
     */
    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
