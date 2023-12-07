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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * File: UniqueIdOccurrenceTest.java
 * Tests for UniqueIdOccurrence
 *
 * @author Juan Patino
 */
public class UniqueIdOccurrenceTest {

    public final static Set<Integer> testAppearances = new HashSet<>(Arrays.asList(5,2,4,5));
    public final UniqueIdOccurrence u1 = new UniqueIdOccurrence("Test", UniqueIdentifierType.CPE);
    public final UniqueIdOccurrence u1Copy = new UniqueIdOccurrence("Test", UniqueIdentifierType.CPE);
    public final UniqueIdOccurrence u2 = new UniqueIdOccurrence("Test2", UniqueIdentifierType.PURL);
    public final UniqueIdOccurrence u3 = new UniqueIdOccurrence("Test3", UniqueIdentifierType.SWID);

    @Test
    public void uidoEqualsTest() {
        u1.addAppearance(1);
        u1Copy.addAppearance(1);
        assertEquals(u1, u1Copy);
    }
    @Test
    public void uidoNotEqualsTest(){
        u1Copy.addAppearance(1);
        assertNotEquals(u1, u1Copy);
        assertNotEquals(u1, u2);
        assertNotEquals(u1, u3);
    }
    @Test
    public void uidoAppearancesTest(){
        u1.addAppearance(5);
        u1.addAppearance(2);
        u1.addAppearance(4);
        u1.addAppearance(5);
        assertEquals(u1.getAppearances(), testAppearances);
    }

}
