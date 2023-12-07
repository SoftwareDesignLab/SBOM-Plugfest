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

import java.util.List;
import java.util.Set;

/**
 * Collection of Hash Algorithms used by Signature in CycloneDX
 *
 * @author Matt London
 */

/**
 * Represents one signature for an SBOM. The SBOM class will hold a set of these signatures
 */
public class Signature {
    public enum Algorithm {
        RS256,
        RS384,
        RS512,
        PS256,
        PS384,
        PS512,
        ES256,
        ES384,
        ES512,
        Ed25519,
        Ed448,
        HS256,
        HS384,
        HS512
    }

    /**
     * Satisfy the KTY parameter for CDX signatures
     */
    public enum KTY {
        EC,
        OKP,
        RSA
    }

    /**
     * EC curve name
     */
    public enum CRV {
        P_256,
        P_384,
        P_521
    }
    /**
     * Algorithm used to sign the SBOM
     */
    private final Algorithm algorithm;

    /**
     * Application specific id for signature
     */
    private final String keyId;

    /**
     * Public key parameter
     */
    private final KTY kty;
    private final CRV crv;
    private final String x;
    private final String y;

    /**
     * Certificate path
     * Sorted array of X.509 [RFC5280] certificates
     */
    private final List<String> certificatePath;

    /**
     * Excludes, names of any properties that are excluded from this signature
     */
    private final Set<String> excludes;

    /**
     * Signature data, must follow JWA [RFC7518] format
     */
    private final String value;

    public Signature(Algorithm algorithm, String keyId, KTY kty, CRV crv, String x, String y,
                     List<String> certificatePath, Set<String> excludes, String value) {
        this.algorithm = algorithm;
        this.keyId = keyId;
        this.kty = kty;
        this.crv = crv;
        this.x = x;
        this.y = y;
        this.certificatePath = certificatePath;
        this.excludes = excludes;
        this.value = value;
    }

    ///
    /// Getters and Setters
    ///

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String getKeyId() {
        return keyId;
    }

    public KTY getKty() {
        return kty;
    }

    public CRV getCRV() {
        return crv;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public List<String> getCertificatePath() {
        return certificatePath;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    public String getValue() {
        return value;
    }
}
