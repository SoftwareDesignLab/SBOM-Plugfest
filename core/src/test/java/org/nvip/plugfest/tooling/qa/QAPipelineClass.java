package org.nvip.plugfest.tooling.qa;

import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QAPipelineClass {

    /**
     * QAPipeline Constructor Tests
     */

    @Test
    public void create_QAPipeline_test() {
        QAPipeline qaPipeline = new QAPipeline();
        assertNotNull(qaPipeline);
    }

    /**
     * process Tests
     */

    @Test
    public void process_test() {
        QAPipeline qaPipeline = new QAPipeline();
        assertNotNull(qaPipeline);

        // Create and SBOM with some components
        SBOM test_SBOM = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        Component test_component_a = new Component(
                "red", "red_publisher", "1.1.0",
                Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
        );

        Component test_component_b = new Component(
                "blue", "blue_publisher", "1.1.0",
                Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
        );

        test_SBOM.addComponent(null, test_component_a);
        test_SBOM.addComponent(test_component_a.getUUID(), test_component_b);

        // Throw the SBOM into the QA Pipeline
        QualityReport test_quality_report = qaPipeline.process(test_SBOM);

        // Make sure quality report is an actual QualityReport and is not null
        assertNotNull(test_quality_report);
        assertInstanceOf(QualityReport.class ,test_quality_report);

    }

}
