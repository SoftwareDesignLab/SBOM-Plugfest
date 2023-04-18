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

public class CompareFromAPITest {

    private final MultipartFile alpineSBOM = new MockMultipartFile(System.getProperty("user.dir") + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.alpine-compare.2-3.spdx");
    private final MultipartFile pythonSBOM = new MockMultipartFile(System.getProperty("user.dir") + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.python.2-3.spdx");
    private final MultipartFile dockerSBOM = new MockMultipartFile(System.getProperty("user.dir") + "/src/test/java/org/nvip/plugfest/tooling/sample_sboms/sbom.docker.2-2.spdx");

    private APIController ctrl;

    @Test
    public void compareTest() throws IOException {

        List<MultipartFile> m = new ArrayList<>();
        m.add(alpineSBOM);
        m.add(pythonSBOM);
        m.add(dockerSBOM);
        ResponseEntity<Comparison> report = ctrl.compare(m);
        assert report.getStatusCode() == HttpStatus.OK;
        assert report.getBody().getDiffReports().size() == 3; // todo translator doesn't add components
                                                              // but for the purposes of testing the API this should be fine
    }

    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
