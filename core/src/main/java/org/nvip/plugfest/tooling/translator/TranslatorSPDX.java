package org.nvip.plugfest.tooling.translator;

import org.cyclonedx.exception.ParseException;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.uids.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * file: TranslatorSPDX.java
 * Coverts SPDX SBOMs into internal SBOM objects.
 * Compatible with SPDX 2.2 and SPDX 2.3
 *
 * @author Tyler Drake
 * @author Matt London
 * @author Ian Dunn
 */
public class TranslatorSPDX extends TranslatorCore {

    /**
     * Constants
     */
    private static final String TAG = "#####";

    private static final String UNPACKAGED_TAG = "##### Unpackaged files";

    private static final String PACKAGE_TAG = "##### Package";

    private static final String RELATIONSHIP_TAG = "##### Relationships";

    private static final String EXTRACTED_LICENSE_TAG = "##### Extracted"; // starts with

    private static final String EXTRACTED_LICENSE_ID = "LicenseID";

    private static final String EXTRACTED_LICENSE_NAME = "LicenseName";

    private static final String RELATIONSHIP_KEY = "Relationship";

    private static final String SPEC_VERSION_TAG = "SPDXVersion";

    private static final String ID_TAG = "SPDXID";

    private static final String TIMESTAMP_TAG = "Created";

    private static final String DOCUMENT_NAMESPACE_TAG = "DocumentNamespace";

    private static final String AUTHOR_TAG = "Creator";

    // Used as an identifier for main SBOM information. Sometimes used as reference in relationships to show header contains main component.
    private static final String DOCUMENT_REFERENCE_TAG = "SPDXRef-DOCUMENT";
    private static final String EXTERNAL_REFERENCE_TAG = "ExternalRef";

    private static final Pattern TAG_VALUE_PATTERN = Pattern.compile("(\\S+): (.+)");
    private static final Pattern EXTERNAL_REF_PATTERN = Pattern.compile("ExternalRef: (\\S*) (\\S*) (\\S*)");

    public TranslatorSPDX() {
        super("spdx");
    }


    /**
     * Coverts SPDX SBOMs into internal SBOM objects by its contents
     *
     * @param fileContents Contents of the SBOM to translate
     * @param file_path Original path to SPDX SBOM
     * @return internal SBOM object from contents
     */
    // TODO: Break into sub-methods
    @Override
    protected SBOM translateContents(String fileContents, String file_path) throws TranslatorException {

        // Top level component information
        String product_id = "";

        String fileName = file_path.substring(file_path.lastIndexOf('/') + 1).trim().toLowerCase(); // todo make sure this works

        // Collection for components, packaged and unpackaged
        // Key = SPDXID , Value = Component
        HashMap<String, Component> components = new HashMap<>();

        // Collection of packages, used  for adding head components to top component if in the header (SPDXRef-DOCUMENT)
        // Value (SPDX_ID)
        ArrayList<String> packages = new ArrayList<>();

        // Map of external licenses mapping ID to name
        Map<String, String> externalLicenses = new HashMap<>();

        bom_data.put("sbomVersion", "1");
        bom_data.put("format", "spdx");

        /*
            Top level SBOM data (metadata, etc.)
        */
        fileContents = fileContents.replaceAll("\r", ""); // Remove carriage return characters if windows
        int firstIndex = fileContents.indexOf(TAG); // Find first index of next "section"
        String header;

        // If no tags found, assume the header is the only part of the file
        if (firstIndex == -1) header = fileContents;
        else {
            header = fileContents.substring(0, firstIndex - 2); // Remove newlines as well
            fileContents = fileContents.substring(firstIndex); // Remove all header info from fileContents
        }

        // Process header TODO throw error if required fields are not found. Create enum with all tags?
        Matcher m = TAG_VALUE_PATTERN.matcher(header);
        while(m.find()) {
            switch (m.group(1)) {
                case DOCUMENT_NAMESPACE_TAG -> bom_data.put("serialNumber", m.group(2));
                case SPEC_VERSION_TAG -> bom_data.put("specVersion", m.group(2));
                case AUTHOR_TAG -> {
                    if (!bom_data.containsKey("author")) bom_data.put("author", m.group(2));
                    else bom_data.put("author", bom_data.get("author") + " " + m.group(2));
                }
                case ID_TAG -> bom_data.put("id", m.group(1));
                case TIMESTAMP_TAG -> bom_data.put("timestamp", m.group(2));
                default -> bom_data.put(m.group(1), m.group(2));
            }
        }

        /*
            Extracted Licensing Info
         */
        String extractedLicenseContent = getTagContents(fileContents, EXTRACTED_LICENSE_TAG);
        List<String> extractedLicenses = List.of(extractedLicenseContent.split("\n\n"));

        for (String extractedLicenseBlock : extractedLicenses) {
            String licenseId = null;
            String licenseName = null;

            m = TAG_VALUE_PATTERN.matcher(extractedLicenseBlock);
            while(m.find()) {
                switch (m.group(1)) {
                    case EXTRACTED_LICENSE_ID -> licenseId = m.group(2);
                    case EXTRACTED_LICENSE_NAME -> licenseName = m.group(2);
                    default -> {} // TODO more fields?
                }
            }

            if (licenseId != null && licenseName != null) externalLicenses.put(licenseId, licenseName);
        }

        /*
            Files
         */

        String unpackagedFilesContents = getTagContents(fileContents, UNPACKAGED_TAG);
        List<String> files = List.of(unpackagedFilesContents.split("\n\n")); // Split over newline

        for(String fileBlock : files) {
            m = TAG_VALUE_PATTERN.matcher(fileBlock);
            HashMap<String, String> file_materials = new HashMap<>();
            while(m.find()) {
                file_materials.put(m.group(1), m.group(2));
            }

            // Create new component from materials
            Component unpackaged_component = new Component(
                    file_materials.get("FileName"),
                    "Unknown", // TODO
                    file_materials.get("PackageVersion"),
                    file_materials.get("SPDXID")
            );
            unpackaged_component.setUnpackaged(true);

            // Add unpackaged file to components
            this.components.put(unpackaged_component.getUniqueID(), unpackaged_component); // TODO is unique id correct?
        }

        /*
            Packages
         */
        String packageContents = getTagContents(fileContents, PACKAGE_TAG);
        List<String> packageList = List.of(packageContents.split("\n\n"));

        for (String pkg : packageList) {
            // If new package/component is found
            HashMap<String, String> component_materials = new HashMap<>();
            Set<String> cpes = new HashSet<>();
            Set<String> purls = new HashSet<>();
            Set<String> swids = new HashSet<>();

            m = TAG_VALUE_PATTERN.matcher(pkg);

            while (m.find()) {
                // Special case for external references
                if (m.group(1).equals(EXTERNAL_REFERENCE_TAG)) {
                    Matcher externalRef = EXTERNAL_REF_PATTERN.matcher(m.group(0));
                    if (!externalRef.find()) continue; // TODO invalid formatting?
                    if (externalRef.group(1).equalsIgnoreCase("security")) cpes.add(externalRef.group(3));
                    else if (externalRef.group(2).equalsIgnoreCase("purl")) purls.add(externalRef.group(3));
                    // TODO find examples of how SPDX represents SWID and implement that here

                    continue; // Now that the external ref line has been parsed, continue
                }

                component_materials.put(m.group(1), m.group(2));
            }

            // Cleanup package originator
            String supplier = null;
            if (component_materials.get("PackageSupplier") != null) {
                supplier = component_materials.get("PackageSupplier");
            } else if (component_materials.get("PackageOriginator") != null) {
                supplier = component_materials.get("PackageOriginator");
            }

            if (supplier != null) {
                supplier = supplier.contains("Person: ") && supplier.contains("<") ? supplier.substring(8) : supplier;
            }

            // Create new component from required information
            Component component = new Component(
                    component_materials.get("PackageName"),
                    supplier,
                    component_materials.get("PackageVersion"),
                    component_materials.get("SPDXID"));

            // Append CPEs and Purls
            component.setCpes(cpes);
            component.setPurls(purls);

            // License materials map
            HashSet<String> licenses = new HashSet<>();

            // Get licenses from component materials and split them by 'AND' tag, store them into HashSet and add them to component object
            if (component_materials.get("PackageLicenseConcluded") != null) {
                licenses.addAll(Arrays.asList(component_materials.get("PackageLicenseConcluded").split(" AND ")));
            }
            if (component_materials.get("PackageLicenseDeclared") != null) {
                licenses.addAll(Arrays.asList(component_materials.get("PackageLicenseDeclared").split(" AND ")));
            }

            // Remove any NONE/NOASSERTION licenses
            licenses.remove("NONE");
            licenses.remove("NOASSERTION");

            // Replace any extracted license information
            licenses = (HashSet<String>) licenses.stream().map(l -> {
                if (l.contains("LicenseRef") && externalLicenses.get(l) != null) return externalLicenses.get(l);
                return l;
            }).collect(Collectors.toSet());

            component.setLicenses(licenses);

            // Add packaged component to components list
            this.loadComponent(component);

            // Add packaged component to packages list as well
            packages.add(component.getUniqueID());
        }

        /*
            Relationships
         */
        // Regex to get every Relationship: tag in file
//                // Split and get and value of the line
//                String relationship = current_line.split(RELATIONSHIP_KEY, 2)[1];
//
//                // Split dependency relationship and store into relationships map depends on relationship type
//                if (current_line.contains("DEPENDS_ON")) {
//
//                    addDependency(
//                            relationship.split(" DEPENDS_ON ")[0],
//                            relationship.split(" DEPENDS_ON ")[1]
//                    );
//
//                } else if (current_line.contains("DEPENDENCY_OF")) {
//
//                    addDependency(
//                            relationship.split(" DEPENDENCY_OF ")[1],
//                            relationship.split(" DEPENDENCY_OF ")[0]
//                    );
//
//                }  else if (current_line.contains("DESCRIBES")) {
//
//                    product_id = relationship.split(" DESCRIBES ")[1];
//
//        if (product_id.contains(DOCUMENT_REFERENCE_TAG)) {
//            product_data.put("name", bom_data.get("DocumentName"));
//            product_data.put("publisher", bom_data.get("N/A"));
//            product_data.put("version", bom_data.get("N/A"));
//            product_data.put("id", bom_data.get("id"));
//            defaultTopComponent(product_data.get("id"), packages);
//        } else {
//            product = components.get(product_id);
//        }

        // Create the new SBOM Object with top level data
        this.createSBOM();

        // Create the top level component
        // Build the dependency tree using dependencyBuilder
        try {
            this.dependencyBuilder(components, this.product, null);
        } catch (Exception e) {
            Debug.log(Debug.LOG_TYPE.ERROR, "Error processing dependency tree.");
            Debug.log(Debug.LOG_TYPE.EXCEPTION, e.getMessage());
        }

        this.defaultDependencies(this.product);

        // Return the final SBOM object
        return sbom;
    }

    /**
     * Private helper method to get all tag-value pairs categorized under the specified tag (ex. ##### Unpackaged
     * Files). The tags will be located anywhere in the file; the order of tags does not impact translation of the SBOM.
     *
     * @param fileContents The file contents to get the tag from.
     * @param tag The tag to get all tag-value pairs of.
     * @return An "excerpt" from the {@code fileContents} string containing all tag-value pairs categorized under {@code
     * tag}.
     */
    private String getTagContents(String fileContents, String tag) {
        String tagContents = "";
        int firstIndex;
        int lastIndex;

        while (fileContents.contains(tag)) {
            // Get boundaries of this tag
            firstIndex = fileContents.indexOf(tag);
            lastIndex = fileContents.indexOf(TAG, firstIndex + 1);
            if (lastIndex == -1) break; // If another tag is not found, break the loop. TODO end of file?

            // Use this data to update tagContents with the found tag
            tagContents += fileContents.substring(firstIndex, lastIndex); // Remove newline
            fileContents = fileContents.substring(0, firstIndex) + fileContents.substring(lastIndex);
        }

        return tagContents;
    }
}
