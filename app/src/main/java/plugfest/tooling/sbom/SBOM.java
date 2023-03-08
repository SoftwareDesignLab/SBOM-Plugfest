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

    Multimap<String, String> header;

    HashMap<String, Component> components;

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
