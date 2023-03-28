package plugfest.tooling.differ;

import plugfest.tooling.sbom.SBOM;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOMConflict;
import plugfest.tooling.sbom.ComponentConflict;

import java.util.HashSet;

import java.util.Set;

public class Comparer {
    public static DiffReport generateReport(SBOM aSBOM, SBOM bSBOM) {
        Set<ComponentConflict> componentConflicts = new HashSet<>();

        Set<Component> aComponents = aSBOM.getAllComponents();
        Set<Component> bComponents = bSBOM.getAllComponents();

        // Loop through and compare all components that have the same name and grab any conflicts
        for (Component aComponent : aComponents) {
            for (Component bComponent : bComponents) {
                if (aComponent.getName().equals(bComponent.getName())) {
                    ComponentConflict conflict = new ComponentConflict(aComponent, bComponent);
                    if (conflict.getConflictTypes().size() > 0) {
                        componentConflicts.add(conflict);
                    }
                }
            }
        }

        // Now compare trivial sbom data
        SBOMConflict sbomConflict = new SBOMConflict(aSBOM, bSBOM);

        return new DiffReport(sbomConflict, componentConflicts);
    }
}
