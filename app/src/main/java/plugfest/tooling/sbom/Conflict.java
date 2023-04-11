package plugfest.tooling.sbom;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to mark a conflict between two components
 *
 * @author Matt London
 */
public class Conflict {
    /** The two components that have a conflict */
    private Component componentA;
    private Component componentB;
    /** The type of conflict between the components */
    private Set<ConflictType> conflictTypes;

    /**
     * Determine the type of conflict between the two components
     */
    private void assignConflictType() {
        // To be set to true if we hit a situation where one component is a subset of the other
        boolean falsePositive = false;

        // Compare the components and find the difference, then add it to the list of conflicts
        if (componentA.getName() != null && !componentA.getName().equals(componentB.getName())) {
            conflictTypes.add(ConflictType.COMPONENT_NAME_MISMATCH);
        }
        if (componentA.getPublisher() != null && !componentA.getPublisher().equals(componentB.getPublisher())) {
            conflictTypes.add(ConflictType.COMPONENT_PUBLISHER_MISMATCH);
        }
        if (componentA.getVersion() != null && !componentA.getVersion().equals(componentB.getVersion())) {
            conflictTypes.add(ConflictType.COMPONENT_VERSION_MISMATCH);
        }
        if (componentA.getCpes() != null && !componentA.getCpes().equals(componentB.getCpes())) {
            // Check if one set contains all items from the other
            if (componentA.getCpes().containsAll(componentB.getCpes()) || componentB.getCpes().containsAll(componentA.getCpes())) {
                // Then this isn't really a conflict
                falsePositive = true;
            }
            else {
                conflictTypes.add(ConflictType.COMPONENT_CPE_MISMATCH);
            }
        }
        if (componentA.getPurls() != null && !componentA.getPurls().equals(componentB.getPurls())) {
            if (componentA.getPurls().containsAll(componentB.getPurls()) || componentB.getPurls().containsAll(componentA.getPurls())) {
                // Then this isn't really a conflict
            }
            else {
                conflictTypes.add(ConflictType.COMPONENT_PURL_MISMATCH);
            }
        }
        if (componentA.getSwids() != null && !componentA.getSwids().equals(componentB.getSwids())) {
            if (componentA.getSwids().containsAll(componentB.getSwids()) || componentB.getSwids().containsAll(componentA.getSwids())) {
                // Then this isn't really a conflict
                falsePositive = true;

            }
            else {
                conflictTypes.add(ConflictType.COMPONENT_SWID_MISMATCH);
            }
        }
        if (componentA.getUniqueID() != null && !componentA.getUniqueID().equals(componentB.getUniqueID())) {
            conflictTypes.add(ConflictType.COMPONENT_SPDXID_MISMATCH);
        }
        if (componentA.getLicenses() != null && !componentA.getLicenses().equals(componentB.getLicenses())) {
            conflictTypes.add(ConflictType.COMPONENT_LICENSE_MISMATCH);
        }
        if (conflictTypes.isEmpty() && !falsePositive) {
            conflictTypes.add(ConflictType.COMPONENT_UNKNOWN_MISMATCH);
        }

    }

    /**
     * Construct a conflict between two components
     *
     * @param componentA First component
     * @param componentB Second component
     */
    public Conflict(Component componentA, Component componentB) {
        conflictTypes = new HashSet<>();

        // Deep copy all information
        // In the case that the conflict is the component doesn't exist at all, then a or b may be null
        if (componentA != null) {
            this.componentA = new Component();
            this.componentA.copyFrom(componentA);
        }
        else {
            this.componentA = null;
            this.conflictTypes.add(ConflictType.COMPONENT_NOT_FOUND);
        }

        if (componentB != null) {
            this.componentB = new Component();
            this.componentB.copyFrom(componentB);
        }
        else {
            this.componentB = null;
            this.conflictTypes.add(ConflictType.COMPONENT_NOT_FOUND);
        }

        // Determine the type
        if (!this.conflictTypes.contains(ConflictType.COMPONENT_NOT_FOUND)) {
            // In this case we know both components exist
            // Now we need to determine what the conflict is
            assignConflictType();
        }
    }

    public Component getComponentA() {
        return componentA;
    }

    public Component getComponentB() {
        return componentB;
    }

    public Set<ConflictType> getConflictTypes() {
        return conflictTypes;
    }
}
