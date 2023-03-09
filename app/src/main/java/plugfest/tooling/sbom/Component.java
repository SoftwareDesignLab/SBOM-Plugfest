package plugfest.tooling.sbom;

import com.google.common.collect.Multimap;

import java.util.HashSet;

/**
 * File: Component.java
 * Hold's relevant data for a component in an SBOM
 *
 * @author Tyler Drake
 */
public class Component {

    HashSet<String> component_information;

    public Component() {
    }

    public void addInformation(String value) {
        component_information.add(value);
    }

}
