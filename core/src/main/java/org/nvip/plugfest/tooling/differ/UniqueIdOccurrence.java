package org.nvip.plugfest.tooling.differ;

import org.apache.commons.compress.harmony.pack200.NewAttributeBands;

import java.util.Set;

public class UniqueIdOccurrence {

    // Unique ID
    private String uniqueIdentifier;

    // SBOMs found in
    private Set<Integer> appearances;

    // Type of Unique ID
    private UniqueIdentifierType uniqueIdType;

    public UniqueIdOccurrence () {

    }

    public String getUniqueId() {
        return this.uniqueIdentifier;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }

}
