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

        return sbom;
    }

}
