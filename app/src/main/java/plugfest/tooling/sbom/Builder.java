package plugfest.tooling.sbom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


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
        String current_line = br.readLine();
        sbom.addData(current_line);

        // Process header information
        while ((current_line) != null
                && !current_line.contains(UNPACKAGED_TAG)
                && !current_line.contains(PACKAGE_TAG)
                && !current_line.contains(RELATIONSHIP_TAG)
                && !current_line.contains(RELATIONSHIP_KEY)
        ) {
            current_line = br.readLine();
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
                current_line = br.readLine();
                sbom.addData(current_line);
                Component component = new Component();
                while (!(current_line).contains(TAG) && !current_line.isEmpty()) {

                    if(current_line.contains("SPDXID:")) {
                        component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
                    }

                    component.addInformation(current_line);
                    current_line = br.readLine();
                    sbom.addData(current_line);
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
            if (current_line.contains(RELATIONSHIP_TAG)) break;

            // Temporary component collection of materials
            Component component = new Component();

            // If new package/component is found
            if (current_line.contains(PACKAGE_TAG)) {
                current_line = br.readLine();
                sbom.addData(current_line);
                // While in the same package/component
                while (!(current_line).contains(TAG) && !current_line.contains(RELATIONSHIP_TAG)) {
                    if (current_line.contains(": ")) {
                        if(current_line.contains("SPDXID:")) {
                            component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
                        }
                        component.addInformation(current_line);
                    }
                    current_line = br.readLine();
                    sbom.addData(current_line);
                }

                sbom.addComponent(component.getIdentifier(), component);


            } else {
                current_line = br.readLine();
                sbom.addData(current_line);
            }
        }

        // Parse through relationships
        while(current_line != null) {

            if(current_line.contains("Relationship: ")) {

                sbom.addRelationship(current_line.split("Relationship: ", 2)[1]);

            }

            current_line = br.readLine();
            sbom.addData(current_line);

        }

        return sbom;

    }

}
