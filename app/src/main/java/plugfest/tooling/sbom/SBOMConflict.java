package plugfest.tooling.sbom;

import java.util.HashSet;
import java.util.Set;

/**
 * Conflict between two SBOMs and their trivial information
 *
 * @author Matt London
 */
public class SBOMConflict {
    /** Snapshot of the two SBOMs that have a conflict */
    private SBOM aSBOM;
    private SBOM bSBOM;

    /** The types of conflicts found in these SBOMs */
    private Set<SBOMConflictType> conflictTypes;

    /**
     * Constructor to copy over trivial data
     *
     * @param aSBOM First SBOM to compare
     * @param bSBOM Second SBOM to compare
     */
    public SBOMConflict(SBOM aSBOM, SBOM bSBOM) {
        this.aSBOM = new SBOM(aSBOM);
        this.bSBOM = new SBOM(bSBOM);

        this.conflictTypes = new HashSet<>();

        assignConflictTypes();
    }

    /**
     * Detect conflicts within the two sboms and add them to the set
     */
    public void assignConflictTypes() {
        if (aSBOM.getOriginFormat() != null && !aSBOM.getOriginFormat().equals(bSBOM.getOriginFormat())) {
            conflictTypes.add(SBOMConflictType.ORIGIN_FORMAT_MISMATCH);
        }
        if (aSBOM.getSpecVersion() != null && !aSBOM.getSpecVersion().equals(bSBOM.getSpecVersion())) {
            conflictTypes.add(SBOMConflictType.SCHEMA_VERSION_MISMATCH);
        }
        if (aSBOM.getSerialNumber() != null && !aSBOM.getSerialNumber().equals(bSBOM.getSerialNumber())) {
            conflictTypes.add(SBOMConflictType.SERIAL_NUMBER_MISMATCH);
        }
        if (aSBOM.getTimestamp() != null && !aSBOM.getTimestamp().equals(bSBOM.getTimestamp())) {
            conflictTypes.add(SBOMConflictType.TIMESTAMP_MISMATCH);
        }
        // TODO Currently skip author because it is not stored in the SBOM class, fix this
//        if (aSBOM.getAuthor() != null && !aSBOM.getAuthor().equals(bSBOM.getAuthor())) {
//            conflictTypes.add(SBOMConflictType.AUTHOR_MISMATCH);
//        }
        if (aSBOM.getSbomVersion() != null && !aSBOM.getSbomVersion().equals(bSBOM.getSbomVersion())) {
            conflictTypes.add(SBOMConflictType.SBOM_VERSION_MISMATCH);
        }
    }

    public String getConflictString(SBOMConflictType conflictType) {
        String conflictString = "";
        switch (conflictType) {
            case SUPPLIER_MISMATCH:
                conflictString = "Supplier Mismatch\n";
                // TODO supplier currently not implemented
//                conflictString += "\n+ " + aSBOM.getSupplier();
                break;
            case AUTHOR_MISMATCH:
                // TODO author currently not implemented
                conflictString = "Author Mismatch\n";
                break;
            case TIMESTAMP_MISMATCH:
                conflictString = "Timestamp Mismatch:\n";
                conflictString += "+ " + aSBOM.getTimestamp() + "\n";
                conflictString += "- " + bSBOM.getTimestamp() + "\n";
                break;
            case ORIGIN_FORMAT_MISMATCH:
                conflictString = "Origin Format Mismatch:\n";
                conflictString += "+ " + aSBOM.getOriginFormat() + "\n";
                conflictString += "- " + bSBOM.getOriginFormat() + "\n";
                break;
            case SCHEMA_VERSION_MISMATCH:
                conflictString = "Schema Version Mismatch:\n";
                conflictString += "+ " + aSBOM.getSpecVersion() + "\n";
                conflictString += "- " + bSBOM.getSpecVersion() + "\n";
                break;
            case SBOM_VERSION_MISMATCH:
                conflictString = "SBOM Version Mismatch:\n";
                conflictString += "+ " + aSBOM.getSbomVersion() + "\n";
                conflictString += "- " + bSBOM.getSbomVersion() + "\n";
                break;
            case SERIAL_NUMBER_MISMATCH:
                conflictString = "Serial Number Mismatch:\n";
                conflictString += "+ " + aSBOM.getSerialNumber() + "\n";
                conflictString += "- " + bSBOM.getSerialNumber() + "\n";
                break;
        }

        return conflictString;
    }

    // Stringify this entire object
    @Override
    public String toString() {
        StringBuilder conflictString = new StringBuilder("SBOM Conflicts:\n");
        for (SBOMConflictType conflictType : conflictTypes) {
            conflictString.append(getConflictString(conflictType));
        }

        return conflictString.toString();
    }

}
