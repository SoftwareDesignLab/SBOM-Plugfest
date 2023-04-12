package org.nvip.plugfest.tooling.differ;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Comparison {

    // Target SBOM (Ground Truth) for comparison.
    private SBOM targetSBOM;

    // List of diff reports
    private List<DiffReport> diffReportList;

    // Set of comparisons
    private Map<String, Set<ComponentVersion>> comparisons;

    public Comparison() {

    }

    public SBOM getTargetSBOM() {
        return this.targetSBOM;
    }

    public List<DiffReport> getDiffReports() {
        return this.getDiffReports();
    }

    public Map<String, Set<ComponentVersion>> getComparisons() {
        return this.comparisons;
    }


}
