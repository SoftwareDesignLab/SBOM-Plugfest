package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.DependencyTree;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.PURL;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * file: DiffReportTest.java
 *
 * @author Derek Garcia
 */
public class DiffReportTest {


    private SBOM generateTemplateSBOM() {
        SBOM s = new SBOM(SBOM.Type.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
        try{
            Component test_component_a = new Component(
                    "red", "red_publisher", "1.1.0",
                    Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
            );

            Component test_component_b = new Component(
                    "red", "red_publisher", "1.2.3",
                    Set.of("cpe2.3::test_red_cpe_123"), Set.of(new PURL("pkg:redpackage/red@1.2.3")), Set.of("random_red_swid123")
            );

            Component test_component_c = new Component(
                    "blue", "blue_publisher", "1.1.0",
                    Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
            );

            s.addComponent(null, test_component_b);
            s.addComponent(test_component_b.getUUID(), test_component_a);
            s.addComponent(test_component_b.getUUID(), test_component_c);
        } catch (Exception e){
            fail("Failed to generate SBOM");
        }




        return s;
    }

    ///
    /// Constructor
    ///

    @Test
    @DisplayName("DiffReport Constructor")
    public void test_diffreport_constructor(){
        DiffReport d = new DiffReport("TEST_DIFF", generateTemplateSBOM());
        assertNotNull(d);
    }

    ///
    /// Comparison
    ///


}
