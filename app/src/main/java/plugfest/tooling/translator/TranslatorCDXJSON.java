package plugfest.tooling.translator;
import plugfest.tooling.sbom.*;


import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.*;
import org.cyclonedx.parsers.JsonParser;

import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;


import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * file: TranslatorCDXJSON.java
 * Coverts SPDX SBOMs into internal SBOM objects.
 * Compatible with CDX 1.4 JSON SBOMs
 *
 * @author Tyler Drake
 */
public class TranslatorCDXJSON {

    /**
     * Coverts CDX JSON SBOMs into internal SBOM objects
     *
     * @param file_path Path to CDX JSON SBOM
     * @return internal SBOM object
     * @throws ParseException
     */
    public static SBOM translatorCDXJSON(String file_path) throws ParseException {

        // Internal SBOM Object
        SBOM sbom;

        // Top level component for SBOM's dependencyTree
        Component top_component = null;

        // Initialize JSON Parser
        JsonParser parser = new JsonParser();

        // Use JSON Parser to parse cdx.json file and store into cyclonedx Bom Object
        Bom json_sbom = parser.parse(new File(file_path));

        // Attempt to create the SBOM object. If information isn't found, cancel process and return a null object,
        try {
            // TODO: Add signature's later. CycloneDX BOM does a good job at dealing with them.
            // Create new SBOM object with require data
            sbom = new SBOM(
                    json_sbom.getBomFormat(),
                    json_sbom.getSpecVersion(),
                    String.valueOf(json_sbom.getVersion()),
                    json_sbom.getMetadata().getAuthors() == null
                            ? json_sbom.getMetadata().getTools().toString()
                            : json_sbom.getMetadata().getAuthors().toString(),
                    json_sbom.getSerialNumber(),
                    json_sbom.getMetadata().getTimestamp().toString(),
                    null
            );
        } catch (Exception e) {
            System.err.println("Error in creating internal SBOM for: " + file_path );
            return null;
        }

        // Attempt to create the top component. If unable to create the component, print an error and continue.
        try {
            // Get top component information from MetaData
            org.cyclonedx.model.Component top_component_meta = json_sbom.getMetadata().getComponent();

            // Create top component and add it to SBOM object
            top_component = new plugfest.tooling.sbom.Component(
                    top_component_meta.getName(),
                    top_component_meta.getPublisher(),
                    top_component_meta.getVersion(),
                    top_component_meta.getBomRef()
            );
            sbom.addComponent(null, top_component);

        } catch(Exception e) {
            System.err.println("Could not create top-level component from MetaData.\n " +
                    "If this is not expected please check SBOM file: " + file_path);
        }

        // Create new collection of components
        HashMap<String, Component> components = new HashMap<>();

        // Loop through all components in cyclonedx component list
        for(org.cyclonedx.model.Component cdx_component : json_sbom.getComponents()) {

            if( cdx_component != null ) {

                // Get CPE, PURL, and SWIDs
                String cpe = cdx_component.getCpe() == null ? null : cdx_component.getCpe();
                PURL purl = cdx_component.getPurl() == null ? null : new PURL(cdx_component.getPurl());
                String swid = cdx_component.getSwid() == null ? null : String.valueOf(cdx_component.getSwid());

                // Create new component with a name, publisher, version along with CPEs/PURLs/SWIDs
                Component new_component = new Component(
                        cdx_component.getName(),
                        cdx_component.getPublisher(),
                        cdx_component.getVersion(),
                        Collections.singleton(cpe),
                        Collections.singleton(purl),
                        Collections.singleton(swid)
                );

                // Attempt to get licenses. If no licenses found put out error message and continue.
                try {
                    new_component.setLicenses(new HashSet<>(Arrays.asList(cdx_component.getLicenseChoice().getExpression())));
                } catch (NullPointerException e) {
                    System.err.println("No licenses found for " + new_component.getName() + ". bom-ref: " + cdx_component.getBomRef());
                } catch (Exception e) {
                    System.err.println("An error occurred while getting licenses: \n");
                    e.printStackTrace();
                }

                // Set the component's unique ID
                new_component.setSPDXID(cdx_component.getBomRef());

                // Add component to component list
                components.put(new_component.getSPDXID(), new_component);

            }

        }

        // Attempt to build dependency tree
        try {
            // Reformat list of dependencies
            Map<String, List<Dependency>> dependencies = json_sbom.getDependencies()
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    Dependency::getRef,
                                    Dependency::getDependencies
                            )
                    );

            // Call dependency builder to build the dependency tree
            dependencyBuilder(dependencies, components, top_component, sbom, null);
        } catch (NullPointerException e) {
            System.err.println("Could not find dependencies. Dependency Tree will not be built for: " + file_path);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error when processing dependency tree: " + file_path);
            e.printStackTrace();
        }

        // Return SBOM object
        return sbom;
    }

    /**
     * A simple recursive function to build a dependency tree out of the CDX JSON SBOM
     *
     * @param dependencies  A map containing packaged components with their CDX bom-refs, pointing to dependencies
     * @param components    A map containing each Component with their bom-ref ID as a key
     * @param parent        Parent component to have dependencies connected to
     * @param sbom          The SBOM object
     */
    public static void dependencyBuilder(Map dependencies, HashMap components, Component parent, SBOM sbom, Set<String> visited) {

        // If top component is null, return. There is nothing to process.
        if (parent == null) { return; }

        if (visited != null) {
            // Add this parent to the visited set
            visited.add(parent.getSPDXID());
        }

        // Get the parent's dependencies as a list
        String parent_id = parent.getSPDXID();
        List<Dependency> children_ref = (List<Dependency>) dependencies.get(parent_id);

        // If there are no
        if( children_ref == null ) { return; }

        // Cycle through each dependency the parent component has
        for (Dependency child_ref: children_ref) {
            // Retrieve the component the parent has a dependency for
            Component child = (Component) components.get(child_ref.getRef());

            // If component is already in the dependency tree, add it as a child to the parent
            // Else, add it to the dependency tree while setting the parent
            if(sbom.hasComponent(child.getUUID())) {
                parent.addChild(child.getUUID());
            } else {
                sbom.addComponent(parent.getUUID(), child);
            }

            if (visited == null) {
                // This means we are in the top level component
                // Pass in a new hashset instead of the visited set
                visited = new HashSet<>();
                dependencyBuilder(dependencies, components, child, sbom, new HashSet<>());
            }
            else {
                // Only explore if we haven't already visited this component
                if (!visited.contains(child.getSPDXID())) {
                    // Pass the child component as the new parent into dependencyBuilder
                    dependencyBuilder(dependencies, components, child, sbom, visited);
                }
            }
        }
    }
}
