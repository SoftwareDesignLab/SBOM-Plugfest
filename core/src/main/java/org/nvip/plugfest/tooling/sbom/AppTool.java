package org.nvip.plugfest.tooling.sbom;

import java.util.Objects;

/**
 * File: SBOM.java
 * Represents an application tool for an SBOM
 *
 * @author Juan Francisco Patino
 */
public class AppTool {

    private String vendor;
    private String name;
    private String version;

    /*
    Constructors
     */
    public AppTool(){}

    public AppTool(String vendor, String name, String version){
        this.vendor = vendor;
        this.name = name;
        this.version = version;
    }

    /*
    Getters and setters
     */
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // overrides
    @Override
    public String toString() {
        return "Tool: "
                + (this.vendor != null ? this.vendor + " " : "")
                + this.name + "-" + this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppTool appTool)) return false;
        return Objects.equals(getVendor(), appTool.getVendor()) && Objects.equals(getName(), appTool.getName()) && Objects.equals(getVersion(), appTool.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVendor(), getName(), getVersion());
    }
}
