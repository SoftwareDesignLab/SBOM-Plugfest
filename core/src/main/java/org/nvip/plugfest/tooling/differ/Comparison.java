package org.nvip.plugfest.tooling.differ;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;

public class Comparison {

    // Target SBOM (Ground Truth) for comparison.
    private SBOM targetSBOM;

    // List of diff reports
    private List<DiffReport> diffReportList;

    // Set of comparisons
    private Multimap<String, ComponentVersion> comparisons;

    public Comparison(SBOM target) {
        this.targetSBOM = target;
        this.diffReportList = new ArrayList<>();
        this.comparisons = HashMultimap.create();
    }

    public void runComparison(List<SBOM> stream) {
        for(SBOM current_sbom : stream) {
            assignComponents(current_sbom);
            diffReportList.add(Comparer.generateReport(targetSBOM, current_sbom));
        }
    }

    public void assignComponents(SBOM current_sbom) {

        // Loop through all components in SBOM and add them to comparisons list.
        for(Component current_component : current_sbom.getAllComponents()) {

            // Create a temporary ComponentVersion object for the current SBOM component
            ComponentVersion temporary_cv = generateComponentVersion(current_component);

            // If the comparisons collection contains
            if(comparisons.containsKey(current_component.getName())) {

                // Get that component's component version collection
                Collection<ComponentVersion> current_cv_list = comparisons.get(current_component.getName());

                // Get all ComponentVersions that match the temporary ComponentVersion's version
                List<ComponentVersion> matching_cv_list = current_cv_list
                        .stream()
                        .filter(x -> x.getComponentVersion().contains(current_component.getVersion()))
                        .toList();

                // If there are no matching ComponentVersion objects in the Set for that package name
                // Else, find all matching ComponentVersion objects and add relevant information
                if (matching_cv_list.isEmpty()) {

                    // Add the new ComponentVersion object to the package name it matches in the comparisons collection
                    comparisons.put(current_component.getName(), temporary_cv);

                } else {

                    // Take all CPEs, PURLs, and SWIDs, that don't exist in the original ComponentVersion and add them in
                    for (ComponentVersion matching_cv : matching_cv_list) {

                        // Remove the old matching ComponentVersion object from the Map
                        comparisons.remove(current_component.getName(), matching_cv);

                        // Update the ComponentVersion object with the extra CPEs
                        temporary_cv.getCPES().iterator().forEachRemaining(cpe -> matching_cv.addCPE(cpe));

                        // Update the ComponentVersion object with extra PURLs
                        temporary_cv.getPURLS().iterator().forEachRemaining(purl -> matching_cv.addPURL(purl));

                        // Update the ComponentVersion object with extra SWIDs
                        temporary_cv.getSWIDS().iterator().forEachRemaining(swid -> matching_cv.addSWID(swid));

                        // Add it back into the map
                        comparisons.put(current_component.getName(), matching_cv);

                    }

                }


            } else {
                // Add a new entry to the comparisons list along with the new ComponentVersion object
                comparisons.put(temporary_cv.getComponentName(), temporary_cv);
            }
        }
    }

    private ComponentVersion generateComponentVersion(Component component) {

        // Create the new ComponentVersion
        ComponentVersion new_cv = new ComponentVersion(component.getName(), component.getVersion());

        // Cycle through CPEs
        for (String cpe : component.getCpes()) {

            // Create new UniqueIDOccurrence object for the CPE, then add it to the ComponentVersion object
            UniqueIdOccurrence new_cpe_uid = new UniqueIdOccurrence(cpe, UniqueIdentifierType.CPE);
            new_cv.addCPE(new_cpe_uid);

        }
        for (PURL purl : component.getPurls()) {

            // Create new UniqueIDOccurrence object for the PURL, then add it to the ComponentVersion object
            UniqueIdOccurrence new_purl_uid = new UniqueIdOccurrence(purl.toString(), UniqueIdentifierType.PURL);
            new_cv.addPURL(new_purl_uid);

        }
        for (String swid : component.getSwids()) {

            // Create new UniqueIDOccurrence object for the SWID, then add it to the ComponentVersion object
            UniqueIdOccurrence new_swid_uid = new UniqueIdOccurrence(swid, UniqueIdentifierType.SWID);
            new_cv.addSWID(new_swid_uid);
        }
        return new_cv;
    }

    public SBOM getTargetSBOM() {
        return this.targetSBOM;
    }

    public List<DiffReport> getDiffReports() {
        return this.diffReportList;
    }

    public Multimap<String, ComponentVersion> getComparisons() {
        return this.comparisons;
    }

}
