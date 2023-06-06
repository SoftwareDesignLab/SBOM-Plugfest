package org.nvip.plugfest.tooling.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.Debug;
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

        public boolean hasNullOrEmptyProperties() {
            return fileName == null || contents == null || fileName.length() == 0 || contents.length() == 0;
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
        int i = 0;
        for (SBOMFile a: arr
             ) {
            if(a == null || a.hasNullOrEmptyProperties())
                return i;
            i++;
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
