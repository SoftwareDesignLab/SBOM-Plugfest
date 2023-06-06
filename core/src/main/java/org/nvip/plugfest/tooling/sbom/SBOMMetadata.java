package org.nvip.plugfest.tooling.sbom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.nvip.plugfest.tooling.qa.tests.Result;

import javax.annotation.Nullable;
import java.util.*;

/**
 * File: SBOMMetadata.java
 *
 * Stores all metadata fields that can be found in an SBOM, schema-independent.
 *
 * @author Ian Dunn
 */
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SBOMMetadata {

    //#region Attributes

    /**
     * The timestamp of when the SBOM was generated.
     */
    private String timestamp;

    /**
     * A map of tools used to create the SBOM. Maps from the tool name to the tool string.
     */
    private Map<String, String> tools;

    /**
     * A list of suppliers (Person, Organization) that generated the SBOM.
     */
    private Set<String> suppliers;

    /**
     * A map of metrics on the metadata. Maps from a processor name to a list of Results from that processor.
     */
    private Map<String, List<Result>> metrics;

    /**
     * The data license of the SBOM. This is an SPDX-only field.
     */
    private String dataLicense;

    /**
     * The license list version of the SBOM. This is an SPDX-only field.
     */
    private String licenseListVersion;

    //#endregion

    //#region Constructors

    /**
     * Default, no arguments constructor. Initializes everything to null or an empty collection.
     */
    public SBOMMetadata() {
        this.timestamp = null;
        this.tools = new HashMap<>();
        this.suppliers = new HashSet<>();
        this.metrics = new HashMap<>();
        dataLicense = null;
    }

    /**
     * Constructor to create an SBOMMetadata instance with a timestamp.
     *
     * @param timestamp The timestamp of the metadata.
     */
    public SBOMMetadata(String timestamp) {
        this();
        this.timestamp = timestamp;
    }

    /**
     * Constructor to create an SBOMMetadata instance with a timestamp and suppliers.
     *
     * @param timestamp The timestamp of the metadata.
     * @param suppliers The suppliers of the metadata.
     */
    public SBOMMetadata(String timestamp, Set<String> suppliers) {
        this(timestamp);
        this.suppliers = suppliers;
    }

    /**
     * Constructor to create an SBOMMetadata instance with a timestamp, suppliers, and tools.
     *
     * @param timestamp The timestamp of the metadata.
     * @param suppliers The suppliers of the metadata.
     * @param tools The tools of the metadata.
     */
    public SBOMMetadata(String timestamp, Set<String> suppliers, Map<String, String> tools) {
        this(timestamp, suppliers);
        this.tools = tools;
    }

    //#endregion

    //#region Getters

    /**
     * Gets the size of the metadata object (i.e. how many fields are not null or empty). Each supplier and tool is
     * counted as an individual field. This does not count the metrics.
     *
     * @return The size of the metadata object.
     */
    @JsonIgnore
    public int getSize() {
        int size = tools.size() + suppliers.size();

        if (timestamp != null) size++;
        if (dataLicense != null) size++;
        if (licenseListVersion != null) size++;

        return size;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getTools() {
        return tools;
    }

    public Set<String> getSuppliers() {
        return suppliers;
    }

    public Map<String, List<Result>> getMetrics() {
        return metrics;
    }

    public String getDataLicense() {
        return dataLicense;
    }

    public String getLicenseListVersion() {
        return licenseListVersion;
    }

    //#endregion

    //#region Setters

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTools(Map<String, String> tools) {
        this.tools = tools;
    }

    /**
     * Adds a tool with a vendor, name, and version. The minimum requirement is a non-null name.
     *
     * @param toolVendor The vendor of the tool.
     * @param toolName The name of the tool.
     * @param toolVersion The version of the tool.
     */
    public void addTool(@Nullable String toolVendor, @NonNull String toolName, @Nullable String toolVersion) {
        this.tools.put(toolName, String.format("Tool:%s %s%s",
                toolVendor == null ? "" : " " + toolVendor,
                toolName,
                toolVersion == null ? "" : "-" + toolVersion));
    }

    public void addSupplier(String supplier) {
        if (this.suppliers == null)
            this.suppliers = new HashSet<>();
        this.suppliers.add(supplier);
    }

    public void setSuppliers(Set<String> suppliers) {
        this.suppliers = suppliers;
    }

    public void setDataLicense(String dataLicense) {
        this.dataLicense = dataLicense;
    }

    public void setLicenseListVersion(String licenseListVersion) {
        this.licenseListVersion = licenseListVersion;
    }

    //#endregion

    //#region Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SBOMMetadata metadata)) return false;

        if (!Objects.equals(timestamp, metadata.timestamp)) return false;
        if (!Objects.equals(tools, metadata.tools)) return false;
        if (!Objects.equals(suppliers, metadata.suppliers)) return false;
        if (!Objects.equals(dataLicense, metadata.dataLicense))
            return false;
        return Objects.equals(licenseListVersion, metadata.licenseListVersion);
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (tools != null ? tools.hashCode() : 0);
        result = 31 * result + (suppliers != null ? suppliers.hashCode() : 0);
        result = 31 * result + (dataLicense != null ? dataLicense.hashCode() : 0);
        result = 31 * result + (licenseListVersion != null ? licenseListVersion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (timestamp!= null)
            sb.append("Timestamp: ").append(timestamp).append("; ");
        if (tools != null && tools.size() > 0)
            sb.append("Tools: ").append(tools).append("; ");
        if (suppliers != null && suppliers.size() > 0)
            sb.append("Suppliers: ").append(suppliers).append("; ");
        if (dataLicense!= null)
            sb.append("Data License: ").append(dataLicense).append(";");
        if (dataLicense!= null)
            sb.append("License List Version: ").append(licenseListVersion);

        return sb.append("\n").toString();
    }

    //#endregion

}
