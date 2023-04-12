package org.nvip.plugfest.tooling.differ;

import java.util.Set;

public class ComponentVersion {

    // Name of the component
    private String componentName;

    // Version of the component
    private String version;

    // component CPE IDs
    private Set<UniqueIdOccurrence> cpe;

    // component PURL IDs
    private Set<UniqueIdOccurrence> purl;

    // component SWIDs
    private Set<UniqueIdOccurrence> swid;

    // SBOMs the component appears in
    private Set<Integer> appearances;

    public ComponentVersion() {

    }

    public String getComponentName() {
        return this.componentName;
    }

    public String getComponentVersion() {
        return this.version;
    }

    public Set<UniqueIdOccurrence> getCpe() {
        return this.cpe;
    }

    public Set<UniqueIdOccurrence> getPurl() {
        return this.purl;
    }

    public Set<UniqueIdOccurrence> getSwid() {
        return this.swid;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }

}
