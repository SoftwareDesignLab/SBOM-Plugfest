package org.nvip.plugfest.tooling.qa.processors;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Hash;
import org.cyclonedx.util.BomUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CDXProcessor extends SchemaProcessor{
    private String filepath;

    public CDXProcessor(String filepath){
        this.filepath = filepath;
    }
    public HashMap<CycloneDxSchema.Version, Boolean> testVerifyCDX(String sbom) {
        System.out.println("Running Verification on CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, Boolean> verificationMap = new HashMap<>();

        System.out.println("Valid File: "+sbom_file);
        verificationMap.put(CycloneDxSchema.Version.VERSION_10, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_11, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_12, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_13, BomUtils.validateUriString(sbom_file));
        verificationMap.put(CycloneDxSchema.Version.VERSION_14, BomUtils.validateUriString(sbom_file));

        return verificationMap;
    }

    public HashMap<CycloneDxSchema.Version, List<Hash>> testCalculateHashes(String sbom) {
        System.out.println("Calculating Hashes of CDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        HashMap<CycloneDxSchema.Version, List<Hash>> hashesMap = new HashMap<>();
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
}

