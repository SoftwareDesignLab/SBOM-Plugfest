package plugfest.tooling.sbom;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * File: Component.java
 * Represents a single component inside an SBOM
 *
 * @author Matt London
 * @author Kevin Laporte
 */
public class Component {

    /**
     * UUID assigned to Component upon being added to the dependency tree
     *
     * Note: only used for assembling Dependency Tree
     */
    private UUID uuid = null;

    /**
     * Name of the component
     */
    private String name;

    /**
     * Publisher of this component
     */
    private String publisher;

    /**
     * If the component is unpackaged (not included in SPDX notation)
     */
    private boolean unpackaged;

    /**
     * Unique identifiers of the component (ex: CDX uses purl and/or cpe)
     */
    private Set<String> CPE;
    private Set<String> PURL;
    private Set<String> SWID;

    /**
     * Unique identifier for SPDX component
     */
    private String SPDXid;

    /**
     * UUIDs for the children of the given component
     */
    private final Set<UUID> children;

    /**
     * Version of the component (version assigned by publisher)
     */
    private String version;

    /**
     * List of vulnerabilities found (created by NVIP)
     */
    private final Set<Vulnerability> vulnerabilities;

    /**
     * Represent the license of the component
     */
    private Set<String> licenses;

    /**
     * Represent the conflicts of the component with other components
     * Note: This should ONLY be used in the master SBOM and never in individual sboms
     */
    private final Set<ComponentConflict> componentConflicts;

    /**
     * Constructs a component with no attributes
     */
    public Component() {
        // This should not be a parameter passed as children will not be instantiated first
        this.children = new HashSet<>();
        this.vulnerabilities = new HashSet<>();
        this.CPE = new HashSet<>();
        this.PURL = new HashSet<>();
        this.SWID = new HashSet<>();
        this.componentConflicts = new HashSet<>();
        this.unpackaged = false;
    }

    /**
     * Constructs a component with the name field and version field
     * In order to search for vulnerabilities name and version are required
     *
     * @param name    Name of the component
     * @param version Version of the component
     */
    public Component(String name, String version) {
        this();
        this.name = name;
        this.version = version;
        this.publisher = "Unknown";
    }

    /**
     * Constructs a component with all attributes
     *
     * @param name      Name of the component
     * @param publisher Publisher of the component
     * @param version   Version number of the component
     */
    public Component(String name, String publisher, String version) {
        this(name, version);
        this.publisher = publisher;
    }

    /**
     * Constructs a component unique to packages in SPDX files without cpe/purl/swid
     *
     * @param name      Name of the component
     * @param publisher Publisher of the component
     * @param version   Version number of the component
     * @param SPDXid    SPDX ID
     */
    public Component(String name, String publisher, String version, String SPDXid) {
        this(name, publisher, version);
        this.SPDXid = SPDXid;
    }

    /**
     * Constructs a component with all attributes
     *
     * @param name      Name of the component
     * @param publisher Publisher of the component
     * @param version   Version number of the component
     * @param CPE       Set of CPEs of the component
     * @param PURL      Set of PURLs of the component
     * @param SWID      SWID of the component
     */
    public Component(String name, String publisher, String version, Set<String> CPE, Set<String> PURL, Set<String> SWID) {
        this(name, publisher, version);
        this.CPE = CPE;
        this.PURL = PURL;
        this.SWID = SWID;
    }

    ///
    /// Getters and Setters
    ///

    public UUID getUUID() { return uuid; }

    protected void setUUID(UUID componentUUID) { this.uuid = componentUUID; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addLicense(String license) {
        licenses.add(license);
    }

    public Set<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(HashSet<String> licenses) {
        this.licenses = licenses;
    }

    public String getLicense(String license) {
        return licenses.stream()
                .filter(license::equals)
                .findAny()
                .orElse(null);
    }

    /**
     * Set if the component is unpackaged or not
     *
     * @param unpackaged If component is unpackaged
     */
    public void setUnpackaged(boolean unpackaged) {
        this.unpackaged = unpackaged;
    }

    public boolean isUnpackaged() {
        return unpackaged;
    }

    public void addChild(UUID child) {
        children.add(child);
    }

    public Set<UUID> getChildren() {
        return children;
    }

    public void removeChild(UUID child) {
        children.remove(child);
    }

    public Set<String> getCPE() {
        return CPE;
    }

    public void setCPE(Set<String> cpe) {
        this.CPE = cpe;
    }

    public void addCPE(String cpe) {
        this.CPE.add(cpe);
    }

    public Set<String> getPURL() {
        return PURL;
    }

    public void setPURL(Set<String> PURL) {
        this.PURL = PURL;
    }

    public void addPURL(String purl) {
        this.PURL.add(purl);
    }

    public Set<String> getSWID() {
        return SWID;
    }

    public void setSWID(Set<String> swid) {
        this.SWID = swid;
    }

    public void addSWID(String swid) {
        this.SWID.add(swid);
    }

    public String getSPDXID() {
        return SPDXid;
    }

    public void setSPDXID(String spdxid) {
        this.SPDXid = spdxid;
    }

    public Set<ComponentConflict> getConflicts() {
        return componentConflicts;
    }

    /**
     * Adds a conflict to the component
     *
     * @param componentConflict Conflict to add
     */
    public void addConflict(ComponentConflict componentConflict) {
        componentConflicts.add(componentConflict);
    }

    /**
     * Add a vulnerability to the list of vulnerabilities for this component
     *
     * @param vulnerability Vulnerability to add to this component
     */
    public void addVulnerability(Vulnerability vulnerability) {
        vulnerabilities.add(vulnerability);
    }

    /**
     * Get a set of all vulnerabilities for this component
     *
     * @return The set of all vulnerabilities
     */
    public Set<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    /**
     * Copy a component's attributes to this component
     *
     * @param component Component to copy from
     */
    public void copyFrom(Component component) {
        this.name = component.name;
        this.publisher = component.publisher;
        this.CPE = new HashSet<>(component.CPE);
        this.PURL = new HashSet<>(component.PURL);
        this.SWID = new HashSet<>(component.SWID);
        this.children.addAll(component.children);
        this.version = component.version;
        this.vulnerabilities.addAll(component.vulnerabilities);
        this.licenses = component.licenses;
    }

    @Override
    public String toString() {
        // Only add what is not null
        StringBuilder sb = new StringBuilder();
        if (this.publisher != null) {
            sb.append(this.publisher).append(" ");
        }
        if (this.name != null) {
            // This should never happen, but may as well guard against it
            sb.append(this.name);
        }
        if (this.version != null) {
            sb.append(":").append(this.version);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        //multiply the hash of every vulnerability together
        int vulnerabilitiesHash = 1;
        for (Vulnerability vulnerability : vulnerabilities) {
            vulnerabilitiesHash *= vulnerability.hashCode();
        }
        return Objects.hash(name, publisher, CPE, PURL, SWID, children, version, licenses) * vulnerabilitiesHash;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Component) {
            Component otherComponent = (Component) other;
            // Check each field for null
            boolean retval = true;

            if (otherComponent.name != null) {
                retval = retval && otherComponent.name.equals(this.name);
            } else {
                retval = retval && this.name == null;
            }

            if (otherComponent.publisher != null) {
                retval = retval && otherComponent.publisher.equals(this.publisher);
            } else {
                retval = retval && this.publisher == null;
            }

            if (otherComponent.CPE != null) {
                retval = retval && otherComponent.CPE.equals(this.CPE);
            } else {
                retval = retval && this.CPE == null;
            }

            if (otherComponent.PURL != null) {
                retval = retval && otherComponent.PURL.equals(this.PURL);
            } else {
                retval = retval && this.PURL == null;
            }

            if (otherComponent.SWID != null) {
                retval = retval && otherComponent.SWID.equals(this.SWID);
            } else {
                retval = retval && this.SWID == null;
            }

            if (otherComponent.children != null) {
                retval = retval && otherComponent.children.equals(this.children);
            } else {
                retval = retval && this.children == null;
            }

            if (otherComponent.version != null) {
                retval = retval && otherComponent.version.equals(this.version);
            } else {
                retval = retval && this.version == null;
            }

            if (otherComponent.licenses != null) {
                retval = retval && otherComponent.licenses.equals(this.licenses);
            } else {
                retval = retval && this.licenses == null;
            }

            if (otherComponent.vulnerabilities != null) {
                retval = retval && otherComponent.vulnerabilities.equals(this.vulnerabilities);
            } else {
                retval = retval && this.vulnerabilities == null;
            }

            return retval;
        }
        return false;
    }
}
