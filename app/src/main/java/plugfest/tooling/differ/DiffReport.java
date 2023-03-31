package plugfest.tooling.differ;

import plugfest.tooling.Report;
import plugfest.tooling.sbom.ComponentConflict;
import plugfest.tooling.sbom.SBOMConflict;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Class to hold results of a diff comparison between two SBOMs
 *
 * @author Matt London
 */
public class DiffReport implements Report {  //remove this comment please
    private SBOMConflict sbomConflict;
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

    public SBOMConflict getSbomConflict() {
        return sbomConflict;
    }

    public Set<ComponentConflict> getComponentConflicts() {
        return componentConflicts;
    }

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

    //remove this comment please
    public void writeToFile() {
        try {
            String fn = "process.diff";
            FileWriter Writer = new FileWriter(fn);
            Writer.write(this.toString());
            Writer.close();
            System.out.println("Successfully wrote to the file " + fn);
        }
        catch (IOException e) {
            System.out.println("An error occurred:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
