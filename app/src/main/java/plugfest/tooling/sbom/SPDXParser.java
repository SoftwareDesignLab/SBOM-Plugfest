package plugfest.tooling.sbom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * FileName: SPDXParser
 * Receives an SPDX SBOM and parses through the file while building
 * a SBOM Map Object at the same time.
 * Compatible with versions:
 *      - SPDX 2.2
 *      - SPDX 2.3
 *
 * @author Tyler Drake
 */
public class SPDXParser {
    private static final String TAG = "#####";

    private static final String UNPACKAGED_TAG = "##### Unpackaged files";

    private static final String PACKAGE_TAG = "##### Package";

    private static final String RELATIONSHIP_TAG = "##### Relationships";

    private static final String RELATIONSHIP_KEY = "Relationship: ";

    /**
     * Name: parse
     * Receive a file path to an SPDX SBOM and parse through it while
     * storing data into the SBOM Map Object.
     *
     * @param file_path
     * @return
     * @throws IOException
     */
    public static SBOM parse(String file_path) throws IOException {

//        SBOM sbom = new SBOM();

//        sbom.setName(Paths.get(file_path).getFileName().toString());
//
//        // Get spdx file
//        File file = new File(file_path);
//
//        // If file doesn't exist, return a null object
//        if(!file.exists()) { return null; }
//
//        // Initialize BufferedReader along with current line
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        String current_line = br.readLine();
//        sbom.addData(current_line);
//
//        // Process header information
//        while ( (current_line = readSBOMLine(sbom, br) ) != null
//                && !current_line.contains(UNPACKAGED_TAG)
//                && !current_line.contains(PACKAGE_TAG)
//                && !current_line.contains(RELATIONSHIP_TAG)
//                && !current_line.contains(RELATIONSHIP_KEY)
//        ) { sbom.addToHeader(current_line); }
//
//        // Process Unpackaged Components
//        while ( (current_line = readSBOMLine(sbom, br) ) != null
//                && !current_line.contains(PACKAGE_TAG)
//                && !current_line.contains(RELATIONSHIP_TAG)
//                && !current_line.contains(RELATIONSHIP_KEY)
//        ) {
//            if (current_line.contains(PACKAGE_TAG) || current_line.contains(RELATIONSHIP_TAG)) break;
//            if (current_line.isEmpty()) {
//                readSBOMLine(sbom, br);
//                Component component = new Component();
//                while ( (current_line = readSBOMLine(sbom, br) ) != null
//                        && !current_line.contains(TAG)
//                        && !current_line.isEmpty())
//                {
//
//                    if(current_line.contains("SPDXID:")) {
//                        component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
//                    }
//
//                    component.addInformation(current_line);
//
//                }
//
//                if( component.component_information.size() > 0 ) {
//                    sbom.addComponent(component.getIdentifier(), component);
//                }
//            }
//        }
//
//        // Parse through packaged components
//        while ( (current_line = readSBOMLine(sbom, br) ) != null
//                && !current_line.contains(RELATIONSHIP_TAG)
//                && !current_line.contains(RELATIONSHIP_KEY)
//        ) {
//            if (current_line.contains(RELATIONSHIP_TAG)) break;
//
//            // Temporary component collection of materials
//            Component component = new Component();
//
//            // If new package/component is found
//            if (current_line.contains(PACKAGE_TAG)) {
//                readSBOMLine(sbom, br);
//                // While in the same package/component
//                while (!(current_line = readSBOMLine(sbom, br)).contains(TAG)
//                        && !current_line.contains(RELATIONSHIP_TAG)
//                        && !current_line.contains(RELATIONSHIP_KEY)) {
//
//                    if (current_line.contains(": ")) {
//                        if(current_line.contains("SPDXID:")) {
//                            component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
//                        }
//                        component.addInformation(current_line);
//                    }
//
//                }
//
//                if ( component.component_information.size() > 0 ) {
//                    sbom.addComponent(component.getIdentifier(), component);
//                }
//
//            }
//
//        }
//
//        // Parse through relationships
//        while( (current_line = readSBOMLine(sbom, br) ) != null) {
//
//            if(current_line.contains("Relationship: ")) {
//
//                sbom.addRelationship(current_line.split("Relationship: ", 2)[1]);
//
//            }
//
//        }

        return null;

    }

    /**
     * Name: readSBOMLine
     * Does the same as BufferedReader's readLine(), except it adds
     * raw data from the new line to SBOM Map Object.
     *
     * @param sbom
     * @param br
     * @return
     * @throws IOException
     */
    public static String readSBOMLine(SBOM sbom, BufferedReader br) throws IOException {
        String next_line = br.readLine();
//        sbom.addData(next_line);
        return next_line;
    }

}
