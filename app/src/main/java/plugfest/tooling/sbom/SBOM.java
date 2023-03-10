package plugfest.tooling.sbom;

import com.google.common.collect.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * File: SBOM.java
 * Hold's relevant data from a submitted SBOM
 *
 * @author Tyler Drake
 */
public class SBOM {

    // Raw data from SBOM
    // Organized as [Data(1) ... Data(n)]
    HashSet<String> data;

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
        this.data = new HashSet<>();
        this.header = new HashSet<>();
        this.components = new HashMap<>();
        this.relationships = ArrayListMultimap.create();
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

    public void addRelationship(String key, String value) {
        this.relationships.put(key, value);
    }

}
