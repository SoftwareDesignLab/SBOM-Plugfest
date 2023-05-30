package org.nvip.plugfest.tooling.differ;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.*;

import static org.nvip.plugfest.tooling.sbom.ComponentConflictType.*;

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
    private record ConflictData(@JsonProperty String type, @JsonProperty String target, @JsonProperty String other) {
        /**
         * Create new conflict and record differences
         *
         * @param type    Type of conflict
         * @param target  data stored in the target SBOM
         * @param other data stored in the current SBOM
         */
        private ConflictData(String type, String target, String other) {
            this.type = type;
            this.target = target;
            this.other = other;
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
    @JsonProperty
    private ConflictOrganizer diffReport = new ConflictOrganizer();   // todo could get rid of ConflictOrganizer
    private final SBOM targetSBOM;

    public DiffReport(String targetUID, SBOM targetSBOM){
        this.targetUID = targetUID;
        this.targetSBOM = targetSBOM;
    }

    /**
     * Generate a report of the differences between two SBOMs and store the results
     *
     * @param otherSBOM other SBOM to compare against
     */
    public void compare(SBOM otherSBOM) {

        // Compare SBOM level differences
        compareSBOMs(otherSBOM);

        // Compare Component level Differences
        compareComponents(otherSBOM.getAllComponents());

    }

    private void compareSBOMs(SBOM otherSBOM){
        // Compare SBOMs
        SBOMConflict sbomConflict = new SBOMConflict(this.targetSBOM, otherSBOM);

        // Get data for conflicts
        // todo move sbom conflicts here
        for(SBOMConflictType conflictType : sbomConflict.getConflicts()){
            String targetValue = null, otherValue = null;
            switch (conflictType) {
                case SUPPLIER_MISMATCH -> {
                    targetValue = targetSBOM.getSupplier();
                    otherValue = otherSBOM.getSupplier();
                }
                // todo not implemented
                case AUTHOR_MISMATCH -> {
                    targetValue = "";
                    otherValue = "";
                }
                case TIMESTAMP_MISMATCH -> {
                    targetValue = targetSBOM.getTimestamp();
                    otherValue = otherSBOM.getTimestamp();
                }
                case ORIGIN_FORMAT_MISMATCH -> {
                    targetValue = targetSBOM.getOriginFormat().name();
                    otherValue = otherSBOM.getOriginFormat().name();
                }
                case SCHEMA_VERSION_MISMATCH -> {
                    targetValue = targetSBOM.getSpecVersion();
                    otherValue = otherSBOM.getSpecVersion();
                }
                case SBOM_VERSION_MISMATCH -> {
                    targetValue = targetSBOM.getSbomVersion();
                    otherValue = otherSBOM.getSbomVersion();
                }
                case SERIAL_NUMBER_MISMATCH -> {
                    targetValue = targetSBOM.getSerialNumber();
                    otherValue = otherSBOM.getSerialNumber();
                }
            }

            // add details to diff report
            this.diffReport.addSBOMConflict(new ConflictData(conflictType.name(), targetValue, otherValue));

        }
    }

    private void compareComponents(Set<Component> otherComponents){
        Set<ComponentConflict> componentConflicts = new HashSet<>();

        Set<Component> targetComponents = this.targetSBOM.getAllComponents();

        Set<String> targetComponentNames = new HashSet<>();
        Set<String> otherComponentNames = new HashSet<>();

        // Add all target components to a map
        HashMap<String, Component> targetComponentMap = new HashMap<>();
        for (Component targetComponent : targetComponents) {
            // Only look at packaged components
            if (targetComponent.isUnpackaged())
                continue;

            // add to target map
            targetComponentMap.put(targetComponent.getName(), targetComponent);
            targetComponentNames.add(targetComponent.getName());
        }

        // Check to see if all other components are in target
        for (Component otherComponent : otherComponents) {
            // Only look at packaged components
            if (otherComponent.isUnpackaged())
                continue;

            // add to other map
            otherComponentNames.add(otherComponent.getName());

            // Check to see if target SBOM contains the other component
            if (!targetComponentMap.containsKey(otherComponent.getName())) {
                // target doesn't contain other component
                ComponentConflict conflict = new ComponentConflict(null, otherComponent);
                componentConflicts.add(conflict);
            } else {
                // Compare the two
                ComponentConflict conflict = new ComponentConflict(targetComponentMap.get(otherComponent.getName()), otherComponent);

                // add new conflict to existing conflict
                if (conflict.getConflictTypes().size() > 0)
                    componentConflicts.add(conflict);

            }
        }

        // Check to see if target SBOM contains the other component
        for (String targetComponent : targetComponentNames) {
            if (!otherComponentNames.contains(targetComponent)) {
                // other doesn't contain target component
                ComponentConflict conflict = new ComponentConflict(targetComponentMap.get(targetComponent), null);
                componentConflicts.add(conflict);
            }
        }
        
        // get data
        // todo this and components will need a refactor to use new diff report model
        for(ComponentConflict conflict : componentConflicts){
            for(ComponentConflictType ct : conflict.getConflictTypes()){
                String targetValue = null, otherValue = null;
                switch (ct) {
                    // todo need better way to handle this
                    case COMPONENT_NOT_FOUND -> {
                        targetValue = conflict.getComponentA() == null ? null : conflict.getComponentA().getName();
                        otherValue = conflict.getComponentB() == null ? null : conflict.getComponentB().getName();
                    }
                    case COMPONENT_VERSION_MISMATCH -> {
                        targetValue = conflict.getComponentA().getVersion();
                        otherValue = conflict.getComponentB().getVersion();
                    }

                    // todo need better solution with component refactor
                    case COMPONENT_LICENSE_MISMATCH -> {
                        // get licenses
                        Set<String> licenseA = new HashSet<>(conflict.getComponentA().getLicenses());
                        Set<String> licenseB = new HashSet<>(conflict.getComponentA().getLicenses());

                        // remove duplicates
                        licenseA.removeAll(conflict.getComponentA().getLicenses());
                        licenseB.removeAll(conflict.getComponentA().getLicenses());
                        for (String license : licenseA) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentA().getName(),
                                    conflict.getComponentB().getName(),
                                    new ConflictData(COMPONENT_LICENSE_MISMATCH.name(), license, null)
                            );
                        }
                        for (String license : licenseB) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentB().getName(),
                                    conflict.getComponentA().getName(),
                                    new ConflictData(COMPONENT_LICENSE_MISMATCH.name(), null, license)
                            );
                        }
                        continue;
                    }
                    case COMPONENT_PUBLISHER_MISMATCH -> {
                        targetValue = conflict.getComponentA().getPublisher();
                        otherValue = conflict.getComponentB().getPublisher();
                    }
                    case COMPONENT_NAME_MISMATCH -> {
                        targetValue = conflict.getComponentA().getName();
                        otherValue = conflict.getComponentB().getName();
                    }
                    case COMPONENT_CPE_MISMATCH -> {
                        // get licenses
                        Set<String> cpeA = new HashSet<>(conflict.getComponentA().getCpes());
                        Set<String> cpeB = new HashSet<>(conflict.getComponentA().getCpes());

                        // remove duplicates
                        cpeA.removeAll(conflict.getComponentA().getCpes());
                        cpeB.removeAll(conflict.getComponentA().getCpes());
                        for (String cpe : cpeA) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentA().getName(),
                                    conflict.getComponentB().getName(),
                                    new ConflictData(COMPONENT_CPE_MISMATCH.name(), cpe, null)
                            );
                        }
                        for (String cpe : cpeB) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentB().getName(),
                                    conflict.getComponentA().getName(),
                                    new ConflictData(COMPONENT_CPE_MISMATCH.name(), null, cpe)
                            );
                        }
                        continue;
                    }
                    case COMPONENT_PURL_MISMATCH -> {
                        // get cpes
                        Set<PURL> purlA = new HashSet<>(conflict.getComponentA().getPurls());
                        Set<PURL> purlB = new HashSet<>(conflict.getComponentA().getPurls());

                        // remove duplicates
                        purlA.removeAll(conflict.getComponentA().getPurls());
                        purlB.removeAll(conflict.getComponentA().getPurls());
                        for (PURL purl : purlA) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentA().getName(),
                                    conflict.getComponentB().getName(),
                                    new ConflictData(COMPONENT_PURL_MISMATCH.name(), purl.toString(), null)
                            );
                        }
                        for (PURL purl : purlB) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentB().getName(),
                                    conflict.getComponentA().getName(),
                                    new ConflictData(COMPONENT_PURL_MISMATCH.name(), null, purl.toString())
                            );
                        }
                        continue;
                    }
                    case COMPONENT_SWID_MISMATCH -> {
                        // get swids
                        Set<String> swidA = new HashSet<>(conflict.getComponentA().getSwids());
                        Set<String> swidB = new HashSet<>(conflict.getComponentA().getSwids());

                        // remove duplicates
                        swidA.removeAll(conflict.getComponentA().getSwids());
                        swidB.removeAll(conflict.getComponentA().getSwids());
                        for (String swid : swidA) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentA().getName(),
                                    conflict.getComponentB().getName(),
                                    new ConflictData(COMPONENT_SWID_MISMATCH.name(), swid, null)
                            );
                        }
                        for (String swid : swidB) {
                            this.diffReport.addComponentConflict(
                                    conflict.getComponentB().getName(),
                                    conflict.getComponentA().getName(),
                                    new ConflictData(COMPONENT_SWID_MISMATCH.name(), null, swid)
                            );
                        }
                        continue;
                    }
                    case COMPONENT_SPDXID_MISMATCH -> {
                        targetValue = conflict.getComponentA().getUniqueID();
                        otherValue = conflict.getComponentB().getUniqueID();
                    }
                    case COMPONENT_UNKNOWN_MISMATCH -> {
                        targetValue = conflict.getComponentA().toString();
                        otherValue = conflict.getComponentB().toString();
                    }
                }

                // todo use better identifiers than name
                this.diffReport.addComponentConflict(
                        conflict.getComponentA().getName(),
                        conflict.getComponentB().getName(),
                        new ConflictData(ct.name(), targetValue, otherValue));
               
            }
        }
        
        
    }
}
