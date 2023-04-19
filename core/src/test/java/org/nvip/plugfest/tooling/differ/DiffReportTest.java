package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DiffReportTest {

    /**
     * Test Constants
     */

    /**
     * Test variables
     */

    SBOM test_SBOM_a;

    SBOM test_SBOM_b;

    SBOMConflict test_SBOM_conflict;

    ComponentConflict test_component_conflict_one;

    ComponentConflict test_component_conflict_two;

    ComponentConflict test_component_conflict_three;

    Component test_component_a;

    Component test_component_b;

    Component test_component_c;

    Component test_component_d;

    Component test_component_e;


    /**
     * Test Setup
     * Creates:
     *  - (2) 'SBOM'
     *  - (1) 'SBOMConflict'
     *  - (4) 'Component'
     *  - (2) 'ComponentConflict'
     */
    @BeforeEach
    public void setup() {

        // Initialize two SBOM objects
        test_SBOM_a = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        test_SBOM_b = new SBOM(SBOMType.SPDX, "2", "2", "supplier",
                "b9fc484b-41c4-4589-b3ef-c57bba20078c", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        test_SBOM_conflict = new SBOMConflict(test_SBOM_a, test_SBOM_b);

        // Initialize components and create component conflicts
        test_component_a = new Component(
                "red", "red_publisher", "1.1.0",
                Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
        );

        test_component_b = new Component(
                "blue", "blue_publisher", "1.1.0",
                Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
        );

        test_component_c = new Component(
                "yellow", "yellow_publisher", "2.3.7",
                Set.of("cpe2.3::test_yellow_cpe"), Set.of(new PURL("pkg:yellowpackage/yellow@2.3.7")), Set.of("random_yellow_swid")
        );

        test_component_d = new Component(
                "green", "green_publisher", "4.4.5",
                Set.of("cpe2.3::test_green_cpe"), Set.of(new PURL("pkg:greenpackage/yellow@2.3.7")), Set.of("random_green_swid")
        );

        test_component_e = new Component(
                "purple", "purple_publisher", "2.1.2",
                Set.of("cpe2.3::test_purple_cpe"), Set.of(new PURL("pkg:purplepackage/yellow@2.3.7")), Set.of("random_purple_swid")
        );

        // Conflict with two different components
        test_component_conflict_one = new ComponentConflict(test_component_a, test_component_b);

        // Conflict with two different components
        test_component_conflict_two = new ComponentConflict(test_component_c, test_component_d);

        // Conflict with same components
        test_component_conflict_three = new ComponentConflict(test_component_e, test_component_e);

    }

    /**
     * Test Teardown
     * Nullifies all setup items
     */
    @AfterEach
    public void teardown() {
        test_SBOM_a = null;
        test_SBOM_b = null;
        test_SBOM_conflict = null;
        test_component_a = null;
        test_component_b = null;
        test_component_c = null;
        test_component_d = null;
        test_component_e = null;
        test_component_conflict_one = null;
        test_component_conflict_two = null;
        test_component_conflict_three = null;
    }

    @Test
    public void create_differReport_test() {

        Set<ComponentConflict> test_conflicts = new HashSet<>(
                Arrays.asList(test_component_conflict_one, test_component_conflict_two, test_component_conflict_three)
        );

        DiffReport test_report = new DiffReport(test_SBOM_conflict, test_conflicts);

        assertNotNull(test_report);

    }

}
