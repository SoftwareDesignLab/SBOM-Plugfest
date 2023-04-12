package org.nvip.plugfest.tooling.differ;

import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.Objects;
import java.util.Set;

public class UniqueIdOccurrence {

    // Unique ID
    private String uniqueIdentifier;

    // SBOMs found in
    private Set<Integer> appearances;

    // Type of Unique ID
    private UniqueIdentifierType uniqueIdType;

    public UniqueIdOccurrence (String uID, UniqueIdentifierType type) {
        this.uniqueIdentifier = uID;
        this.uniqueIdType = type;
    }

    // getters

    public String getUniqueId() {
        return this.uniqueIdentifier;
    }

    public UniqueIdentifierType getUniqueIdType() {
        return uniqueIdType;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }

    // add individual SBOMs to appearances
    public void addAppearance(int a){
        appearances.add(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniqueIdOccurrence that)) return false;
        return Objects.equals(uniqueIdentifier, that.uniqueIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier);
    }
}
