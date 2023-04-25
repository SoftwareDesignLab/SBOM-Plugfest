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
 * @author Matt London
 */
public class Comparison {

    // Target SBOM (Ground Truth) for comparison.
    private SBOM targetSBOM;

    // List of SBOMs to stream against the Target SBOM for comparison
    private List<SBOM> sbomStream;

    // List of diff reports
    private List<DiffReport> diffReportList;

    // Set of comparisons
    private Map<String, HashSet<ComponentVersion>> comparisons;

    /**
     * Default constructor for Comparison
     *
     * @param stream a list of SBOMs
     */
    public Comparison(List<SBOM> stream) {
        this.targetSBOM = stream.get(0);
        this.sbomStream = stream.subList(1, stream.size());
        this.diffReportList = new ArrayList<>();
        this.comparisons = new HashMap<>();
    }

    /**
     * General driver for Comparison. Runs through the stream of SBOMs.
     * Each SBOM will be compared against the target and will go through assignComponents.
     */
    public void runComparison() {

        // Index of the current SBOM from the list
        int SBOM_index = 0;

        // Assign components for target SBOM
        assignComponents(targetSBOM, SBOM_index++);

        // Cycle through each sbom in the stream
        for(SBOM current_sbom : sbomStream) {

            // Run assignComponents for each sbom
            assignComponents(current_sbom, SBOM_index++);

            //generate a DiffReport for the current sbom and target sbom
            diffReportList.add(Comparer.generateReport(targetSBOM, current_sbom));

        }

    }

    /**
     * Cycles through all components in an SBOM and creates ComponentVersion objects.
     * These ComponentVersion objects are added to the comparisons list.
     *
     * @param current_sbom
     * @param SBOM_index
     */
    public void assignComponents(SBOM current_sbom, int SBOM_index) {

        // Loop through all components in SBOM and add them to comparisons list.
        for(Component current_component : current_sbom.getAllComponents()) {
            // Do not compare unpackaged components
            if (current_component.isUnpackaged()) {
                continue;
            }

            // Create a temporary ComponentVersion object for the current SBOM component
            ComponentVersion temporary_cv = generateComponentVersion(current_component, SBOM_index);

            // If the comparisons collection contains
            if(comparisons.containsKey(current_component.getName())) {

                // Get that component's component version collection
                Set<ComponentVersion> current_cv_list = comparisons.get(current_component.getName());

                // There should only be one ComponentVersion that matches the current component, or none
                ComponentVersion matching = null;
                for (ComponentVersion cv : current_cv_list) {
                    if (Objects.equals(cv.getComponentVersion(), current_component.getVersion())) {
                        matching = cv;
                        break;
                    }
                }

                // If there are no matching ComponentVersion objects in the Set for that package name
                // Else, find all matching ComponentVersion objects and add relevant information
                if (matching == null) {

                    // Get the old collection for this
                    HashSet<ComponentVersion> new_set = comparisons.get(current_component.getName());

                    // Add this version in
                    new_set.add(temporary_cv);

                } else {

                    // In this case we have a matching version so we should merge
                    matching.addAppearance(SBOM_index);

                    // Cycle through CPEs
                    // Get the set
                    Set<UniqueIdOccurrence> cpe_set = matching.getCPEs();
                    for (String cpe : current_component.getCpes()) {
                        boolean added = false;
                        // Look for matches
                        for (UniqueIdOccurrence existing_cpe: cpe_set) {
                            if (Objects.equals(existing_cpe.getUniqueId(), cpe)) {
                                existing_cpe.addAppearance(SBOM_index);
                                added = true;
                                break;
                            }
                        }

                        // In this case we need to create a new unique ID occurrence
                        if (!added) {
                            UniqueIdOccurrence new_cpe_uid = new UniqueIdOccurrence(cpe, UniqueIdentifierType.CPE);
                            new_cpe_uid.addAppearance(SBOM_index);
                            matching.addCPE(new_cpe_uid);
                        }
                    }

                    // Repeat for PURL
                    Set<UniqueIdOccurrence> purl_set = matching.getPURLs();
                    for (PURL purl : current_component.getPurls()) {
                        boolean added = false;
                        // Look for matches
                        for (UniqueIdOccurrence existing_purl: purl_set) {
                            if (Objects.equals(existing_purl.getUniqueId(), purl.getPURLString())) {
                                existing_purl.addAppearance(SBOM_index);
                                added = true;
                                break;
                            }
                        }

                        // In this case we need to create a new unique ID occurrence
                        if (!added) {
                            UniqueIdOccurrence new_purl_uid = new UniqueIdOccurrence(purl.getPURLString(), UniqueIdentifierType.PURL);
                            new_purl_uid.addAppearance(SBOM_index);
                            matching.addPURL(new_purl_uid);
                        }
                    }

                    // Repeat for SWID
                    Set<UniqueIdOccurrence> swid_set = matching.getSWIDs();
                    for (String swid : current_component.getSwids()) {
                        boolean added = false;
                        // Look for matches
                        for (UniqueIdOccurrence existing_swid: swid_set) {
                            if (Objects.equals(existing_swid.getUniqueId(), swid)) {
                                existing_swid.addAppearance(SBOM_index);
                                added = true;
                                break;
                            }
                        }

                        // In this case we need to create a new unique ID occurrence
                        if (!added) {
                            UniqueIdOccurrence new_swid_uid = new UniqueIdOccurrence(swid, UniqueIdentifierType.SWID);
                            new_swid_uid.addAppearance(SBOM_index);
                            matching.addSWID(new_swid_uid);
                        }
                    }

                }

            } else {
                // In this case the name has not been encountered before
                comparisons.put(current_component.getName(), new HashSet<>(Arrays.asList(temporary_cv)));

            }

        }

    }

    /**
     * Builds a ComponentVersion object for the current component including all IDs.
     *
     * @param component The current component
     * @param SBOM_index The index of this SBOM in the whole set
     * @return The ComponentVersion object
     */
    private ComponentVersion generateComponentVersion(Component component, int SBOM_index) {

        // Create the new ComponentVersion
        ComponentVersion new_cv = new ComponentVersion(component.getName(), component.getVersion());
        new_cv.addAppearance(SBOM_index);

        // Cycle through CPEs
        for (String cpe : component.getCpes()) {

            // Create new UniqueIDOccurrence object for the CPE, then add it to the ComponentVersion object
            UniqueIdOccurrence new_cpe_uid = new UniqueIdOccurrence(cpe, UniqueIdentifierType.CPE);
            new_cpe_uid.addAppearance(SBOM_index);
            new_cv.addCPE(new_cpe_uid);

        }
        for (PURL purl : component.getPurls()) {

            // Create new UniqueIDOccurrence object for the PURL, then add it to the ComponentVersion object
            UniqueIdOccurrence new_purl_uid = new UniqueIdOccurrence(purl.toString(), UniqueIdentifierType.PURL);
            new_purl_uid.addAppearance(SBOM_index);
            new_cv.addPURL(new_purl_uid);

        }
        for (String swid : component.getSwids()) {

            // Create new UniqueIDOccurrence object for the SWID, then add it to the ComponentVersion object
            UniqueIdOccurrence new_swid_uid = new UniqueIdOccurrence(swid, UniqueIdentifierType.SWID);
            new_swid_uid.addAppearance(SBOM_index);
            new_cv.addSWID(new_swid_uid);

        }

        return new_cv;

    }

    ///
    /// Getters
    //

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