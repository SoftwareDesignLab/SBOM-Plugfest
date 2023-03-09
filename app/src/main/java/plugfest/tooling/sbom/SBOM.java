package plugfest.tooling.sbom;

import com.google.common.collect.Multimap;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * File: SBOM.java
 * Hold's relevant data from a submitted SBOM
 *
 * @author Tyler Drake
 */
public class SBOM {

    // Items from SBOM header
    // Organized as [Value(1) ... Value(n)]
    HashSet<String> header;

    // Components from the SBOM
    // Organized as [Unique Identifier : Component Object]
    HashMap<String, Component> components;

    // Relationships between each Component
    // Organized as [Unique Identifier : Unique Identifier(1) ... Unique Identifier(n)]
    Multimap<String, String> relationships;

    // Error log for any conflicts or other issues detected
    // Organized as [Error(1) ... Error(n)]
    HashSet<String> errors;

    public SBOM() {
    }

    public void addToHeader(String value) {
        header.add(value);
    }

    public void addComponent(String key, Component component) {
        if(components.containsKey(key)) {
            errors.add("Duplicate Component with ID " + key + " found.");
        }
        components.put(key, component);
    }

    public void addRelationship(String key, String value) {
        relationships.put(key, value);
    }

}
