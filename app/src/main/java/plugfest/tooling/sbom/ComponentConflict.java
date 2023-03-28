package plugfest.tooling.sbom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to mark a conflict between two components
 *
 * @author Matt London
 */
public class ComponentConflict {
    /** The two components that have a conflict */
    private Component componentA;
    private Component componentB;
    /** The type of conflict between the components */
    private Set<ComponentConflictType> componentConflictTypes;

    /**
     * Determine the type of conflict between the two components
     */
    private void assignConflictType() {
        // To be set to true if we hit a situation where one component is a subset of the other
        boolean falsePositive = false;

        // Compare the components and find the difference, then add it to the list of conflicts
        if (componentA.getName() != null && !componentA.getName().equals(componentB.getName())) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_NAME_MISMATCH);
        }
        if (componentA.getPublisher() != null && !componentA.getPublisher().equals(componentB.getPublisher())) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_PUBLISHER_MISMATCH);
        }
        if (componentA.getVersion() != null && !componentA.getVersion().equals(componentB.getVersion())) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_VERSION_MISMATCH);
        }
        if (componentA.getCPE() != null && !componentA.getCPE().equals(componentB.getCPE())) {
            // Check if one set contains all items from the other
            if (componentA.getCPE().containsAll(componentB.getCPE()) || componentB.getCPE().containsAll(componentA.getCPE())) {
                // Then this isn't really a conflict
                falsePositive = true;
            }
            else {
                componentConflictTypes.add(ComponentConflictType.COMPONENT_CPE_MISMATCH);
            }
        }
        if (componentA.getPURL() != null && !componentA.getPURL().equals(componentB.getPURL())) {
            if (componentA.getPURL().containsAll(componentB.getPURL()) || componentB.getPURL().containsAll(componentA.getPURL())) {
                // Then this isn't really a conflict
            }
            else {
                componentConflictTypes.add(ComponentConflictType.COMPONENT_PURL_MISMATCH);
            }
        }
        if (componentA.getSWID() != null && !componentA.getSWID().equals(componentB.getSWID())) {
            if (componentA.getSWID().containsAll(componentB.getSWID()) || componentB.getSWID().containsAll(componentA.getSWID())) {
                // Then this isn't really a conflict
                falsePositive = true;

            }
            else {
                componentConflictTypes.add(ComponentConflictType.COMPONENT_SWID_MISMATCH);
            }
        }
        if (componentA.getSPDXID() != null && !componentA.getSPDXID().equals(componentB.getSPDXID())) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_SPDXID_MISMATCH);
        }
        if (componentA.getLicenses() != null && !componentA.getLicenses().equals(componentB.getLicenses())) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_LICENSE_MISMATCH);
        }
        if (componentConflictTypes.isEmpty() && !falsePositive) {
            componentConflictTypes.add(ComponentConflictType.COMPONENT_UNKNOWN_MISMATCH);
        }

    }

    /**
     * Construct a conflict between two components
     *
     * @param componentA First component
     * @param componentB Second component
     */
    public ComponentConflict(Component componentA, Component componentB) {
        componentConflictTypes = new HashSet<>();

        // Deep copy all information
        // In the case that the conflict is the component doesn't exist at all, then a or b may be null
        if (componentA != null) {
            this.componentA = new Component();
            this.componentA.copyFrom(componentA);
        }
        else {
            this.componentA = null;
            this.componentConflictTypes.add(ComponentConflictType.COMPONENT_NOT_FOUND);
        }

        if (componentB != null) {
            this.componentB = new Component();
            this.componentB.copyFrom(componentB);
        }
        else {
            this.componentB = null;
            this.componentConflictTypes.add(ComponentConflictType.COMPONENT_NOT_FOUND);
        }

        // Determine the type
        if (!this.componentConflictTypes.contains(ComponentConflictType.COMPONENT_NOT_FOUND)) {
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

    public Set<ComponentConflictType> getConflictTypes() {
        return componentConflictTypes;
    }

    @Override
    public String toString() {
        StringBuilder conflictString = new StringBuilder();
        // Check if we are only showing stuff that isn't in the component string
        boolean printEquals = (!componentConflictTypes.contains(ComponentConflictType.COMPONENT_PUBLISHER_MISMATCH))
                && (!componentConflictTypes.contains(ComponentConflictType.COMPONENT_NAME_MISMATCH))
                && (!componentConflictTypes.contains(ComponentConflictType.COMPONENT_VERSION_MISMATCH));
        if (printEquals) {
            // This means we only are showing internal component differences
            conflictString.append("= ").append(componentA.toString());
        }
        else {
            // Need to print a plus
            conflictString.append("+ ").append(componentA.toString());
        }

        // Print internal conflicts if they exist
        for (ComponentConflictType conflictType : componentConflictTypes) {
            // Don't show data that is being shown in the component string
            if (conflictType == ComponentConflictType.COMPONENT_NAME_MISMATCH
                    || conflictType == ComponentConflictType.COMPONENT_PUBLISHER_MISMATCH
                    || conflictType == ComponentConflictType.COMPONENT_VERSION_MISMATCH) {
                continue;
            }

            // Otherwise we need to print the conflict that occurs
            switch (conflictType) {
                case COMPONENT_CPE_MISMATCH:
                    conflictString.append(" CPE:\n");
                    // Get differences
                    Set<String> cpeA = new HashSet<>(componentA.getCPE());
                    Set<String> cpeB = new HashSet<>(componentB.getCPE());
                    cpeA.removeAll(componentB.getCPE());
                    cpeB.removeAll(componentA.getCPE());

                    for (String cpe : cpeA) {
                        conflictString.append("+ ").append(cpe).append("\n");
                    }

                    for (String cpe : cpeB) {
                        conflictString.append("- ").append(cpe).append("\n");
                    }

                    break;
                case COMPONENT_PURL_MISMATCH:
                    conflictString.append(" PURL:\n");
                    // Get differences
                    Set<String> purlA = new HashSet<>(componentA.getPURL());
                    Set<String> purlB = new HashSet<>(componentB.getPURL());
                    purlA.removeAll(componentB.getPURL());
                    purlB.removeAll(componentA.getPURL());

                    for (String purl : purlA) {
                        conflictString.append("+ ").append(purl).append("\n");
                    }

                    for (String purl : purlB) {
                        conflictString.append("- ").append(purl).append("\n");
                    }

                    break;
                case COMPONENT_SWID_MISMATCH:
                    conflictString.append(" SWID:\n");
                    // Get differences
                    Set<String> swidA = new HashSet<>(componentA.getSWID());
                    Set<String> swidB = new HashSet<>(componentB.getSWID());

                    swidA.removeAll(componentB.getSWID());
                    swidB.removeAll(componentA.getSWID());

                    for (String swid : swidA) {
                        conflictString.append("+ ").append(swid).append("\n");
                    }

                    for (String swid : swidB) {
                        conflictString.append("- ").append(swid).append("\n");
                    }
                    break;
                case COMPONENT_SPDXID_MISMATCH:
                    // TODO Skip this one for now because we need to first confirm both components are from SPDXID
//                    conflictString.append(" SPDXID: ").append(componentA.getSPDXID()).append(" vs ").append(componentB.getSPDXID());
                    break;
                case COMPONENT_LICENSE_MISMATCH:
                    conflictString.append(" License:\n");
                    // Get differences
                    Set<String> licenseA = new HashSet<>(componentA.getLicenses());
                    Set<String> licenseB = new HashSet<>(componentB.getLicenses());

                    licenseA.removeAll(componentB.getLicenses());
                    licenseB.removeAll(componentA.getLicenses());

                    for (String license : licenseA) {
                        conflictString.append("+ ").append(license).append("\n");
                    }

                    for (String license : licenseB) {
                        conflictString.append("- ").append(license).append("\n");
                    }
                    break;
                case COMPONENT_UNKNOWN_MISMATCH:
                    conflictString.append(" Other conflicts not displayed\n");
                    break;
                default:
                    break;
            }
        }

        // Print the component B if it exists or if they are not equal
        if (componentB != null && !printEquals) {
            conflictString.append("- ").append(componentB.toString());
        }

        return conflictString.toString();
    }
}
