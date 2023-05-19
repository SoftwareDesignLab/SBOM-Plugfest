package org.nvip.plugfest.tooling.sbom;

import jregex.Matcher;
import jregex.Pattern;

import java.util.*;

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
            "(?:/*)" +    // Skip over any/all '/' characters
            "([\\w\\d.+\\-]*)/" +                     // Get type
            "(?(?=[\\w\\.\\+\\-\\%^@#?]*\\/)(.*?)\\/)" +    // Get namespaces if present
            "([\\w.+\\-%]*)" +    //  Get name
            "(?(?=.*@.*(?:\\?|#|$))@(.*?)(?=\\?|#|$))" +    // Get version, if present
            "(?(?=.*\\?.*(?:#|$))\\?(.*?)(?=#|$))" +        // Get qualifiers if present
            "(?(?=.*#.*$)#(.*?)$)";     // Get subpath, if present

    private String scheme;  // required
    private String type;    // required
    private List<String> namespace;   // Optional and type-specific
    private String name;    // required
    private String version; // Optional
    private LinkedHashMap<String, String> qualifiers = null;    // Optional
    private String subpath; // Optional

    private ComponentPackageManager pm;
    private String PURLString;

    /**
     * Create purl object from given purl String. Will error if purl fails
     * @param PURL
     */
    public PURL(String PURL){
        Pattern purlPattern = new Pattern(this.PURL_REGEX, Pattern.MULTILINE);
        try{
            Matcher matcher = purlPattern.matcher(PURL);

            // Regex fails to match to string
            if(!matcher.find())
                throw new Exception("Unable to parse purl \"" + PURL + "\"");

            // Check for required fields
            if(matcher.group(1) == null || matcher.group(2) == null || matcher.group(4) == null){
                throw new Exception("Invalid purl, missing the following: "+
                        ( matcher.group(1) == null ? "Schema " : "" ) +
                        ( matcher.group(1) == null ? "Type " : "" ) +
                        ( matcher.group(1) == null ? "Name " : "" )
                );
            }

            // Build purl object
            this.scheme = matcher.group(1);
            this.type = matcher.group(2);
            if(matcher.group(3) != null)
                this.namespace = Arrays.stream(matcher.group(3).split("/")).toList();   // can be 0 - n namespaces
            this.name = matcher.group(4);
            this.version = matcher.group(5);

            // Build qualifiers if present
            if(matcher.group(6) != null){
                this.qualifiers = new LinkedHashMap<>();
                // Add all key=value pairs for the quantifier
                for(String qualifier : matcher.group(6).split("&")){
                    String[] keyVal = qualifier.split("=");
                    this.qualifiers.put(keyVal[0], keyVal[1]);
                }
            }

            this.subpath = matcher.group(7);

        } catch (Exception e){
            System.err.println(e.getMessage());
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

    ///
    /// Overrides
    ///

    @Override
    public String toString() {
        // scheme:type/namespace/name@version?qualifiers#subpath
        // Build namespaces
        StringBuilder namespace = new StringBuilder();
        if(this.namespace != null)
            for(String n : this.namespace)
                namespace.append("/").append(n);

        // Build qualifiers
        StringBuilder qualifiers = new StringBuilder();
        if(this.qualifiers != null){
            for(String key : this.qualifiers.keySet())
                qualifiers.append(key).append("=").append(this.qualifiers.get(key)).append("&");
            qualifiers.deleteCharAt(qualifiers.length() - 1);   // truncate last '&'
        }

        // build final purl
        return this.scheme + ":"
                + this.type +
                namespace +
                "/" + this.name +
                (this.version != null ? "@" + this.version : "") +
                (!qualifiers.isEmpty() ? "?" + qualifiers : "") +
                (this.subpath != null ? "#" + this.subpath : "");
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