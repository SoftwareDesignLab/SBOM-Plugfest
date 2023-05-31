package org.nvip.plugfest.tooling.translator;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.parsers.JsonParser;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.translator.TranslatorCore;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.uids.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;
import java.util.stream.Collectors;

/**
 * file: TranslatorCDXJSON.java
 * Coverts SPDX SBOMs into internal SBOM objects.
 * Compatible with CDX 1.4 JSON SBOMs
 *
 * @author Tyler Drake
 */
public class TranslatorCDXJSON extends TranslatorCore {
    public TranslatorCDXJSON() {
        super("json");
    }

    /**
     * Converts a file into an internal SBOM object
     *
     * @param fileContents String of file contents
     * @param file_path Path to file
     * @return internal SBOM object
     */
    @Override
    protected SBOM translateContents(String fileContents, String file_path) throws ParseException {

        // Initialize JSON Parser
        JsonParser parser = new JsonParser();

        // Use JSON Parser to parse cdx.json file and store into cyclonedx Bom Object
        Bom json_sbom = parser.parse(fileContents.getBytes());

        // TODO these are essential fields, throw an actual error if any of these are null
        bom_data.put("format", json_sbom.getBomFormat());
        bom_data.put("specVersion", json_sbom.getSpecVersion());
        bom_data.put("sbomVersion", String.valueOf(json_sbom.getVersion()));
        bom_data.put("serialNumber", json_sbom.getSerialNumber());

        // Ensure metadata is not null before we begin querying it
        Metadata metadata = json_sbom.getMetadata();
        if(metadata != null) {
            bom_data.put("author", json_sbom.getMetadata().getAuthors() == null ?
                    json_sbom.getMetadata().getTools().toString() : json_sbom.getMetadata().getAuthors().toString());
            bom_data.put("timestamp" , json_sbom.getMetadata().getTimestamp().toString());

            // Top component analysis (check if not null as well)
            org.cyclonedx.model.Component topComponent = metadata.getComponent();
            if(topComponent != null) {
                product_data.put("name", topComponent.getName());
                product_data.put("publisher", topComponent.getPublisher());
                product_data.put("version", topComponent.getVersion());
                product_data.put("id", topComponent.getBomRef());
            }
        }

        this.createSBOM();

        // Create new collection of components
        HashMap<String, Component> components = new HashMap<>();

        // Loop through all components in cyclonedx component list
        for(org.cyclonedx.model.Component cdx_component : json_sbom.getComponents()) {

            if( cdx_component != null ) {

                // Get CPE, PURL, and SWIDs
                String cpe = cdx_component.getCpe() == null ? null : cdx_component.getCpe();
                PURL purl = null;
                try {
                    purl = new PURL(cdx_component.getPurl());
                } catch (Exception ignored){
                }
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
                    // Getting a NullPointerException on licenses is fine. It just means the component had none.
                } catch (Exception e) {
                    // This may be an actual error
                    Debug.log(Debug.LOG_TYPE.ERROR, "An error occurred while getting licenses:");
                    Debug.log(Debug.LOG_TYPE.EXCEPTION, e.getMessage());
//                    e.printStackTrace();
                }

                // Set the component's unique ID
                new_component.setUniqueID(cdx_component.getBomRef());

                // Add component to component list
                this.loadComponent(new_component.getUniqueID(), new_component);

                // If a top component doesn't exist, make this new component the top component
                this.product = product == null ? new_component : product;

            }

        }

        // Add the top component to the sbom
        if(this.sbom.getAllComponents().size() == 0) { this.sbom.addComponent(null, product); }

        // Create dependency collection
        //Map<String, List<String>> dependencies;
        try {
            // Attempt to get all dependencies from CycloneDX Object
            dependencies = json_sbom.getDependencies()
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    Dependency::getRef,
                                    x -> {
                                        // Returns dependencies as strings
                                        return x.getDependencies().stream().map(
                                                y -> y.getRef()).collect(
                                                Collectors.toCollection(ArrayList::new));
                                    },
                                    (x,y) -> y,
                                    HashMap::new
                            )
                    );
        } catch (NullPointerException nullPointerException) {
            // If dependencies fail, default
            Debug.log(Debug.LOG_TYPE.ERROR, "Could not find dependencies from CycloneDX Object. " +
                    "Defaulting all components to point to head component. File: " + file_path);
            dependencies.put(
                    this.product.getUniqueID(),
                    components.values().stream().map(x->x.getUniqueID()).collect(Collectors.toCollection(ArrayList::new))
            );
        }

        // If the dependency list isn't empty, call dependencyBuilder to construct dependencyTree
        // Otherwise, default the dependencyTree by adding all subcomponents as children to the top component
        if( dependencies != null ) {
            try {
                this.dependencyBuilder(components, this.product, null);
            } catch (Exception e) {
                Debug.log(Debug.LOG_TYPE.WARN, "Error building dependency tree. Dependency tree may be incomplete " +
                        "for: " + file_path);
            }
        }

        this.defaultDependencies(this.product);

        return this.sbom;

    }

}