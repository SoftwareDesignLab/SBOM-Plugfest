package org.nvip.plugfest.tooling.differ;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;

/**
 * file: Comparison.java
 *
 * Comparison pipeline class used by the frontend to generate
 * DiffReports from a target SBOM and a list of SBOMs.
 *
 * This class may also generate a list of component comparisons with
 * ComponentVersion objects.
 *
 * @author Tyler Drake
 */
public class Comparison {

    // Target SBOM (Ground Truth) for comparison.
    private SBOM targetSBOM;

    // List of diff reports
    private List<DiffReport> diffReportList;

    // Set of comparisons
    private Map<String, HashSet<ComponentVersion>> comparisons;

    public Comparison(SBOM target) {
        this.targetSBOM = target;
        this.diffReportList = new ArrayList<>();
        this.comparisons = new HashMap<>();
    }

    public void runComparison(List<SBOM> stream) {

        // Cycle through each sbom in the stream
        for(SBOM current_sbom : stream) {

            // Run assignComponents for each sbom
            assignComponents(current_sbom);

            //generate a DiffReport for the current sbom and target sbom
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
                Set<ComponentVersion> current_cv_list = comparisons.get(current_component.getName());

                // Get all ComponentVersions that match the temporary ComponentVersion's version
                List<ComponentVersion> matching_cv_list = current_cv_list
                        .stream()
                        .filter(x -> x.getComponentVersion().contains(current_component.getVersion()))
                        .toList();

                // If there are no matching ComponentVersion objects in the Set for that package name
                // Else, find all matching ComponentVersion objects and add relevant information
                if (matching_cv_list.isEmpty()) {

                    // Get the old collection for this
                    HashSet<ComponentVersion> new_set = comparisons.get(current_component.getName());

                    // Remove the old set for this key
                    comparisons.remove(current_component.getName());

                    // Add the new ComponentVersion to the set
                    new_set.add(temporary_cv);

                    // Add the new set with the newly added ComponentVersion to the collection
                    comparisons.put(current_component.getName(), new_set);

                } else {

                    // Get the old collection for this
                    HashSet<ComponentVersion> new_set = comparisons.get(current_component.getName());

                    // Remove the old set for this key
                    comparisons.remove(current_component.getName());

                    // Take all CPEs, PURLs, and SWIDs, that don't exist in the original ComponentVersion and add them in
                    for (ComponentVersion matching_cv : matching_cv_list) {

                        // Remove the old matching ComponentVersion object from the set
                        new_set.remove(matching_cv);

                        // Update the ComponentVersion object with the extra CPEs
                        temporary_cv.getCPES().iterator().forEachRemaining(cpe -> matching_cv.addCPE(cpe));

                        // Update the ComponentVersion object with extra PURLs
                        temporary_cv.getPURLS().iterator().forEachRemaining(purl -> matching_cv.addPURL(purl));

                        // Update the ComponentVersion object with extra SWIDs
                        temporary_cv.getSWIDS().iterator().forEachRemaining(swid -> matching_cv.addSWID(swid));

                        // Add it back into the map
                        new_set.add(matching_cv);

                    }

                    // Add the new ComponentVersion to the set
                    new_set.add(temporary_cv);

                    // Add the new set with the newly added ComponentVersion to the collection
                    comparisons.put(current_component.getName(), new_set);

                }

            } else {
                // Add a new entry to the comparisons list along with the new ComponentVersion object
                comparisons.put(temporary_cv.getComponentName(), new HashSet<>(Arrays.asList(temporary_cv)));
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

    public Map<String, HashSet<ComponentVersion>> getComparisons() {

        return this.comparisons;

    }

}