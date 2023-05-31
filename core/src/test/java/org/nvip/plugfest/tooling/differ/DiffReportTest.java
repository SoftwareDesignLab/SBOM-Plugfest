package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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


    private SBOM generateTemplateSBOM() {
        SBOM s = new SBOM(SBOM.Type.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
        try{
            Component test_component_a = new Component(
                    "red", "red_publisher", "1.1.0",
                    Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
            );

            Component test_component_b = new Component(
                    "blue", "blue_publisher", "1.1.0",
                    Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
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
                    Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
            );

            Component test_component_b = new Component(
                    "blue", "blue_publisher", "1.1.0",
                    Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
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
                    Set.of("cpe2.3::test_yellow_cpe"), Set.of(new PURL("pkg:yellowpackage/blue@1.2.3")), Set.of("random_yellow_swid")
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
                    Set.of("cpe2.3::other_red_cpe"), Set.of(new PURL("pkg:other/red@1.5.0")), Set.of("rand_red_swid")
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
