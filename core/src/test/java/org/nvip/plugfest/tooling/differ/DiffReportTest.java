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

package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.DependencyTree;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.PURL;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * file: DiffReportTest.java
 *
 * @author Derek Garcia
 */
public class DiffReportTest {


    /**
     * Generate a template SBOM
     *
     * @return Template SBOM
     */
    private SBOM generateTemplateSBOM() {
        SBOM s = new SBOM(SBOM.Type.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
        try{
            Component test_component_a = new Component(
                    "red", "red_publisher", "1.1.0",
                    Set.of("cpe2.3::test_red_cpe"), Set.of("pkg:redpackage/red@1.1.0"), Set.of("random_red_swid")
            );

            Component test_component_b = new Component(
                    "blue", "blue_publisher", "1.1.0",
                    Set.of("cpe2.3::test_blue_cpe"), Set.of("pkg:bluepackage/blue@1.1.0"), Set.of("random_blue_swid")
            );

            HashSet<String> licences = new HashSet<>();
            licences.add("License_1");
            test_component_a.setLicenses(licences);

            s.addComponent(null, test_component_a);
            s.addComponent(test_component_a.getUUID(), test_component_b);

        } catch (Exception e){
            fail("Failed to generate SBOM");
        }

        return s;
    }

    ///
    /// Constructor
    ///

    @Test
    @DisplayName("DiffReport Constructor")
    public void test_diffreport_constructor(){
        DiffReport d = new DiffReport("TEST_DIFF", generateTemplateSBOM());
        assertNotNull(d);
    }

    ///
    /// SBOM Comparison
    ///

    @Test
    @DisplayName("Compare same SBOM, different UID")
    public void test_compare_same_sbom_diff_uid(){
        DiffReport d = new DiffReport("TEST_DIFF", generateTemplateSBOM());
        d.compare("OTHER_UID", generateTemplateSBOM());
        assertNotNull(d);
    }

    @Test
    @DisplayName("Compare same SBOM, same UID")
    public void test_compare_same_same_uid(){
        DiffReport d = new DiffReport("TEST", generateTemplateSBOM());
        d.compare("TEST", generateTemplateSBOM());
        assertNotNull(d);
    }

    @Test
    @DisplayName("Compare 3 SBOMs")
    public void test_compare_three_same_sboms(){
        DiffReport d = new DiffReport("ONE", generateTemplateSBOM());
        d.compare("TWO", generateTemplateSBOM());
        d.compare("THREE", generateTemplateSBOM());
        assertNotNull(d);
    }

    ///
    /// SBOM Metadata Comparison
    ///
    @Test
    @DisplayName("Compare SBOM Conflicts")
    public void test_compare_sbom_conflicts(){
        DiffReport d = new DiffReport("ONE", generateTemplateSBOM());

        // Build other SBOM with random metadata details
        SBOM s = new SBOM(SBOM.Type.Other, "7", "2", "Foo",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617a54", "2002-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
        try{
            Component test_component_a = new Component(
                    "red", "red_publisher", "1.1.0",
                    Set.of("cpe2.3::test_red_cpe"), Set.of("pkg:redpackage/red@1.1.0"), Set.of("random_red_swid")
            );

            Component test_component_b = new Component(
                    "blue", "blue_publisher", "1.1.0",
                    Set.of("cpe2.3::test_blue_cpe"), Set.of("pkg:bluepackage/blue@1.1.0"), Set.of("random_blue_swid")
            );

            HashSet<String> licences = new HashSet<>();
            licences.add("License_1");
            test_component_a.setLicenses(licences);

            s.addComponent(null, test_component_a);
            s.addComponent(test_component_a.getUUID(), test_component_b);

        } catch (Exception e){
            fail("Failed to generate other SBOM");
        }

        d.compare("OTHER", s);
        assertNotNull(d);
    }

    ///
    /// Component Comparison
    ///

    @Test
    @DisplayName("Compare No Components")
    public void test_compare_no_components(){
        DiffReport d = new DiffReport("ONE", generateTemplateSBOM());

        // Empty SBOM
        SBOM s = new SBOM(SBOM.Type.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        d.compare("OTHER", s);
        assertNotNull(d);
    }

    @Test
    @DisplayName("Compare Extra Component")
    public void test_compare_extra_components(){
        DiffReport d = new DiffReport("ONE", generateTemplateSBOM());

        // Generate template and add extra component
        SBOM s = generateTemplateSBOM();
        try{
            Component test_component_a = new Component(
                    "yellow", "yellow_publisher", "1.2.3",
                    Set.of("cpe2.3::test_yellow_cpe"), Set.of("pkg:yellowpackage/blue@1.2.3"), Set.of("random_yellow_swid")
            );

            s.addComponent(null, test_component_a);

        } catch (Exception e){
            fail("Failed to generate SBOM");
        }

        d.compare("OTHER", s);
        assertNotNull(d);
    }

    // todo test name conflict, spdxid conflict, unknown
    @Test
    @DisplayName("Compare Component Conflicts")
    public void test_compare_component_conflicts(){

        DiffReport d = new DiffReport("ONE", generateTemplateSBOM());

        // make new SBOM with modified component
        SBOM s = new SBOM(SBOM.Type.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
        try{
            Component test_component_a = new Component(
                    "red", "other", "1.5.0",
                    Set.of("cpe2.3::other_red_cpe"), Set.of("pkg:other/red@1.5.0"), Set.of("rand_red_swid")
            );

            HashSet<String> licences = new HashSet<>();
            licences.add("License_2");
            test_component_a.setLicenses(licences);

            s.addComponent(null, test_component_a);

        } catch (Exception e){
            fail("Failed to generate other SBOM");
        }

        d.compare("OTHER", s);
        assertNotNull(d);
    }
}
