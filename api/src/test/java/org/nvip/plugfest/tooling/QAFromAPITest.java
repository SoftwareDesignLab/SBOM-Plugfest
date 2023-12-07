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
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.qa.QualityReport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


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

    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
