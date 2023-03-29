package plugfest.tooling.differ;

import plugfest.tooling.sbom.SBOM;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOMConflict;
import plugfest.tooling.sbom.ComponentConflict;

import java.util.HashMap;
import java.util.HashSet;

import java.util.Set;

/**
 * Class to generate the difference report between two SBOMs
 *
 * @author Matt London
 */
public class Comparer {
    /**
     * Generate a report of the differences between two SBOMs
     *
     * @param aSBOM SBOM to compare to
     * @param bSBOM SBOM to compare
     * @return Report of the differences between the two SBOMs
     */
    public static DiffReport generateReport(SBOM aSBOM, SBOM bSBOM) {
        Set<ComponentConflict> componentConflicts = new HashSet<>();

        Set<Component> aComponents = aSBOM.getAllComponents();
        Set<Component> bComponents = bSBOM.getAllComponents();

        Set<String> aComponentNames = new HashSet<>();
        Set<String> bComponentNames = new HashSet<>();

        for (Component aComponent : aComponents) {
            aComponentNames.add(aComponent.getName());
        }

        for (Component bComponent : bComponents) {
            bComponentNames.add(bComponent.getName());
        }

        // Add all acomponents to a map
        HashMap<String, Component> aComponentMap = new HashMap<>();
        for (Component aComponent : aComponents) {
            aComponentMap.put(aComponent.getName(), aComponent);
        }

        // Now loop through and check if all b components are there
        for (Component bComponent : bComponents) {
            if (!aComponentMap.containsKey(bComponent.getName())) {
                // Add a new component conflict
                ComponentConflict conflict = new ComponentConflict(null, bComponent);
                componentConflicts.add(conflict);
            }
            else {
                // Compare the two
                ComponentConflict conflict = new ComponentConflict(aComponentMap.get(bComponent.getName()), bComponent);
                if (conflict.getConflictTypes().size() > 0) {
                    componentConflicts.add(conflict);
                }
            }
        }

        // Now look for components that exist in a but not b
        for (String aComponent : aComponentNames) {
            if (!bComponentNames.contains(aComponent)) {
                // Then we have a component in a that is not in b
                ComponentConflict conflict = new ComponentConflict(aComponentMap.get(aComponent), null);
                componentConflicts.add(conflict);
            }
        }

        // Now compare trivial sbom data
        SBOMConflict sbomConflict = new SBOMConflict(aSBOM, bSBOM);

        return new DiffReport(sbomConflict, componentConflicts);
    }
}
