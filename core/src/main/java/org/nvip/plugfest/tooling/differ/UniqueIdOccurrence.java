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

package org.nvip.plugfest.tooling.differ;

import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.cyclonedx.model.Hash;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * File: UniqueIdOccurrence.java
 * holds a CPE/PURL/SWID, and the IDs of every SBOM that identifier appears in.
 *
 * @author Juan Francisco Patino, Tyler Drake, Henry Orsagh
 */
public class UniqueIdOccurrence {

    /**
     * The PURL/CPE/SWID
     */
    private String uniqueIdentifier;

    /**
     * a set of SBOM IDs that this unique ID appears in
     */
    private Set<Integer> appearances;

    /**
     * the type of Unique ID
     */
    private UniqueIdentifierType uniqueIdType;

    /**
     * initializes a UniqueIdOccurrence with an empty appearance Set.
     * @param uID The unique ID
     * @param type The type of unique ID (CPE/PURL/SWID/etc)
     */
    public UniqueIdOccurrence (String uID, UniqueIdentifierType type) {
        this.uniqueIdentifier = uID;
        this.uniqueIdType = type;
        this.appearances = new HashSet<>();
    }

    ///
    /// getters and setters
    ///

    public String getUniqueId() {
        return this.uniqueIdentifier;
    }

    public UniqueIdentifierType getUniqueIdType() {
        return uniqueIdType;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }

    /**
     * adds an SBOM ID to the appearance Set.
     * @param a ID of the SBOM this unique ID appears in.
     */
    public void addAppearance(int a){
        appearances.add(a);
    }

    ///
    /// Overrides
    ///

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniqueIdOccurrence that)) return false;
        return Objects.equals(uniqueIdentifier, that.uniqueIdentifier) && Objects.equals(getAppearances(), that.getAppearances()) && getUniqueIdType() == that.getUniqueIdType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier, getAppearances(), getUniqueIdType());
    }
}
