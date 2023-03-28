package plugfest.tooling.differ;

import plugfest.tooling.sbom.ComponentConflict;
import plugfest.tooling.sbom.SBOMConflict;

import java.util.Set;

public class DiffReport {
    private SBOMConflict sbomConflict;
    private Set<ComponentConflict> componentConflicts;

    public DiffReport(SBOMConflict sbomConflict, Set<ComponentConflict> componentConflicts) {
        this.sbomConflict = sbomConflict;
        this.componentConflicts = componentConflicts;
    }

    public SBOMConflict getSbomConflict() {
        return sbomConflict;
    }

    public Set<ComponentConflict> getComponentConflicts() {
        return componentConflicts;
    }

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
