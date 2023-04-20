package org.nvip.plugfest.tooling.metrics;

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


/**
 * Class for CDX SBOM Metrics
 *
 * @author Henry Keena
 */
public class CDXMetrics extends Metric{

    /**
     * The filepath of the SBOM file.
     */
    private String filepath;

    /**
     * The text of the SBOM file.
     */
    private final String sbom;

    /**
     * Constructor for CDXMetrics
     *
     * @param filepath The filepath of the SBOM file
     * @param sbom The text of the SBOM file
     */
    public CDXMetrics(String filepath, String sbom) {
        this.filepath = filepath;
        this.sbom = sbom;
    }

    /**
     * Method that verifies whether a provided CDX SBOM file
     * is valid according to the CycloneDX SBOM file format schema.
     * 
     * <p>Returns a HashMap with verification results depending on schema version.
     * 
     * @param sbom the SBOM file to be verified.
     * @return a HashMap using the verification schema version as the key, and the verification results as a boolean as the value.
     */
    public HashMap<CycloneDxSchema.Version, Boolean> verifyCDX(String sbom) {
        System.out.println("Running Verification on CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, Boolean> verificationMap = new HashMap<CycloneDxSchema.Version, Boolean>();

        System.out.println("Valid File: "+sbom_file);
        verificationMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.validateUriString(sbom_file));

        return verificationMap;
    }

    /**
     * Method for calculating hash values for CDX SBOM Files.
     * 
     * <p>Returns a HashMap with calculated hash values depending on schema version.
     * 
     * @param sbom the SBOM file to get hash value.
     * @return a HashMap using the CDX SBOM file schema version as the key, and the calculated hashes as the value.
     */
    public HashMap<CycloneDxSchema.Version, List<Hash>> calculateHashes(String sbom) {
        System.out.println("Calculating Hashes of CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, List<Hash>> hashesMap = new HashMap<CycloneDxSchema.Version, List<Hash>>();
        try {
            System.out.println("Valid File: "+sbom_file);
            hashesMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_10));
            hashesMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_11));
            hashesMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_12));
            hashesMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_13));
            hashesMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.calculateHashes(new File(sbom_file), CycloneDxSchema.Version.VERSION_14));

        }
        catch(Exception ex) {
            System.out.println("EXCEPTION: "+ex);
            return null;
        }
        return hashesMap;
    }

    ///
    /// getter and setter methods
    ///

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    ///
    /// Overrides
    ///

    @Override
    protected int testMetric() {
        int result = 0;

        //Runs verify and calculateHashes and increments score if they are valid
        if (verifyCDX(this.sbom) != null) result++;
        if (calculateHashes(this.sbom) != null) result++;

        return result;
    }
}
