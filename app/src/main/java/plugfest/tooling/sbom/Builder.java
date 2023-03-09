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
    public SBOM builder(String file_path) throws IOException {

        SBOM sbom = new SBOM();

        // Get spdx file
        File file = new File(file_path);

        // Initialize BufferedReader along with current line
        BufferedReader br = new BufferedReader(new FileReader(file));
        String current_line;

        // Process header information
        while ((current_line = br.readLine()) != null && !current_line.contains(UNPACKAGED_TAG) && !current_line.contains(PACKAGE_TAG) && !current_line.contains(RELATIONSHIP_TAG)) {

            sbom.addToHeader(current_line);

        }

        // Process Unpackaged Components
        while (current_line != null && !current_line.contains(PACKAGE_TAG) && !current_line.contains(RELATIONSHIP_TAG)) {
            if (current_line.contains(PACKAGE_TAG) || current_line.contains(RELATIONSHIP_TAG)) break;

            if (current_line.contains(UNPACKAGED_TAG)) {

                Component component = new Component();


                while (!(current_line = br.readLine()).contains(TAG) && !current_line.isEmpty()) {

                    if(current_line.contains("SPDXID:")) {
                        component.setIdentifier(current_line.split("SPDXID: ", 2)[1]);
                    }

                    component.addInformation(current_line);
                }

            } else {
                current_line = br.readLine();
            }
        }

        return sbom;
    }

}
