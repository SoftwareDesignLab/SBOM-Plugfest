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

public class TranslatorCDXJSON {

    public static SBOM translatorCDXJSON(String file_path) throws IOException, ParseException {

        // Internal SBOM Object
        SBOM sbom;

        // Top level component for SBOM's dependencyTree
        Component top_component = null;

        // Initialize JSON Parser
        JsonParser parser = new JsonParser();

        // Use JSON Parser to parse cdx.json file and store into cyclonedx Bom Object
        Bom json_sbom = parser.parse(new File(file_path));

        try {
            // TODO: Add signature's later. CycloneDX BOM does a good job at dealing with them.
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
            System.err.println("Error in creating internal SBOM for " + file_path);
            return null;
        }

        try {
            top_component = new plugfest.tooling.sbom.Component(
                    json_sbom.getMetadata().getComponent().getName(),
                    json_sbom.getMetadata().getComponent().getPublisher(),
                    json_sbom.getMetadata().getComponent().getVersion(),
                    json_sbom.getMetadata().getComponent().getBomRef()
            );
            sbom.addComponent(null, top_component);
        } catch(Exception e) {
            System.err.println("Could not create top-level component from MetaData. If this is ");
        }

        HashMap<String, Component> components = new HashMap<>();


        for(org.cyclonedx.model.Component cdx_component : json_sbom.getComponents()) {

            if( cdx_component != null ) {
                PURL purl = cdx_component.getPurl() == null ? null : new PURL(cdx_component.getPurl());
                Component new_component = new Component(
                        cdx_component.getName(),
                        cdx_component.getPublisher(),
                        cdx_component.getVersion(),
                        Collections.singleton(cdx_component.getCpe()),
                        Collections.singleton(purl),
                        Collections.singleton(String.valueOf(cdx_component.getSwid()))
                );
                try {
                    new_component.setLicenses(new HashSet<>(Arrays.asList(cdx_component.getLicenseChoice().getExpression())));
                } catch (NullPointerException e) {
                    System.err.println("No licenses found for " + new_component.getName() + ". bom-ref: " + cdx_component.getBomRef());
                } catch (Exception e) {
                    System.err.println("An error occurred while getting licenses: \n");
                    e.printStackTrace();
                }

                new_component.setSPDXID(cdx_component.getBomRef());

                components.put(new_component.getSPDXID(), new_component);

            }


        }

        try {
            Map<String, List<Dependency>> dependencies = json_sbom.getDependencies()
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    Dependency::getRef,
                                    Dependency::getDependencies
                            )
                    );

            dependencyBuilder(dependencies, components, top_component, sbom, null);
        } catch (NullPointerException e) {
            System.err.println("Could not find dependencies. Dependency Tree will not be build for " + file_path);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error when processing dependency tree: " + file_path);
        }

        return sbom;
    }

    public static void dependencyBuilder(Map dependencies, HashMap components, Component parent, SBOM sbom, Set<String> visited) {

        // If top component is null, return. There is nothing to process.
        if (parent == null) { return; }

        if (visited != null) {
            // Add this parent to the visited set
            visited.add(parent.getSPDXID());
        }

        // Get the parent's dependencies as a list
        String parent_id = parent.getSPDXID();
        List<Dependency> children_SPDX = (List<Dependency>) dependencies.get(parent_id);
        if( children_SPDX == null ) { return; }
        System.out.println("OK");
        // Cycle through each dependency the parent component has
        for (Dependency child_SPDX: children_SPDX) {
            // Retrieve the component the parent has a dependency for
            Component child = (Component) components.get(child_SPDX.getRef());

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
