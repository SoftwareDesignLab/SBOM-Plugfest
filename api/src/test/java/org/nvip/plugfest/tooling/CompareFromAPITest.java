package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.APIController;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompareFromAPITest {

    private final MultipartFile alpineSBOM = new MockMultipartFile("../sample_sboms/sbom.alpine.xml");
    private final MultipartFile pythonSBOM = new MockMultipartFile("../sample_sboms/sbom.python.xml");
    private APIController ctrl;

    @Test
    public void compareTest() throws IOException {

        List<MultipartFile> m = new ArrayList<>();
        m.add(alpineSBOM);
        m.add(pythonSBOM);
        ResponseEntity<Comparison> report = ctrl.compare(m); //todo fix tests
        assert report.getStatusCode() == HttpStatus.OK;
        assert Objects.requireNonNull(report.getBody()).getDiffReports() != null;
        assert Objects.requireNonNull(report.getBody()).getComparisons() != null;

    }

    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
