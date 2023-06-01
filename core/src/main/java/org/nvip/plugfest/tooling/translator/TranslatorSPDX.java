package org.nvip.plugfest.tooling.translator;

import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //#region Constants

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
    private static final Pattern EXTERNAL_REF_PATTERN = Pattern.compile(EXTERNAL_REFERENCE_TAG + ": (\\S*) (\\S*) (\\S*)");
    private static final Pattern RELATIONSHIP_PATTERN = Pattern.compile(RELATIONSHIP_KEY + ": (\\S*) (\\S*) (\\S*)");

    //#endregion

    //#region Constructors

    public TranslatorSPDX() {
        super("spdx");
    }

    //#endregion

    //#region Abstract Method Overrides

    /**
     * Coverts SPDX SBOMs into internal SBOM objects by its contents
     *
     * @param fileContents Contents of the SBOM to translate
     * @param file_path Original path to SPDX SBOM
     * @return internal SBOM object from contents
     */
    @Override
    protected SBOM translateContents(String fileContents, String file_path) throws TranslatorException {
        // Top level component information
        String product_id = "";
        // Collection for components, packaged and unpackaged
        // Key = SPDXID , Value = Component
        HashMap<String, Component> components = new HashMap<>();
        // Collection of package IDs used for adding head components to top component if in the header SPDXRef-DOCUMENT
        ArrayList<String> packages = new ArrayList<>();
        Map<String, String> externalLicenses = new HashMap<>(); // Map of external licenses mapping ID to name

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

        this.parseHeader(header);
        bom_data.put("sbomVersion", "1");
        bom_data.put("format", "spdx");

        /*
            Relationships
         */
        List<String> lines = new ArrayList<>(List.of(fileContents.split("\n")));
        // Find all relationships in the file contents regardless of where they are
        Matcher relationship = RELATIONSHIP_PATTERN.matcher(fileContents);
        while(relationship.find()) {
            switch(relationship.group(2)) {
                case "DEPENDS_ON" -> addDependency( // TODO verify
                        relationship.group(1),
                        relationship.group(3)
                );
                case "DEPENDENCY_OF" -> addDependency( // TODO verify
                        relationship.group(3),
                        relationship.group(1)
                );
                case "DESCRIBES" -> product_id = relationship.group(3);
            }
            lines.remove(relationship.group()); // Remove parsed relationship from contents
        }
        fileContents = String.join("\n", lines); // Remove all relationships from fileContents

        /*
            Extracted Licensing Info
         */

        String extractedLicenseContent = getTagContents(fileContents, EXTRACTED_LICENSE_TAG);
        List<String> extractedLicenses = List.of(extractedLicenseContent.split("\n\n"));

        for (String extractedLicenseBlock : extractedLicenses) {
            if (extractedLicenseBlock.equals("")) continue;
            this.parseExternalLicense(extractedLicenseBlock, externalLicenses);
        }

        /*
            Files
         */

        String unpackagedFilesContents = getTagContents(fileContents, UNPACKAGED_TAG);
        List<String> files = List.of(unpackagedFilesContents.split("\n\n")); // Split over newline

        for(String fileBlock : files) {
            if (fileBlock.equals("")) continue;
            Component file = this.buildFile(fileBlock);

            // Add unpackaged file to components
            this.components.put(file.getUniqueID(), file);
        }

        /*
            Packages
         */

        String packageContents = getTagContents(fileContents, PACKAGE_TAG);
        List<String> packageList = Stream.of(packageContents.split("\n\n")).filter(pkg -> !pkg.contains(TAG)).toList();

        for (String pkg : packageList) {
            if (pkg.equals("")) continue;
            Component component = buildComponent(pkg, externalLicenses);

            // Add packaged component to components list
            this.loadComponent(component);

            // Add packaged component to packages list as well
            packages.add(component.getUniqueID());
        }

        // Build the top component of the SBOM (containing metadata)
        if (product_id.contains(DOCUMENT_REFERENCE_TAG)) {
            product_data.put("name", bom_data.get("DocumentName"));
            product_data.put("publisher", bom_data.get("Unknown"));
            product_data.put("version", bom_data.get("N/A"));
            product_data.put("id", bom_data.get("id"));
            defaultTopComponent(product_data.get("id"), packages);
        } else {
            topComponent = components.get(product_id);
        }

        // Create the new SBOM Object with top level data
        this.createSBOM();

        // Create the top level component
        // Build the dependency tree using dependencyBuilder
        try {
            this.dependencyBuilder(components, this.topComponent, null);
        } catch (Exception e) {
            Debug.log(Debug.LOG_TYPE.ERROR, "Error processing dependency tree.");
            Debug.log(Debug.LOG_TYPE.EXCEPTION, e.getMessage());
        }

        this.defaultDependencies(this.topComponent);

        // Return the final SBOM object
        return sbom;
    }

    //#endregion

    //#region Helper Methods

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

            // If another tag is not found, last index goes to end of file
            if (lastIndex == -1) lastIndex = fileContents.length();

            // Use this data to update tagContents with the found tag
            tagContents += fileContents.substring(firstIndex, lastIndex); // Remove newline
            fileContents = fileContents.substring(0, firstIndex) + fileContents.substring(lastIndex);
        }

        return tagContents;
    }

    /**
     * Private helper method to process the header/metadata of an SPDX document and put all relevant data into the
     * {@code bom_data} map in {@code TranslatorCore}.
     *
     * @param header The header data of the SPDX document.
     */
    private void parseHeader(String header) {
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
    }

    /**
     * Private helper method to process an external license in an SPDX document and append all relevant data into
     * the {@code externalLicenses} map with each entry having an ID (key) and name (value).
     *
     * @param extractedLicenseBlock An extracted licensing information "block" in the document.
     * @param externalLicenses The map of external licenses to append to.
     */
    private void parseExternalLicense(String extractedLicenseBlock, Map<String, String> externalLicenses) {
        String licenseId = null;
        String licenseName = null;

        Matcher m = TAG_VALUE_PATTERN.matcher(extractedLicenseBlock);
        while(m.find()) {
            switch (m.group(1)) {
                case EXTRACTED_LICENSE_ID -> licenseId = m.group(2);
                case EXTRACTED_LICENSE_NAME -> licenseName = m.group(2);
                default -> {} // TODO more fields?
            }
        }

        if (licenseId != null && licenseName != null) externalLicenses.put(licenseId, licenseName);
    }

    /**
     * Private helper method to process an unpackaged file in an SPDX document and use its data to build a
     * {@code Component} representation.
     *
     * @param fileBlock An unpackaged file "block" in the document.
     * @return A {@code Component} with the data of the file "block".
     */
    private Component buildFile(String fileBlock) {
        Matcher m = TAG_VALUE_PATTERN.matcher(fileBlock);
        HashMap<String, String> file_materials = new HashMap<>();
        while(m.find()) file_materials.put(m.group(1), m.group(2));

        // Create new component from materials
        Component unpackaged_component = new Component(
                file_materials.get("FileName"),
                "Unknown",
                file_materials.get("PackageVersion"),
                file_materials.get("SPDXID")
        );
        unpackaged_component.setUnpackaged(true);

        return unpackaged_component;
    }

    /**
     * Private helper method to process a package in an SPDX document and use its data to build a {@code Component}
     * representation.
     *
     * @param packageBlock A package "block" in the document.
     * @return A {@code Component} with the data of the package "block".
     */
    private Component buildComponent(String packageBlock, Map<String, String> externalLicenses) {
        Map<String, String> componentMaterials = new HashMap<>();
        Set<String> cpes = new HashSet<>();
        Set<String> purls = new HashSet<>();
        Set<String> swids = new HashSet<>();

        Matcher m = TAG_VALUE_PATTERN.matcher(packageBlock);

        while (m.find()) {
            if (!m.group(1).equals(EXTERNAL_REFERENCE_TAG)) {
                componentMaterials.put(m.group(1), m.group(2));
                continue;
            }

            // Special case for external references
            Matcher externalRef = EXTERNAL_REF_PATTERN.matcher(m.group());
            if (!externalRef.find()) continue;

            switch(externalRef.group(2).toLowerCase()) {
                case "cpe23type" -> cpes.add(externalRef.group(3));
                case "purl" -> purls.add(externalRef.group(3));
                case "swid" -> swids.add(externalRef.group(3));
            }
        }

        // Cleanup package originator
        String supplier = "Unknown"; // Default value of unknown
        if (componentMaterials.get("PackageSupplier") != null) {
            supplier = componentMaterials.get("PackageSupplier");
        } else if (componentMaterials.get("PackageOriginator") != null) {
            supplier = componentMaterials.get("PackageOriginator");
        }

        // Create new component from required information
        Component component = new Component(
                componentMaterials.get("PackageName"),
                supplier,
                componentMaterials.get("PackageVersion"),
                componentMaterials.get("SPDXID"));

        // Append CPEs and Purls
        component.setCpes(cpes);
        component.setPurls(purls);
        component.setSwids(swids);

        // License materials map
        HashSet<String> licenses = new HashSet<>();

        // Get licenses from component materials and split them by 'AND' tag, store them into HashSet and add them to component object
        if (componentMaterials.get("PackageLicenseConcluded") != null)
            licenses.addAll(Arrays.asList(componentMaterials.get("PackageLicenseConcluded").split(" AND ")));
        if (componentMaterials.get("PackageLicenseDeclared") != null)
            licenses.addAll(Arrays.asList(componentMaterials.get("PackageLicenseDeclared").split(" AND ")));

        // Clean up licenses
        licenses = // Remove NONE/NOASSERTION with .filter()
                (HashSet<String>) licenses.stream().filter(l -> !l.equals("NONE") && !l.equals("NOASSERTION"))
                        .map(l -> { // Replace external licenses with .map()
                            if (l.contains("LicenseRef") && externalLicenses.get(l) != null)
                                return externalLicenses.get(l);
                            return l;
                        }).collect(Collectors.toSet());

        component.setLicenses(licenses);

        return component;
    }
}
