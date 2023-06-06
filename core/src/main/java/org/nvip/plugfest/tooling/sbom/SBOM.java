package org.nvip.plugfest.tooling.sbom;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File: SBOM.java
 * Represents a single component inside an SBOM
 *
 * @author Matt London
 * @author Kevin LaPorte
 */
public class SBOM {

    // Common SBOM Schemas
    public enum Type {
        CYCLONE_DX,
        SPDX,
        Other
    }

    /**
     * Dependency tree of the components
     */
    private final DependencyTree dependencyTree;

    /**
     * Type of SBOM which this object came from (whatever the bom.xml, bom.json was formatted in)
     */
    private Type originFormat;

    /**
     * Specification version of the origin format
     * Example: For CycloneDX 1.4, this would be 1.4
     */
    private String specVersion;

    /**
     * Version of this sbom for the specific project (1 if first generated sbom, n if nthed)
     */
    private String sbomVersion;

    /**
     * Serial number of the sbom matching regex (^urn:uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$)
     */
    private String serialNumber;

    /**
     * Signatures on the SBOM
     */
    private Set<Signature> signature;

    /**
     * Metadata of SBOM
     */
    private SBOMMetadata metadata;

    /**
     * Project information - the head component of the project
     */
    private Component project;

    /**
     * Default constructor
     */
    public SBOM() {
        this.dependencyTree = new DependencyTree();
        this.signature = new HashSet<>();
        this.serialNumber = "urn:uuid:" + UUID.randomUUID();
        this.metadata = new SBOMMetadata();
    }

    /**
     * Constructor to take all parameters except for DependencyTree
     *
     * @param originFormat : original format SBOM was sent in
     * @param specVersion  :  Version of this Object
     * @param sbomVersion  :  Version of the SBOM
     * @param authors       : SBOM contributors
     * @param serialNumber : Serial number of the SBOM
     * @param timestamp    :    Timestamp of when this SBOM was created
     * @param signature    :    signature to verify the SBOM
     */
    public SBOM(String originFormat, String specVersion, String sbomVersion, Set<String> authors, String serialNumber, String timestamp, Set<Signature> signature) {
        this(originFormat, specVersion, sbomVersion, authors, serialNumber, timestamp, signature, new DependencyTree());
    }

    /**
     * Takes all parameters, will be used when merging boms so the tree can be built manually
     *
     * @param originFormat : original format SBOM was sent in
     * @param specVersion  : Version of this Object
     * @param sbomVersion  : Version of the SBOM
     * @param serialNumber : Serial number of the SBOM
     * @param timestamp    : Timestamp of when this SBOM was created
     * @param suppliers     : Manufacturer of the software the SBOM is about
     * @param signature    : signature to verify the SBOM
     */
    public SBOM(String originFormat, String specVersion, String sbomVersion, Set<String> suppliers,
                String serialNumber, String timestamp, Set<Signature> signature, DependencyTree dependencyTree) {
        this.originFormat = assignOriginFormat(originFormat);
        this.specVersion = specVersion;
        this.sbomVersion = sbomVersion;
        this.dependencyTree = dependencyTree;
        this.serialNumber = serialNumber;
        this.signature = signature;
        this.metadata = new SBOMMetadata(timestamp, suppliers);
    }

    /**
     * Takes all parameters, will be used when merging boms so the tree can be built manually
     *
     * @param originFormat: original format SBOM was sent in
     * @param specVersion:  Version of this Object
     * @param sbomVersion:  Version of the SBOM
     * @param serialNumber: Serial number of the SBOM
     * @param timestamp:    Timestamp of when this SBOM was created
     * @param signature:    signature to verify the SBOM
     */
    public SBOM(Type originFormat, String specVersion, String sbomVersion, Set<String> suppliers, String serialNumber,
                String timestamp, Set<Signature> signature, DependencyTree dependencyTree) {
        this.originFormat = originFormat;
        this.specVersion = specVersion;
        this.sbomVersion = sbomVersion;
        this.dependencyTree = dependencyTree;
        this.serialNumber = serialNumber;
        this.signature = signature;
        this.metadata = new SBOMMetadata(timestamp, suppliers);
    }

    /**
     * Copy constructor for the SBOM (THIS DOES NOT COPY COMPONENTS)
     *
     * @param from SBOM to copy from
     */
    public SBOM(SBOM from) {
        // This sets the dependencytree to null so it does not allow copying of dependencies
        this(from.getOriginFormat(), from.getSpecVersion(), from.getSbomVersion(),
                from.getMetadata().getSuppliers(),
                from.getSerialNumber(),
                from.getMetadata().getTimestamp(), from.getSignature(), null);
    }

    /**
     * Get a set of all components in the project, including the head component.
     *
     * @return Set of components
     */
    @JsonIgnore
    public Set<Component> getAllComponents() {
        return dependencyTree.getAllComponents();
    }

    /**
     * Get a set of all components in the project that are children of the head component.
     *
     * @return A map from the component name to the component.
     */
    public Map<String, Component> getComponents() {
        Set<Component> components = getAllComponents().stream()
                .filter(c -> c.getUUID() != getHeadUUID()) // Filter out head component
                .collect(Collectors.toSet());

        return components.stream().collect(Collectors.toMap(Component::getName, component -> component));
    }

    /**
     * Add a component into the dependency tree
     *
     * @param parent Parent UUID (null for root component)
     * @param toAdd  Component to add into the tree
     * @return UUID of added component (null if failed)
     */
    public UUID addComponent(UUID parent, Component toAdd) {
        if (parent == null) this.project = toAdd;
        return dependencyTree.addComponent(parent, toAdd);
    }

    /**
     * Get a component's children from the component's UUID
     *
     * @param parent UUID of the parent component
     * @return Set of children components (null if failed)
     */
    public Set<Component> getComponentChildren(UUID parent) {
        Set<Component> componentChildren;

        componentChildren = dependencyTree.getComponentChildren(parent);

        return componentChildren;
    }

    public Set<UUID> getChildrenUUIDs(UUID parent) {
        Set<UUID> componentChildren;

        componentChildren = dependencyTree.getChildrenUUIDs(parent);

        return componentChildren;

    }

    /**
     * Get the head component of the dependency tree
     *
     * @return Head component UUID (null if failed)
     */
    @JsonIgnore
    public UUID getHeadUUID() {
        return dependencyTree.getHeadUUID();
    }

    /**
     * Get a component by its UUID
     *
     * @param componentUUID UUID to retrieve
     * @return Component object (null if failed)
     */
    public Component getComponent(UUID componentUUID) {
        return dependencyTree.getComponent(componentUUID);
    }

    /**
     * Convert string to SBOM Type Enum
     *
     * @param format SBOM Format string to convert to Format Enum
     * @return SBOM Type
     */
    public Type assignOriginFormat(String format) {
        if (format != null) {
            if (format.toLowerCase().contains("cyclonedx")) {
                return Type.CYCLONE_DX;
            } else if (format.toLowerCase().contains("spdx")) {
                return Type.SPDX;
            }
        }
        return Type.Other;
    }

    /**
     * Recurse through the tree and sum component string code multiplied by the depth
     *
     * @param parent UUID of parent to loop through
     * @param depth  Current depth of the component
     * @return Integer that will be the hashcode of the dependencytree
     */
    private int sumComponents(UUID parent, int depth) {
        Component currentParent = this.getComponent(parent);
        // Calculate the initial value for this component
        int retVal = currentParent.getName().hashCode() * depth;
        if (currentParent.getChildren().size() == 0) {
            // If no children then sum and return
            return retVal;
        }

        // Otherwise, we need to recurse through all children and sum them
        for (UUID child : currentParent.getChildren()) {
            retVal += sumComponents(child, depth + 1);
        }

        return retVal;
    }

    ///
    /// Getters and Setters
    ///

    public Type getOriginFormat() {
        return originFormat;
    }

    public void setOriginFormat(Type originFormat) {
        this.originFormat = originFormat;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getSbomVersion() {
        return sbomVersion;
    }

    public void setSbomVersion(String sbomVersion) {
        this.sbomVersion = sbomVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Set<Signature> getSignature() {
        return signature;
    }

    public void setSignature(Set<Signature> signature) {
        this.signature = signature;
    }

    public SBOMMetadata getMetadata(){
        return metadata;
    }

    public void setMetadata(SBOMMetadata metadata) {
        this.metadata = metadata;
    }

    public Component getProject() {
        return project;
    }

    ///
    /// Overrides
    ///

    @Override
    public String toString() {
        return "\nSBOM Information\n" +
                "  + Serial Number: " + getSerialNumber() + "\n" +
                "  + Version: " + getSpecVersion() + "\n" +
                "  + Tool Version: " + getSbomVersion() + "\n" +
                "  + Metadata: " + getMetadata() + "\n";
    }

    /**
     * Traverse the tree and add the name of the component times the depth of it and sum that together
     *
     * @return Hash of the SBOM
     */
    @Override
    public int hashCode() {
        int retVal = 0;
        // Get hashcode of SBOM params
        if (this.getOriginFormat() != null) {
            retVal += this.getSbomVersion().hashCode();
        }
        if (this.getMetadata() != null) {
            retVal += this.getMetadata().hashCode();
        }
        if (this.getSerialNumber() != null) {
            retVal += this.getSerialNumber().hashCode();
        }
        if (this.getSpecVersion() != null) {
            retVal += this.getSpecVersion().hashCode();
        }
        // Get hashcode of dependency tree
        if (this.dependencyTree != null && this.getAllComponents().size() > 0) {
            retVal += sumComponents(this.getHeadUUID(), 1);

        }

        // Now we can return
        return retVal;
    }

    /**
     * Check if the component is in the dependency tree
     *
     * @param componentUUID UUID of the component to check
     * @return True if in the tree, false otherwise
     */
    public boolean hasComponent(UUID componentUUID) {
        return dependencyTree.hasComponent(componentUUID);
    }

    /**
     * Make sure a dependency tree exists
     *
     * @return True if tree was initialized, false otherwise
     */
    public boolean hasDependencyTree() {
        return dependencyTree != null;
    }

}
