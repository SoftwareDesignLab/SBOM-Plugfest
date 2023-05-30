package org.nvip.plugfest.tooling.differ;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.sbom.ComponentConflict;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMConflict;

import java.util.Set;

/**
 * Class to hold results of a diff comparison between two SBOMs
 *
 * @author Matt London
 * @author Derek Garcia
 */
public class DiffReport {

    /**
     * Utility class for storing conflict data
     */
    private static class Conflict{
        @JsonProperty
        private final String type;
        @JsonProperty
        private final String target;
        @JsonProperty
        private final String current;

        /**
         * Create new conflict and record differences
         *
         * @param type Type of conflict
         * @param target data stored in the target SBOM
         * @param current data stored in the current SBOM
         */
        public Conflict(String type, String target, String current){
            this.type = type;
            this.target = target;
            this.current = current;
        }
    }
    @JsonProperty("target")
    private String targetUID;

    private SBOM targetSBOM;
    /** Stores conflicts in non-trivial information within these sboms */
    private SBOMConflict sbomConflict;
    /** Stores conflicts between components in two SBOMs */
    private Set<ComponentConflict> componentConflicts;

    /**
     * Construct the report from an sbom conflict and a set of component conflicts
     * @param sbomConflict the sbom conflict
     * @param componentConflicts the set of component conflicts
     */
    public DiffReport(SBOMConflict sbomConflict, Set<ComponentConflict> componentConflicts) {
        this.sbomConflict = sbomConflict;
        this.componentConflicts = componentConflicts;
    }

    public DiffReport(String targetUID, SBOM targetSBOM){
        this.targetUID = targetUID;
        this.targetSBOM = targetSBOM;
    }

    ///
    /// Getters
    ///

    public SBOMConflict getSbomConflict() {
        return sbomConflict;
    }

    public Set<ComponentConflict> getComponentConflicts() {
        return componentConflicts;
    }

    ///
    /// Overrides
    ///

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sbomConflict.toString());
        sb.append("Component Conflicts:\n");
        for (ComponentConflict conflict : componentConflicts) {
            sb.append(conflict.toString());
        }

        return sb.toString();
    }
}
