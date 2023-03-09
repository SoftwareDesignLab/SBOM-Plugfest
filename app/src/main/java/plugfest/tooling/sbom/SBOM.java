package plugfest.tooling.sbom;

import com.google.common.collect.Multimap;

import java.util.HashMap;

/**
 * File: SBOM.java
 * Hold's relevant data from a submitted SBOM
 *
 * @author Tyler Drake
 */
public class SBOM {

    // Items from SBOM header
    // Organized as [Key: Value(1) ... Value(n)]
    Multimap<String, String> header;

    // Components from the SBOM
    // Organized as [Unique Identifier : Component Object]
    HashMap<String, Component> components;

    // Relationships between each Component
    // Organized as [Unique Identifier : Unique Identifier(1) ... Unique Identifier(n)]
    Multimap<String, String> relationships;

    public SBOM() {
    }

    public void addToHeader(String key, String value) {
        header.put(key, value);
    }

    public void addComponent(String key, Component component) {
        components.put(key, component);
    }

    public void addRelationship(String key, String value) {
        relationships.put(key, value);
    }

}
