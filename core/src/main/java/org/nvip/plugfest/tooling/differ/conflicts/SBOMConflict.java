/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.differ.conflicts;

import org.nvip.plugfest.tooling.sbom.SBOM;

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
        if (aSBOM.getSupplier() != null && !aSBOM.getSupplier().equals(bSBOM.getSupplier())) {
            conflictTypes.add(SBOMConflictType.SUPPLIER_MISMATCH);
        }
        if (aSBOM.getSbomVersion() != null && !aSBOM.getSbomVersion().equals(bSBOM.getSbomVersion())) {
            conflictTypes.add(SBOMConflictType.SBOM_VERSION_MISMATCH);
        }
    }

    /**
     * Generate a string summary of the conflict
     *
     * @return String representation of a conflict
     */
    public String getConflictString(SBOMConflictType conflictType) {
        StringBuilder conflictString = new StringBuilder();
        switch (conflictType) {
            case SUPPLIER_MISMATCH:
                conflictString.append("Supplier Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getSupplier()).append("\n");
                conflictString.append("- ").append(bSBOM.getSupplier()).append("\n");
                break;
            case AUTHOR_MISMATCH:
                // TODO author currently not implemented
                conflictString.append("Author Mismatch:\n");

                break;
            case TIMESTAMP_MISMATCH:
                conflictString.append("Timestamp Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getTimestamp()).append("\n");
                conflictString.append("- ").append(bSBOM.getTimestamp()).append("\n");
                break;
            case ORIGIN_FORMAT_MISMATCH:
                conflictString.append("Origin Format Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getOriginFormat()).append("\n");
                conflictString.append("- ").append(bSBOM.getOriginFormat()).append("\n");
                break;
            case SCHEMA_VERSION_MISMATCH:
                conflictString.append("Schema Version Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getSpecVersion()).append("\n");
                conflictString.append("- ").append(bSBOM.getSpecVersion()).append("\n");
                break;
            case SBOM_VERSION_MISMATCH:
                conflictString.append("SBOM Version Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getSbomVersion()).append("\n");
                conflictString.append("- ").append(bSBOM.getSbomVersion()).append("\n");
                break;
            case SERIAL_NUMBER_MISMATCH:
                conflictString.append("Serial Number Mismatch:\n");
                conflictString.append("+ ").append(aSBOM.getSerialNumber()).append("\n");
                conflictString.append("- ").append(bSBOM.getSerialNumber()).append("\n");
                break;
        }

        return conflictString.toString();
    }

    ///
    /// Getters and Setters
    ///

    public Set<SBOMConflictType> getConflicts() {
        return this.conflictTypes;
    }

    ///
    /// Overrides
    ///

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
