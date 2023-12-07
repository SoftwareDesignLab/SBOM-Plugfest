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

package org.nvip.plugfest.tooling.differ.conflicts;

/**
 * Enum to represent the type of conflict between two components
 *
 * @author Matt London
 */
public enum ComponentConflictType {
    /** Component only found in one SBOM */
    COMPONENT_NOT_FOUND,
    /** Component found in both SBOMs, but has different versions */
    COMPONENT_VERSION_MISMATCH,
    /** Component found in both SBOMs, but has different licenses */
    COMPONENT_LICENSE_MISMATCH,
    /** Component found in both SBOMs, but has different publisher */
    COMPONENT_PUBLISHER_MISMATCH,
    /** Component found in both SBOMs, but has different name
     * NOTE: This should never occur because we compare components by name */
    COMPONENT_NAME_MISMATCH,
    /** Component found in both SBOMs, but has different CPE */
    COMPONENT_CPE_MISMATCH,
    /** Component found in both SBOMs, but has different PURL */
    COMPONENT_PURL_MISMATCH,
    /** Component found in both SBOMs, but has different SWID */
    COMPONENT_SWID_MISMATCH,
    /** Component found in both SBOMs, but has different SPDX ID */
    COMPONENT_SPDXID_MISMATCH,
    /** Component found in both SBOMs, but has different Hashes */
    COMPONENT_HASH_MISMATCH,
    /** Component found in both SBOMs, conflict was called, but cannot be determined */
    COMPONENT_UNKNOWN_MISMATCH
}
