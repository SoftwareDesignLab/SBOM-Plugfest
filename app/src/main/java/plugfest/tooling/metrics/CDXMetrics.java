package plugfest.tooling.metrics;

/**
 * Imports Java Native Libraries
 */
import java.util.List;
import java.util.HashMap;
import java.io.File;

/**
 * Imports from CycloneDX Java Core Libraries
 */
import org.cyclonedx.CycloneDxSchema;

/**
 * Import from CycloneDX Java Model Libraries
 */
import org.cyclonedx.model.Hash;


/**
 * Import from CycloneDX Java Utility Libraries
 */
import org.cyclonedx.util.BomUtils;


public class CDXMetrics {
    
    private String filepath;

    public CDXMetrics(String _filepath) {
        this.filepath = _filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    public HashMap<CycloneDxSchema.Version, Boolean> verify(String sbom) {
        System.out.println("Running Verification on CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, Boolean> verificationMap = new HashMap<CycloneDxSchema.Version, Boolean>();
        if(sbom_file.substring(sbom_file.length()-5).equals(".json") == true) {
            System.out.println("Valid File: "+sbom_file);
            verificationMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.validateUriString(sbom_file));
        }
        else if(sbom_file.substring(sbom_file.length()-4).equals(".xml") == true) {
            System.out.println("Valid File: "+sbom_file);
            verificationMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.validateUriString(sbom_file));
            verificationMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.validateUriString(sbom_file));
        }
        else {
            System.out.println("Invalid File Type");
            return null;
        }
        return verificationMap;
    }

    public HashMap<CycloneDxSchema.Version, List<Hash>> calculateHashes(String sbom) {
        System.out.println("Calculating Hashes of CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, List<Hash>> hashesMap = new HashMap<CycloneDxSchema.Version, List<Hash>>();
        try {
            if(sbom_file.substring(sbom_file.length()-5).equals(".json") == true) {
                System.out.println("Valid File: "+sbom_file);
                hashesMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_10));
                hashesMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_11));
                hashesMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_12));
                hashesMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_13));
                hashesMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_14));
            }
            else if(sbom_file.substring(sbom_file.length()-4).equals(".xml") == true) {
                System.out.println("Valid File: "+sbom_file);
                hashesMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_10));
                hashesMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_11));
                hashesMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_12));
                hashesMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_13));
                hashesMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_14));
            }
            else {
                System.out.println("Invalid File Type");
                return null;
            }
        }
        catch(Exception ex) {
            System.out.println("EXCEPTION: "+ex);
            return null;
        }
        return hashesMap;
    }

}
