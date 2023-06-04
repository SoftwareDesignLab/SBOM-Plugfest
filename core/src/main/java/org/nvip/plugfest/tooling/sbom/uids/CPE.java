package org.nvip.plugfest.tooling.sbom.uids;

import jregex.Matcher;
import jregex.Pattern;
import org.nvip.plugfest.tooling.Debug;

/**
 * file: CPE.java
 *
 * Class representation of a CPE
 * Specifications can be found in NIST's Common Platform Enumeration: Naming Specification Version 2.3
 * https://nvlpubs.nist.gov/nistpubs/Legacy/IR/nistir7695.pdf
 * 
 * @author Derek Garcia
 */
public class CPE {


    /*
    CPE's conform to the following structure:
    cpe:<cpe_version>:<part>:<vendor>:<product>:<version>:<update>:<edition>:<language>:<sw_edition>:<target_sw>:<target_hw>:<other>
     TODO: Only temporary regex, this has no built in string value checking and only for 2.3
     */
    private static final String CPE_REGEX = "cpe:2\\.3:([aho]?):(.*?):(.*?):(.*?):(.*?):(.*?):(.*?):(.*?):(.*?):(.*?):(.*?)$";

    // Supported types for CPE
    public enum Type{
        APPLICATION,
        HARDWARE,
        OPERATING_SYSTEMS
    }

    // todo required fields?
    private final String cpeVersion = "2.3";    // todo fix to support more versions
    private final Type part;

    // "Values for this attribute SHOULD describe or identify the person or organization that manufactured or created the product."
    private String vendor;

    // "Values for this attribute SHOULD describe or identify the most common and recognizable title or name of the product."
    private String product;

    // "Values for this attribute SHOULD be vendor-specific alphanumeric strings characterizing the particular release
    // version of the product."
    private String version;

    // "Values for this attribute SHOULD be vendor-specific alphanumeric strings characterizing the particular update,
    // service pack, or point release of the product."
    private String update;

    // "The edition attribute is considered deprecated in [the 2.3] specification, and it SHOULD be assigned the logical
    // value ANY except where required for backward compatibility with version 2.2 of the CPE specification. "
    private String edition;

    // "Values for this attribute SHALL be valid language tags as defined by [RFC5646], and SHOULD be used to define the
    // language supported in the user interface of the product being described"
    private String language;

    // "Values for this attribute SHOULD characterize how the product is tailored to a particular market or class
    //of end users."
    private String sw_edition;

    // "Values for this attribute SHOULD characterize the software computing environment within which the product operates"
    private String target_sw;

    // "Values for this attribute SHOULD characterize the instruction set architecture (e.g., x86) on which the
    // product being described or identified by the WFN operates"
    private String target_hw;

    // "Values for this attribute SHOULD capture any other general descriptive or identifying information which
    // is vendor- or product-specific and which does not logically fit in any other attribute value."
    private String other;


    /**
     * Create new cpe object from a given cpe identifier string
     *
     * @param cpe cpe string to use to make objects
     * @throws Exception cpe given is invalid
     */
    public CPE(String cpe) throws Exception {
        Pattern cpePattern = new Pattern(CPE_REGEX, Pattern.MULTILINE);

        Matcher matcher = cpePattern.matcher(cpe);

        // Regex fails to match to string
        if(!matcher.find()){
            Debug.log(Debug.LOG_TYPE.DEBUG, "Unable to parse cpe \"" + cpe + "\"");
            throw new Exception("Unable to parse cpe \"" + cpe + "\"");
        }


        // get type
        switch (matcher.group(1)) {
            case "a" -> this.part = Type.APPLICATION;
            case "h" -> this.part = Type.HARDWARE;
            case "o" -> this.part = Type.OPERATING_SYSTEMS;
            default -> throw new Exception("\"" + matcher.group(1) + "\" is not a valid type");
        }

        // Fill in fields
        this.vendor = matcher.group(2);
        this.product = matcher.group(3);
        this.version = matcher.group(4);
        this.update = matcher.group(5);
        this.edition = matcher.group(6);
        this.language = matcher.group(7);
        this.sw_edition = matcher.group(8);
        this.target_sw = matcher.group(9);
        this.target_hw = matcher.group(10);
        this.other = matcher.group(11);
    }

    @Override
    public String toString() {
        // cpe:<cpe_version>:<part>:<vendor>:<product>:<version>:<update>:<edition>:<language>:<sw_edition>:<target_sw>:<target_hw>:<other>

        // Get application string
        String typeString = "";
        switch (this.part) {
            case APPLICATION -> typeString = "a";
            case HARDWARE -> typeString = "h";
            case OPERATING_SYSTEMS -> typeString = "o";
        }

        return "cpe:" + this.cpeVersion +
                ":" + typeString +
                ":" + this.vendor == "null" ? "*" : this.vendor +
                ":" + this.product == null ? "*" : this.product +
                ":" + this.update == null ? "*" : this.update +
                ":" + this.edition == null ? "*" : this.edition +
                ":" + this.language == null ? "*" : this.language +
                ":" + this.sw_edition == null ? "*" : this.sw_edition +
                ":" + this.target_sw== null ? "*" : this.target_sw +
                ":" + this.target_hw == null ? "*" : this.target_hw +
                ":" + this.other == null ? "*" : this.other;
    }
}
