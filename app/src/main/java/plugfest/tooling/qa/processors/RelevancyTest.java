package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

/**
 * File: RelevancyTest.java
 * Tests a component to ensure that its name is most likely relevant to its functionality
 *
 * @author Juan Francisco Patino
 */

public class RelevancyTest extends MetricTest {

    final String[] mostCommonBiGrams = {"th", "he", "in", "en", "nt", "re", "er", "an", "ti", "es", "on", "at", "se",
            "nd", "or", "ar", "al", "te", "co", "de", "to", "ra", "et", "ed", "it", "sa", "em", "ro"};

    final String[] mostCommonTriGrams = {"the", "and", "tha", "ent", "ing", "ion", "tio", "for", "nde", "has", "nce",
            "edt", "tis", "oft", "sth", "men"};

    final String[] biGramsThatNeverOccur = {"bx", "cj", "cv", "cx", "dx", "fq", "fx", "gq", "gx", "hx", "jc", "jf",
            "jg", "jq", "jv", "jx","jz", "kx", "mx", "px", "qb", "qc", "qd", "qf", "qg", "qj", "qk", "ql", "qm", "qn",
            "qp", "qt", "qv", "qx", "qy", "qz", "sx", "vb", "vf", "vj", "vm", "vp", "vq", "vt", "vw", "vx", "wx", "xj",
            "xx", "zx"};

    protected RelevancyTest() {
        super("Relevancy Test");
    }

    @Override
    public ArrayList<String> test(Component c){
        // Init StringBuilder
        final ArrayList<String> testResults = new ArrayList<>();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        // Check if name makes sense / is english (looking)
        String nameToLowerStripped = c.getName().strip().toLowerCase();
        String publisherToLowerStripped = c.getPublisher().strip().toLowerCase();

        // Ensure BOTH component name and publisher name satisfy at least one of the following:
        //      Contains a "most common bigram"
        //      Contains a "most common trigram"
        //      Does not contain a "bigram that never occurs"

        if(!test(nameToLowerStripped))
            testResults.add(String.format("FAILED: Component %s Name is likely not relevant", UUIDShort));

        if(!test(publisherToLowerStripped)){
            if(!testResults.isEmpty()) testResults.add("\n");
            testResults.add(String.format("FAILED: Component %s Publisher Name is likely not relevant", UUIDShort));
        }

        // If no checks failed, mark test as passed
        if(testResults.isEmpty()) testResults.add("PASSED");

        // Return result
        return testResults;
    }

    // Test to perform on Component Name and Publisher Name
    private boolean test(String name){
        return checkEnglish(name, mostCommonBiGrams, true) ||
                checkEnglish(name, mostCommonTriGrams, true) ||
                checkEnglish(name, biGramsThatNeverOccur, false);
    }

    // Iterate through array of characters depending on what we want
    private boolean checkEnglish(String nameToLowerStripped, String[] array, boolean checkHas) {
        for (String b: array
             ) {

            if(nameToLowerStripped.contains(b)){
                return checkHas;
            }

        }
        return !checkHas;
    }
}