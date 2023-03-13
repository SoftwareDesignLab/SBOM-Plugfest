package plugfest.tooling.sbom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class Builder {
    private static final String TAG = "#####";

    private static final String UNPACKAGED_TAG = "##### Unpackaged files";

    private static final String PACKAGE_TAG = "##### Package";

    private static final String RELATIONSHIP_TAG = "##### Relationships";

    private static final String RELATIONSHIP_KEY = "Relationship: ";
    public static SBOM builder(String file_path) throws IOException {

        SBOM sbom = new SBOM();

        // Get spdx file
        File file = new File(file_path);

        // Initialize BufferedReader along with current line
        BufferedReader br = new BufferedReader(new FileReader(file));
        String current_line;

        // Process header information
        while ((current_line = br.readLine()) != null
                && !current_line.contains(UNPACKAGED_TAG)
                && !current_line.contains(PACKAGE_TAG)
                && !current_line.contains(RELATIONSHIP_TAG)
                && !current_line.contains(RELATIONSHIP_KEY)
        ) {
            sbom.addData(current_line);
            sbom.addToHeader(current_line);
        }

        // Process Unpackaged Components
        while (current_line != null
                && !current_line.contains(PACKAGE_TAG)
                && !current_line.contains(RELATIONSHIP_TAG)
                && !current_line.contains(RELATIONSHIP_KEY)
        ) {
            if (current_line.contains(PACKAGE_TAG) || current_line.contains(RELATIONSHIP_TAG)) break;
            if (current_line.contains(UNPACKAGED_TAG)) {

                Component component = new Component();
                sbom.addData(current_line);

                while (!(current_line = br.readLine()).contains(TAG) && !current_line.isEmpty()) {
                    sbom.addData(current_line);

                    if(current_line.contains("SPDXID:")) {
                        component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
                    }

                    component.addInformation(current_line);
                }

                sbom.addComponent(component.getIdentifier(), component);


            } else {
                current_line = br.readLine();
                sbom.addData(current_line);
            }
        }

        // Parse through packaged components
        while (current_line != null
                && !current_line.contains(RELATIONSHIP_TAG)
                && !current_line.contains(RELATIONSHIP_KEY)
        ) {
            sbom.addData(current_line);
            if (current_line.contains(RELATIONSHIP_TAG)) break;

            // Temporary component collection of materials
            Component component = new Component();

            // If new package/component is found
            if (current_line.contains(PACKAGE_TAG)) {

                // While in the same package/component
                while (!(current_line = br.readLine()).contains(TAG) && !current_line.contains(RELATIONSHIP_TAG)) {
                    sbom.addData(current_line);
                    if (current_line.contains(": ")) {
                        if(current_line.contains("SPDXID:")) {
                            component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
                        }
                        component.addInformation(current_line);
                    }
                }

                sbom.addComponent(component.getIdentifier(), component);


            } else {
                current_line = br.readLine();
                sbom.addData(current_line);
            }
        }

        // Parse through relationships
        while(current_line != null) {
            sbom.addData(current_line);

            if(current_line.contains("Relationship: ")) {

                String relationship = current_line.split("Relationship: ", 2)[1];

                if(current_line.contains("CONTAINS")) {

                    sbom.addRelationship(
                            relationship.split(" CONTAINS ")[0],
                            relationship.split(" CONTAINS ")[1]
                    );

                } else if (current_line.contains("DEPENDENCY_OF")) {

                    sbom.addRelationship(
                            relationship.split(" DEPENDENCY_OF ")[1],
                            relationship.split(" DEPENDENCY_OF ")[0]
                    );

                } else if (current_line.contains("OTHER")) {

                    sbom.addRelationship(
                            relationship.split(" OTHER ")[1],
                            relationship.split(" OTHER ")[0]
                    );
                }
            }

            current_line = br.readLine();

        }

        return sbom;
    }

}
