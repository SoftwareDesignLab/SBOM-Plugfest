package org.nvip.plugfest.tooling.sbom;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.nvip.plugfest.tooling.qa.tests.Result;

import javax.annotation.Nullable;
import java.util.*;

public class SBOMMetadata {
    private String timestamp;
    private Map<String, String> tools;
    private String suppliers;
    private Component component;
    private Map<String, List<Result>> metrics;
    private String dataLicense;

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

    public SBOMMetadata(String timestamp, String suppliers) {
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

    public String getSuppliers() {
        return suppliers;
    }

    public void addSupplier(String supplier) {
        if (this.suppliers == null) {
            this.suppliers = supplier;
            return;
        }

        this.suppliers += "; " + supplier;
    }

    public void setSuppliers(String suppliers) {
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
}
