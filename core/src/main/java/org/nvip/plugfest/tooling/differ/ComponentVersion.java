package org.nvip.plugfest.tooling.differ;

import java.util.Set;

public class ComponentVersion {

    // Name of the component
    private String componentName;

    // Version of the component
    private String version;

    // component CPE IDs
    private Set<UniqueIdOcurrance> cpe;

    // component PURL IDs
    private Set<UniqueIdOcurrance> purl;

    // component SWIDs
    private Set<UniqueIdOcurrance> swid;

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

    public Set<UniqueIdOcurrance> getCpe() {
        return this.cpe;
    }

    public Set<UniqueIdOcurrance> getPurl() {
        return this.purl;
    }

    public Set<UniqueIdOcurrance> getSwid() {
        return this.swid;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }

}
