package plugfest.tooling.sbom;

import java.util.HashSet;


/**
 * File: Component.java
 * Hold's relevant data for a component in an SBOM
 *
 * @author Tyler Drake
 */
public class Component {

    String identifier;

    HashSet<String> component_information;

    public Component() {
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void addInformation(String value) {
        component_information.add(value);
    }

}
