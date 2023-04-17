package org.nvip.plugfest.tooling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class QAFromAPITest {

    private final MultipartFile alpineSBOM = new MockMultipartFile("../sample_sboms/sbom.alpine.xml");
    private final MultipartFile pythonSBOM = new MockMultipartFile("../sample_sboms/sbom.python.xml");
    private APIController ctrl;

    @Test
    public void qaTest() {

        List<MultipartFile> m = new ArrayList<>();
        m.add(alpineSBOM);
        m.add(pythonSBOM);
        assert ctrl.qa(m).getStatusCode() == HttpStatus.OK;

    }

    @BeforeEach
    public void setup(){

        ctrl = new APIController();

    }

}
