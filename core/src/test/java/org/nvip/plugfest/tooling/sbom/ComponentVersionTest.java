package org.nvip.plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.ComponentVersion;
import org.nvip.plugfest.tooling.differ.UniqueIdOccurrence;
import org.nvip.plugfest.tooling.differ.UniqueIdentifierType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentVersionTest {

    public final static ComponentVersion cv1 = new ComponentVersion("Test", "v1");
    public final static ComponentVersion cv2 = new ComponentVersion("Test", "v2");
    public final static Set<Integer> testAppearances = new HashSet<>(Arrays.asList(5,2,4,5));
    public final static Set<UniqueIdOccurrence> testUIDOs =
            new HashSet<>(List.of(new UniqueIdOccurrence("cpe", UniqueIdentifierType.CPE),
                    new UniqueIdOccurrence("purl", UniqueIdentifierType.PURL),
                        new UniqueIdOccurrence("swid", UniqueIdentifierType.SWID)));

    @Test
    public void componentVersionEqualsTest() {
        ComponentVersion cv1Copy = new ComponentVersion("Test", "v1");
        assertEquals(cv1, cv1Copy);
    }
    @Test
    public void componentVersionNotEqualsTest(){
        assertNotEquals(cv1, cv2);
    }
    @Test
    public void setTestAppearances(){

        cv1.addAppearance(5);
        cv1.addAppearance(2);
        cv1.addAppearance(4);
        cv1.addAppearance(5);

        cv2.setAppearances(testAppearances);

        assertEquals(cv1.getAppearances(), cv2.getAppearances());

    }


}
