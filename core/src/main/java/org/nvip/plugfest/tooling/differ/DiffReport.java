package org.nvip.plugfest.tooling.differ;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cyclonedx.model.Hash;
import org.nvip.plugfest.tooling.sbom.ComponentConflict;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMConflict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static class ConflictData {
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
        public ConflictData(String type, String target, String current){
            this.type = type;
            this.target = target;
            this.current = current;
        }
    }

    /**
     * Utility class for organizing conflict data
     */
    private static class ConflictOrganizer {
        @JsonProperty
        private final List<ConflictData> sbomConflicts = new ArrayList<>();
        @JsonProperty
        private final HashMap<String, HashMap<String, List<ConflictData>>> componentConflicts = new HashMap<>();

        /**
         * Add new conflict data for the SBOMs
         *
         * @param data conflict data
         */
        public void addSBOMConflict(ConflictData data){
            this.sbomConflicts.add(data);
        }


        /**
         * Add new conflict data for the components
         *
         * @param targetCUID target component uid
         * @param otherCUID other component uid
         * @param data conflict data
         */
        public void addComponentConflict(String targetCUID, String otherCUID, ConflictData data){
            // add new target component if it doesn't exist
            this.componentConflicts.computeIfAbsent(targetCUID, k -> new HashMap<>());

            // add new other component if it doesn't exist
            this.componentConflicts.get(targetCUID).computeIfAbsent(otherCUID, k -> new ArrayList<>());

            // add the conflict data
            this.componentConflicts.get(targetCUID).get(otherCUID).add(data);
        }
    }


    @JsonProperty("target")
    private String targetUID;

    private HashMap<String, String>

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
