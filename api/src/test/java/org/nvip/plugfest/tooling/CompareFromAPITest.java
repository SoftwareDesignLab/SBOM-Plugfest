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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//pliu import org.nvip.plugfest.tooling.differ.Comparison;
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
        SBOMFile[] arr = sboms.toArray(new SBOMFile[0]);

        ResponseEntity<?> response = ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Null/Empty File Names")
    void emptyFileNamesTest() throws IOException {
        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        sboms.add(new SBOMFile("", new String(Files.readAllBytes(Paths.get(pythonSBOM)))));
        sboms.add(new SBOMFile("", new String(Files.readAllBytes(Paths.get(dockerSBOM)))));
        SBOMFile[] arr = sboms.toArray(new SBOMFile[0]);

        ResponseEntity<?> response = ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("One sbom")
    void oneSbomTest() throws IOException {

        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        SBOMFile[] arr = sboms.toArray(new SBOMFile[0]);
        ResponseEntity<?> report =  ctrl.compare(0, arr);
        assertEquals(HttpStatus.BAD_REQUEST, report.getStatusCode());

    }

    @Test
    @DisplayName("Index out of bounds test")
    void indexOutOfBoundsTest() throws IOException {

        sboms.add(new SBOMFile(alpineSBOM, new String(Files.readAllBytes(Paths.get(alpineSBOM)))));
        sboms.add(new SBOMFile(pythonSBOM, new String(Files.readAllBytes(Paths.get(pythonSBOM)))));
        sboms.add(new SBOMFile(dockerSBOM, new String(Files.readAllBytes(Paths.get(dockerSBOM)))));
        SBOMFile[] arr = sboms.toArray(new SBOMFile[0]);

        ResponseEntity<?> report =  ctrl.compare(4, arr);
        assertEquals(HttpStatus.BAD_REQUEST, report.getStatusCode());

        report =  ctrl.compare(-1, arr);
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
        SBOMFile[] arr = sboms.toArray(new SBOMFile[0]);

        ResponseEntity<?> report =  ctrl.compare(0, arr);
        assertEquals(report.getStatusCode(), HttpStatus.OK);
        //pliu assertEquals(arr.length, ((Comparison) Objects.requireNonNull(report.getBody())).getDiffReports().size());
        //pliu assertNotEquals(arr.length,((Comparison) Objects.requireNonNull(report.getBody())).getComparisons().size());
    }

    /**
     * SETUP: Start API before testing
     */
    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
