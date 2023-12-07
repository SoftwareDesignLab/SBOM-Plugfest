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

package org.nvip.plugfest.tooling.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * A static class containing helpful utilities for API calls and testing responses.
 *
 * @author Juan Francisco Patino
 * @author Ian Dunn
 */
public class Utils {

    /**
     * Utility Class for sending SBOM JSON objects. Contains {@code fileName} & {@code contents} fields that are capable
     * of being automatically serialized to and from JSON.
     */
    public static class SBOMFile {
        @JsonProperty
        public String fileName;
        @JsonProperty
        public String contents;

        public SBOMFile() {
            this(null, null);
        }

        /**
         * Default constructor for SBOMFile. Used for test purposes.
         *
         * @param fileName The name of the SBOM file.
         * @param contents The contents of the SBOM file.
         */
        public SBOMFile(String fileName, String contents) {
            this.fileName = fileName;
            this.contents = contents;
        }

        public boolean hasNullProperties() {
            return fileName == null || contents == null;
        }
    }

    /**
     * Checks an array of {@code SBOMFile} objects for one containing any null properties.
     *
     * @param arr The array of {@code SBOMFile} objects to check.
     * @return -1 if no SBOMFiles have null properties. Otherwise, return the index of the first SBOM with null
     * properties.
     */
    public static int sbomFileArrNullCheck(SBOMFile[] arr){
        for (int i = 0; i < arr.length; i++) {
            SBOMFile file = arr[i];
            if(file == null || file.hasNullProperties())
                return i;
        }
        return -1;
    }

    /**
     * Encodes any object into a ResponseEntity of that same type.
     *
     * @param object The object to encode.
     * @return An encoded {@code ResponseEntity} with the object and a status code of {@code HttpStatus.OK}. If there
     *         is an error encoding, an empty ResponseEntity will be returned with an
     *         {@code HttpStatus.INTERNAL_SERVER_ERROR} code.
     */
    public static <T> ResponseEntity<T> encodeResponse(T object) {
        try {
            return new ResponseEntity<>(object, HttpStatus.OK);
        } catch (Exception e) {
            Debug.log(Debug.LOG_TYPE.EXCEPTION, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
