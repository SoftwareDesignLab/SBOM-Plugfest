package org.nvip.plugfest.tooling.differ;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ComponentVersion {


    // Name of the component
    private String componentName;

    // Version of the component
    private String version;

    // component CPE IDs
    private Set<UniqueIdOccurrence> cpes;

    // component PURL IDs
    private Set<UniqueIdOccurrence> purls;

    // component SWIDs
    private Set<UniqueIdOccurrence> swids;

    // SBOMs the component appears in
    private Set<Integer> appearances;

    public ComponentVersion(String componentName, String version) {
        this.componentName = componentName;
        this.version = version;
        this.cpes = new HashSet<>();
        this.purls = new HashSet<>();
        this.swids = new HashSet<>();
    }

    // getters and setters

    public String getComponentName() {
        return this.componentName;
    }

    public String getComponentVersion() {
        return this.version;
    }

    public Set<UniqueIdOccurrence> getCPES() {
        return this.cpes;
    }

    public Set<UniqueIdOccurrence> getPURLS() {
        return this.purls;
    }

    public Set<UniqueIdOccurrence> getSWIDS() {
        return this.swids;
    }

    public Set<Integer> getAppearances() {
        return this.appearances;
    }


    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setComponentVersion(String version) {
        this.version = version;
    }

    public void setCPES(Set<UniqueIdOccurrence> cpes) {
        this.cpes = cpes;
    }

    public void setPURLS(Set<UniqueIdOccurrence> purls) {
        this.purls = purls;
    }

    public void setSWIDS(Set<UniqueIdOccurrence> swids) {
        this.swids = swids;
    }

    public void setAppearances(Set<Integer> appearances) {
        this.appearances = appearances;
    }

    // add individual objects to respective sets

    public void addCPE(UniqueIdOccurrence cpe){
        cpes.add(cpe);
    }

    public void addPURL(UniqueIdOccurrence purl){
        cpes.add(purl);
    }

    public void addSWID(UniqueIdOccurrence swid){
        cpes.add(swid);
    }

    public void addAppearance(int a){
        this.appearances.add(a);
    }

    // overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentVersion that)) return false;
        return Objects.equals(getComponentName(), that.getComponentName()) && Objects.equals(version, that.version);
    }

    @Override
    public String toString() {
        return "ComponentVersion{" +
                "componentName='" + componentName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getComponentName(), version);
    }
}
