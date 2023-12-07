/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.sbom;

import java.util.Objects;

/**
 * File: SBOM.java
 * Represents an application tool for an SBOM
 *
 * @author Juan Francisco Patino
 */
public class AppTool {

    /**
     * The vendor of the tool.
     */
    private String vendor;

    /**
     * The name of the tool.
     */
    private String name;

    /**
     * The version of the tool.
     */
    private String version;

    /**
     * Default constructor with no parameters. Initializes all attributes to null.
     */
    public AppTool() {
        this.vendor = null;
        this.name = null;
        this.version = null;
    }

    /**
     * Constructor with a complete set of attributes.
     *
     * @param vendor The vendor of the tool.
     * @param name The name of the tool.
     * @param version The version of the tool.
     */
    public AppTool(String vendor, String name, String version) {
        this.vendor = vendor;
        this.name = name;
        this.version = version;
    }

    /*
    Getters and setters
     */
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

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

    // overrides
    @Override
    public String toString() {
        return "Tool: "
                + (this.vendor != null ? this.vendor + " " : "")
                + this.name + "-" + this.version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppTool appTool)) return false;
        return Objects.equals(getVendor(), appTool.getVendor()) && Objects.equals(getName(), appTool.getName()) && Objects.equals(getVersion(), appTool.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVendor(), getName(), getVersion());
    }
}
