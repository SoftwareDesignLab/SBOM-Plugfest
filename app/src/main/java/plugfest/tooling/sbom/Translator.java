package plugfest.tooling.sbom;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;


/**
 * file: Translator.java
 *
 * Driver class for SPDX and CDX Translators
 * @author Tyler Drake
 */
public class Translator {

    // home of SBOM files generated by OSI
    private static final String SBOM_PATH = "src/main/java/com/svip/osi/core/bound_dir/sboms";

    /**
     * Convert CycloneDX and SPDX SBOMs stored in the SBOMs directory
     *
     * @return List of SBOM Objects
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList<SBOM> toReport() throws ParserConfigurationException, IOException {

        // Collection for potential SBOM files
        ArrayList<Path> sbom_files = new ArrayList<>();

        // Collection for built SBOM Objects
        ArrayList<SBOM> sbom_objects = new ArrayList<>();

        // Go through target folder and add files to sbom_file ArrayList
        try (Stream<Path> paths = Files.walk(Paths.get(SBOM_PATH))) {
            paths.filter(Files::isRegularFile).forEach(sbom_files::add);
        }

        /*
         * Iterate through every file found in SBOM folder. If a supported file is found, throw it into a translator.
         * Supported formats:
         *  - CYCLONE-DX XML
         *  - SPDX TAG-VALUE
         */
        for (Path sbom_item : sbom_files) {
            try {
                if (sbom_item.toString().toLowerCase().endsWith(".xml")) {
                    sbom_objects.add(TranslatorCDX.translatorCDX(sbom_item.toString()));
                } else if (sbom_item.toString().toLowerCase().endsWith(".spdx")) {
                    sbom_objects.add(TranslatorSPDX.translatorSPDX(sbom_item.toString()));
                } else {
                    System.err.println("\nInvalid SBOM format found in: " + sbom_item);
                }
                // todo deleting gitignore
                try {
                    Files.delete(sbom_item);
                } catch (IOException e) {
                    // This means it couldn't delete the file, which is fine
                }
            }
            catch (Exception e){
                System.err.println("Error translating SBOM: " + sbom_item);
            }
        }

        // Remove all null sboms in sbom collection
        sbom_objects.removeAll(Collections.singleton(null));

        // Return ArrayList of Java SBOM Objects
        return sbom_objects;
    }
}