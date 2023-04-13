package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.DependencyTree;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMConflictType;
import org.nvip.plugfest.tooling.sbom.SBOMType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonTest {

    /**
     * Test Constants
     */


    /**
     * Setup Teardown Methods
     */
    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void teardown() {

    }


    /**
     * runComparison tests
     */
    @Test
    public void runComparison_should_create_list_of_reports_test() {

        SBOM test_SBOM_target = new SBOM(SBOMType.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_a = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_b = new SBOM(SBOMType.SPDX, "2", "2", "supplier",
                "b9fc484b-41c4-4589-b3ef-c57bba20078c", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_c = new SBOM(SBOMType.CYCLONE_DX, "1.4", "1", "supplier_three",
                "urn:uuid:1b53623d-a11a-1111-1a11-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        List<SBOM> test_SBOM_list = new ArrayList<>();
        test_SBOM_list.add(test_SBOM_a);
        test_SBOM_list.add(test_SBOM_b);
        test_SBOM_list.add(test_SBOM_c);

        Comparison test_comparison = new Comparison(test_SBOM_target);

        test_comparison.runComparison(test_SBOM_list);

        List<DiffReport> test_report_result = test_comparison.getDiffReports();

        assertNotNull(test_report_result);
        assertEquals(3, test_report_result.size());

        // Check conflicts for each diff report

        // SBOM A
        Set<SBOMConflictType> test_a_conflicts = test_report_result.get(0).getSbomConflict().getConflicts();
        assertEquals(4, test_a_conflicts.size());
        assertTrue(
                test_a_conflicts.contains(SBOMConflictType.AUTHOR_MISMATCH) &&
                        test_a_conflicts.contains(SBOMConflictType.SBOM_VERSION_MISMATCH) &&
                        test_a_conflicts.contains(SBOMConflictType.SCHEMA_VERSION_MISMATCH) &&
                        test_a_conflicts.contains(SBOMConflictType.TIMESTAMP_MISMATCH)
        );

        // SBOM B
        Set<SBOMConflictType> test_b_conflicts = test_report_result.get(1).getSbomConflict().getConflicts();
        assertEquals(test_b_conflicts.size(), 5);
        assertTrue(
                test_b_conflicts.contains(SBOMConflictType.ORIGIN_FORMAT_MISMATCH) &&
                        test_b_conflicts.contains(SBOMConflictType.SBOM_VERSION_MISMATCH) &&
                        test_b_conflicts.contains(SBOMConflictType.SCHEMA_VERSION_MISMATCH) &&
                        test_b_conflicts.contains(SBOMConflictType.TIMESTAMP_MISMATCH) &&
                        test_b_conflicts.contains(SBOMConflictType.SERIAL_NUMBER_MISMATCH)
        );

        // SBOM C
        Set<SBOMConflictType> test_c_conflicts = test_report_result.get(2).getSbomConflict().getConflicts();
        assertEquals(test_c_conflicts.size(), 3);
        assertTrue(
                test_c_conflicts.contains(SBOMConflictType.AUTHOR_MISMATCH) &&
                        test_c_conflicts.contains(SBOMConflictType.TIMESTAMP_MISMATCH) &&
                        test_c_conflicts.contains(SBOMConflictType.SERIAL_NUMBER_MISMATCH)
        );

    }


    /**
     * assignComponents tests
     */

    /**
     * getTargetSBOM tests
     */

    /**
     * getDiffReports
     */

    /**
     * getComparisons
     */

}
