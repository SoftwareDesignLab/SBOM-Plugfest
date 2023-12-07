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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * File: SignatureTest.java
 * Tests for Signature
 *
 * @author Tyler Drake
 */
public class SignatureTest {

    /**
     * Test signatures
     */
    Signature test_signature;


    /**
     * Basic test signature items: Set 1
     */
    Signature.Algorithm test_signature_algorithm = Signature.Algorithm.ES256;

    String keyId = "test_key_id";

    Signature.KTY test_signature_kty = Signature.KTY.EC;

    Signature.CRV test_signature_crv = Signature.CRV.P_256;

    String test_x = "5";

    String test_y = "10";

    List<String> test_certificates = Arrays.asList("test1", "test2", "test3");

    Set<String> test_excludes = new HashSet<String>() {
        {
            add("one");
            add("two");
            add("three");
        }
    };

    String test_value = "example_data";


    /**
     * Set-up/Teardown methods
     */
    @BeforeEach
    public void create_signature() {
        test_signature = new Signature(
                test_signature_algorithm, keyId, test_signature_kty, test_signature_crv,
                test_x, test_y, test_certificates, test_excludes, test_value
        );
    }

    @AfterEach
    public void delete_signature() {
        test_signature = null;
    }


    /**
     * Tests
     */

    @Test
    public void getAlgorithm_test() {
        assertEquals(Signature.Algorithm.ES256, test_signature.getAlgorithm());
    }

    @Test
    public void getKeyId_test() {
        assertEquals("test_key_id", test_signature.getKeyId());
    }

    @Test
    public void getKty_test() {
        assertEquals(Signature.KTY.EC, test_signature.getKty());
    }

    @Test
    public void getCRV_test() {
        assertEquals(Signature.CRV.P_256, test_signature.getCRV());
    }

    @Test
    public void getX_test() {
        assertEquals("5", test_signature.getX());
    }

    @Test
    public void getY_test() {
        assertEquals("10", test_signature.getY());
    }

    @Test
    public void getCertificatePath_test() {
        assertEquals(3, test_signature.getCertificatePath().size());
        assertEquals("test1", test_signature.getCertificatePath().get(0));
        assertEquals("test2", test_signature.getCertificatePath().get(1));
        assertEquals("test3", test_signature.getCertificatePath().get(2));
    }

    @Test
    public void getExcludes_test() {
        assertEquals(3, test_signature.getExcludes().size());
    }

    @Test
    public void getValue_test() {
        assertEquals("example_data", test_signature.getValue());
    }
}