package org.nvip.plugfest.tooling.sbom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.Debug;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SBOMMetadataTest {
    private static SBOMMetadata metadata;

    private static final String testTimestamp = "2023-04-05T16:49:04+00:00";
    private static final String testDataLicense = "CC0-1.0";
    private static final String testLicenseListVersion = "3.19";
    private static final Map<String, String> testTools = new HashMap<>() {{
        this.put("toolName1", "toolString1");
        this.put("toolName2", "toolString2");
    }};

    private static final Set<String> testSuppliers = new HashSet<>() {{
        this.add("author1");
        this.add("author2");
    }};

    @BeforeEach
    void setup() {
        metadata = new SBOMMetadata(testTimestamp, testSuppliers, testTools);
        metadata.setDataLicense(testDataLicense);
//        metadata.setLicenseListVersion(testLicenseListVersion);
    }

    @Test
    void testMetadataSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metadata);
        Debug.logBlockTitle("METADATA");
        System.out.println(json);
        Debug.logBlock();
    }
}
