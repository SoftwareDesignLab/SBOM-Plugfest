package org.nvip.plugfest.tooling;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.HashMap;

public class sbomDAO {
    private static HashMap<String, SBOM> cache;

    public sbomDAO() {
        cache = new HashMap<String, SBOM>();
    }

    public SBOM getSBOM(String path) {
        if(cache.containsKey(path)) {
            return cache.get(path);
        }

        //figure out what type of file it is
        //translate it to internal SBOM

        //cache it

        //return it
        return null;
    }
}
