package plugfest.tooling.sbom;

import com.google.common.collect.*;


import java.util.*;

/**
 * File: SBOM.java
 * Hold's relevant data from a submitted SBOM
 *
 * @author Tyler Drake
 */
public class SBOM {

    // Raw data from SBOM
    // Organized as [Data(1) ... Data(n)]
    List<String> data;

    // Items from SBOM header
    // Organized as [Value(1) ... Value(n)]
    LinkedHashSet<String> header;

    // Components from the SBOM
    // Organized as [Unique Identifier : Component Object]
    HashMap<String, Component> components;

    // All other relationships between components/items in sbom
    List<String> relationships;

    // Error log for any conflicts or other issues detected
    // Organized as [Error(1) ... Error(n)]
    LinkedHashSet<String> errors;

    public SBOM() {
        this.data = new ArrayList<>();
        this.header = new LinkedHashSet<>();
        this.components = new HashMap<>();
        this.relationships = new ArrayList<>();
    }

    public void addData(String line) {
        this.data.add(line);
    }

    public void addToHeader(String value) {
        this.header.add(value);
    }

    public void addComponent(String key, Component component) {
        if(components.containsKey(key)) {
            this.errors.add("Duplicate Component with ID " + key + " found.");
        }
        this.components.put(key, component);
    }

    public void addRelationship(String value) { this.relationships.add(value); }

}
