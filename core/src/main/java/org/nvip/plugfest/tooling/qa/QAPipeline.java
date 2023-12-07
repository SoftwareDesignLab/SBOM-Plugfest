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

package org.nvip.plugfest.tooling.qa;

import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.Set;

/**
 * Pipeline that will run all tests against a given SBOM
 *
 * @author Dylan Mulligan
 * @author Matt London
 * @author Derek Garcia
 */
public class QAPipeline {

    /**
     * Run a given sbom against all processor tests within this pipeline
     *
     * @param uid Unique filename used to ID the SBOM
     * @param sbom SBOM to run tests against
     * @param processors Collection of Processors to run against SBOM
     * @return QualityReport containing all results
     */
    public static QualityReport process(String uid, SBOM sbom, Set<AttributeProcessor> processors){
         // Init QualityReport
         QualityReport qr = new QualityReport(uid);

         // Run all added processors
         for (AttributeProcessor p : processors)
             qr.updateAttribute(p.getAttributeName(), p.process(sbom));

         // Return Master QR
         return qr;
    }
}
