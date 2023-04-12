package org.nvip.plugfest.tooling.differ;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * File: Comparison.java
 * holds comparison data between the target SBOM and other SBOMs
 *
 * @author Juan Francisco Patino, Tyler Drake, Henry Orsagh
 */
public class Comparison {

    /**
     * Target SBOM (Ground Truth) for comparison.
     */
    private SBOM targetSBOM;

    /**
     * DiffReports between targetSBOM and each other SBOM.
     * first index will be null (comparison to self).
     */
    private List<DiffReport> diffReportList;

    /**
     * Comparison map
     * @key component name
     * @value each version of the component found in the SBOMs
     */
    private Map<String, Set<ComponentVersion>> comparisons;

    public Comparison(SBOM targetSBOM, List<DiffReport> diffReportList, Map<String, Set<ComponentVersion>> comparisons) {
        this.targetSBOM = targetSBOM;
        this.diffReportList = diffReportList;
        this.comparisons = comparisons;
    }

    /**
     *
     * @return Target SBOM (Ground Truth) for comparison.
     */
    public SBOM getTargetSBOM() {
        return this.targetSBOM;
    }

    /**
     *
     * @return DiffReports between targetSBOM and each other SBOM.
     */
    public List<DiffReport> getDiffReports() { return this.diffReportList; }


    /**
     *
     * @return map of component name -> Set of each found component version
     */
    public Map<String, Set<ComponentVersion>> getComparisons() {
        return this.comparisons;
    }


}
