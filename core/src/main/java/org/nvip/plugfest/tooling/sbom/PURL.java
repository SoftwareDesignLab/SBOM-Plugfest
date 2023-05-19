package org.nvip.plugfest.tooling.sbom;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <b>File</b>: PURL.java<br>
 * <b>Description</b>: Object representation of the Package URl for a component
 * Representation details can be found here: <a href="https://github.com/package-url/purl-spec/tree/master">...</a>
 *
 * @author Juan Francisco Patino
 * @author Matt London
 * @author Derek Garcia
 */
public class PURL {

    // Purl scheme: scheme:type/namespace/name@version?qualifiers#subpath
    private final String PURL_REGEX =
            "(.*?):" +      // Get scheme
            "(?:\\/*)" +    // Skip over any/all '/' characters
            "([\\w\\d\\.\\+\\-]*)\\/" +                     // Get type
            "(?(?=[\\w\\.\\+\\-\\%^@#?]*\\/)(.*?)\\/)" +    // Get namespaces if present
            "([\\w\\.\\+\\-\\%]*)" +    //  Get name
            "(?(?=.*@.*(?:\\?|#|$))@(.*?)(?=\\?|#|$))" +    // Get version, if present
            "(?(?=.*\\?.*(?:#|$))\\?(.*?)(?=#|$))" +        // Get qualifiers if present
            "(?(?=.*#.*$)#(.*?)$)";     // Get subpath, if present

    private String scheme;  // required
    private String type;    // required
    private List<String> namespace;   // Optional and type-specific
    private String name;    // required
    private String version; // Optional
    private HashMap<String, String> data;    // comprised of qualifiers:subpath, optional

    private ComponentPackageManager pm;
    private String PURLString;

    public PURL(String PURL){
        this.PURLString = PURL;
        addFromString();
    }

    public PURL(){}

    /**
     * Helper function to convert the string representation to a class
     * given the object already contains a PURL String
     */
    public void addFromString(){
        assert PURLString != null;
        addFromString(PURLString);
    }

    /**
     * Helper function to convert the string representation to a class
     * @param purl the PURL String
     */
    public void addFromString(String purl){
        String p = purl.toLowerCase();
        if(p.contains("alpine"))
            setPackageManager(ComponentPackageManager.ALPINE);
        else if(p.contains("debian"))
            setPackageManager(ComponentPackageManager.DEBIAN);
        else // add cases here as PMs are added
            setPackageManager(ComponentPackageManager.PYTHON);

        switch (this.pm){
            case ALPINE:
                String[] purlSplit = p.split("[/@]");
                this.name = purlSplit[2];
                purlSplit = p.split("[@?]");
                this.version = purlSplit[1];
                break;
            case DEBIAN:
        }

    }

    ///
    /// Getters and Setters
    ///

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ComponentPackageManager getPackageManager() {
        return pm;
    }

    public void setPackageManager(ComponentPackageManager pm) {
        this.pm = pm;
    }

    public void setPURLString(String PURLString) {
        this.PURLString = PURLString;
    }

    public String getPURLString() {
        return PURLString;
    }

    ///
    /// Overrides
    ///

    @Override
    public String toString() {
        return PURLString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PURL purl)) return false;
        return Objects.equals(getName(), purl.getName()) && Objects.equals(getVersion(), purl.getVersion()) && pm == purl.pm && Objects.equals(PURLString, purl.PURLString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getVersion(), pm, PURLString);
    }
}