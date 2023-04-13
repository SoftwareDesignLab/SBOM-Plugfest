package org.nvip.plugfest.tooling.differ;

import org.cyclonedx.model.Diff;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;

public class Comparison {

    // Target SBOM (Ground Truth) for comparison.
    private SBOM targetSBOM;

    // List of diff reports
    private List<DiffReport> diffReportList;

    // Set of comparisons
    private Map<String, Set<ComponentVersion>> comparisons;

    public Comparison(SBOM target, List<SBOM> stream) {
        this.targetSBOM = target;
        this.diffReportList = new ArrayList<>();
        this.comparisons = new HashMap<>();
    }

    public List<DiffReport> runComparison(List<SBOM> stream) {
        for(SBOM current_sbom : stream) {
            assignComponents(current_sbom);
            diffReportList.add(Comparer.generateReport(targetSBOM, current_sbom));
        }
    }

    private void assignComponents(SBOM current_sbom) {

        // Loop through all components in SBOM and add them to comparisons list.
        for(Component current_component : current_sbom.getAllComponents()) {

            // Create a temporary ComponentVersion object for the current SBOM component
            ComponentVersion temporary_cv = new ComponentVersion(current_component.getName(), current_component.getVersion());

            // If the comparisons collection contains
            if(comparisons.containsKey(current_component.getName())) {

                // Get that component's component version collection
                Set<ComponentVersion> current_cv_list = comparisons.get(current_component.getName());

            } else {

            }
        }
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
