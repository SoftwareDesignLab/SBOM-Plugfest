package org.nvip.plugfest.tooling.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * A static class containing helpful utilities for API calls and testing responses.
 *
 * @author Juan Francisco Patino
 */
public class Utils {


    /**
     * Utility Class for sending SBOM JSON objects
     */
    public static class SBOMFile {
        @JsonProperty
        public String fileName;
        @JsonProperty
        public String contents;
        public boolean hasNullProperties;
        public SBOMFile(String fileName, String contents) {
            this.fileName = fileName; this.contents = contents;
            hasNullProperties = fileName == null || contents == null
                    || fileName.length() == 0 || contents.length() == 0;
        }
    }

    public static SBOMFile[] configSbomFileArr(ArrayList<SBOMFile> sboms) {
        SBOMFile[] arr = new SBOMFile[sboms.size()];
        for(int i = 0; i < sboms.size(); i++){
            arr[i] = sboms.get(i);
        }
        return arr;
    }
    // todo docstrings ^ v
    public static int sbomFileArrNullCheck(SBOMFile[] arr){
        int i = 0;
        for (SBOMFile a: arr
             ) {
            if(a == null || a.hasNullProperties)
                return i;
            i++;
        }
        return -1;
    }

}
