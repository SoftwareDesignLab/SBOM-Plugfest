package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import org.nvip.plugfest.tooling.utils.Utils.*;

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
    private final ArrayList<SBOMFile> sboms = new ArrayList<>();

    @Test
    @DisplayName("Null/Empty File Contents")
    void emptyContentsTest() {

        sboms.add(new SBOMFile(alpineSBOM, ""));
        sboms.add(new SBOMFile(pythonSBOM, ""));
        sboms.add(new SBOMFile(dockerSBOM, ""));
        SBOMFile[] arr = Utils.configSbomFileArr(sboms);

        ResponseEntity<?> response = ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Null/Empty File Names")
    void emptyFileNamesTest() throws IOException {
        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        sboms.add(new SBOMFile("", new String(Files.readAllBytes(Paths.get(pythonSBOM)))));
        sboms.add(new SBOMFile("", new String(Files.readAllBytes(Paths.get(dockerSBOM)))));
        SBOMFile[] arr = Utils.configSbomFileArr(sboms);

        ResponseEntity<?> response = ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("One sbom")
    void oneSbomTest() throws IOException {

        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        SBOMFile[] arr = Utils.configSbomFileArr(sboms);
        ResponseEntity<?> report =  ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, report.getStatusCode());

    }

    // todo tests that break the translators / return an INTERNAL_SERVER_ERROR

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
        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        sboms.add(new SBOMFile(pythonSBOM, new String(Files.readAllBytes(Paths.get(pythonSBOM)))));
        sboms.add(new SBOMFile(dockerSBOM, new String(Files.readAllBytes(Paths.get(dockerSBOM)))));
        SBOMFile[] arr = Utils.configSbomFileArr(sboms);

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
