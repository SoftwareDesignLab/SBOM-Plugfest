package org.nvip.plugfest.tooling.sbom;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.nvip.plugfest.tooling.qa.tests.Result;

import javax.annotation.Nullable;
import java.util.*;

public class SBOMMetadata {
    private String timestamp;
    private Map<String, String> tools;
    private Set<String> suppliers;
    private Component component;
    private Map<String, List<Result>> metrics;
    private String dataLicense;
    private String licenseListVersion;

    public SBOMMetadata() {
        this.timestamp = null;
        this.tools = new HashMap<>();
        this.suppliers = null;
        this.component = null;
        this.metrics = new HashMap<>();
        dataLicense = null;
    }

    public SBOMMetadata(String timestamp) {
        this();
        this.timestamp = timestamp;
    }

    public SBOMMetadata(String timestamp, Set<String> suppliers) {
        this(timestamp);
        this.suppliers = suppliers;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getTools() {
        return tools;
    }

    public void setTools(Map<String, String> tools) {
        this.tools = tools;
    }

    public void addTool(@Nullable String toolVendor, @NonNull String toolName, @Nullable String toolVersion) {
        this.tools.put(toolName, String.format("Tool:%s %s%s",
                toolVendor == null ? "" : " " + toolVendor,
                toolName,
                toolVersion == null ? "" : "-" + toolVersion));
    }

    public Set<String> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(String supplier) {
        if (this.suppliers == null)
            this.suppliers = new HashSet<>();
        this.suppliers.add(supplier);
    }

    public void setSuppliers(Set<String> suppliers) {
        this.suppliers = suppliers;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Map<String, List<Result>> getMetrics() {
        return metrics;
    }

    public String getDataLicense() {
        return dataLicense;
    }

    public void setDataLicense(String dataLicense) {
        this.dataLicense = dataLicense;
    }

    public String getLicenseListVersion() {
        return licenseListVersion;
    }

    public void setLicenseListVersion(String licenseListVersion) {
        this.licenseListVersion = licenseListVersion;
    }

    public int getSize() {
        int size = tools.size() + suppliers.size();

        if (timestamp != null) size++;
        if (component != null) size++;
        if (dataLicense != null) size++;

        return size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SBOMMetadata that)) return false;

        if (!Objects.equals(timestamp, that.timestamp)) return false;
        if (!tools.equals(that.tools)) return false;
        if (!Objects.equals(suppliers, that.suppliers)) return false;
        if (!Objects.equals(component, that.component)) return false;
        return Objects.equals(dataLicense, that.dataLicense);
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + tools.hashCode();
        result = 31 * result + (suppliers != null ? suppliers.hashCode() : 0);
        result = 31 * result + (component != null ? component.hashCode() : 0);
        result = 31 * result + (dataLicense != null ? dataLicense.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if(timestamp!= null)
            sb.append("Timestamp: ").append(timestamp).append("; ");
        if(tools!= null)
            sb.append("Tools: ").append(tools).append("; ");
        if(suppliers!= null && !suppliers.toString().equals("[null]"))
            sb.append("Suppliers: ").append(suppliers).append("; ");
        if(component!= null && component.toString().length() > 0)
            sb.append("Component: ").append(component).append("; ");
        if(dataLicense!= null)
            sb.append("Data License: ").append(dataLicense).append(";");

        return sb.toString();

    }
}
