package plugfest.tooling.sbom;

import com.google.common.collect.Multimap;

/**
 * File: Component.java
 * Hold's relevant data for a component in an SBOM
 *
 * @author Tyler Drake
 */
public class Component {

    Multimap<String, String> component_information;

    public Component() {
    }

    public void addInformation(String key, String value) {
        component_information.put(key, value);
    }

}
