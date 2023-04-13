package org.nvip.plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.UniqueIdOccurrence;
import org.nvip.plugfest.tooling.differ.UniqueIdentifierType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UniqueIdOccurenceTest {

    public final static Set<Integer> testAppearances = new HashSet<>(Arrays.asList(5,2,4,5));
    public final UniqueIdOccurrence u1 = new UniqueIdOccurrence("Test", UniqueIdentifierType.CPE);
    public final UniqueIdOccurrence u2 = new UniqueIdOccurrence("Test2", UniqueIdentifierType.PURL);

    @Test
    public void uidoVersionEqualsTest() {
        UniqueIdOccurrence u1Copy = new UniqueIdOccurrence("Test", UniqueIdentifierType.CPE);
        assertEquals(u1, u1Copy);
    }
    @Test
    public void uidoVersionNotEqualsTest(){
        assertNotEquals(u1, u2);
    }
    @Test
    public void appearancesTest(){

        u1.addAppearance(5);
        u1.addAppearance(2);
        u1.addAppearance(4);
        u1.addAppearance(5);
        assertEquals(u1.getAppearances(), testAppearances);

    }

}
