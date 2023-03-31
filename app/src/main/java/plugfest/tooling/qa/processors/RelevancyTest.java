package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

/**
 * File: RelevancyTest.java
 * Tests a component to ensure that its name is most likely relevant to its functionality
 *
 * @author Juan Francisco Patino
 */

public class RelevancyTest extends MetricTest {

    final static String[] mostCommonBiGrams = {"th", "he", "in", "en", "nt", "re", "er", "an", "ti", "es", "on", "at", "se",
            "nd", "or", "ar", "al", "te", "co", "de", "to", "ra", "et", "ed", "it", "sa", "em", "ro"};

    final static String[] mostCommonTriGrams = {"the", "and", "tha", "ent", "ing", "ion", "tio", "for", "nde", "has", "nce",
            "edt", "tis", "oft", "sth", "men"};

    final static String[] biGramsThatNeverOccur = {"bx", "cj", "cv", "cx", "dx", "fq", "fx", "gq", "gx", "hx", "jc", "jf",
            "jg", "jq", "jv", "jx","jz", "kx", "mx", "px", "qb", "qc", "qd", "qf", "qg", "qj", "qk", "ql", "qm", "qn",
            "qp", "qt", "qv", "qx", "qy", "qz", "sx", "vb", "vf", "vj", "vm", "vp", "vq", "vt", "vw", "vx", "wx", "xj",
            "xx", "zx"};

    protected RelevancyTest() {
        super("Relevancy Test");
    }

    @Override
    public TestResults test(Component c){
        // Init StringBuilder
        TestResults testResults = new TestResults(c);

        // Check if name makes sense / is english (looking)

        // Ensure BOTH component name and publisher name satisfy at least one of the following:
        //      Contains a "most common bigram"
        //      Contains a "most common trigram"
        //      Does not contain a "bigram that never occurs"

        // Test Component Name
        testResults.addTest(testComponentName(c));

        // Test Publisher Name
        testResults.addTest(testPublisherName(c));

        // Return result
        return testResults;
    }

    private Test testComponentName(Component c) {
        if(c.getName() != null &&
                !testNames(c.getName().strip().toLowerCase())) {
            return new Test(false, "Component Name is Likely Not Relevant");
        }
        return new Test(true, "Component Name is Likely Relevant");
    }

    private Test testPublisherName(Component c) {
        if(c.getPublisher() != null &&
                !testNames(c.getPublisher().strip().toLowerCase())) {
            return new Test(false, "Publisher Name is Likely Not Relevant");
        }
        return new Test(true, "Publisher Name is Likely Relevant");
    }

    // Test to perform on Component Name and Publisher Name
    private static boolean testNames(String name){
        return checkEnglish(name, mostCommonBiGrams, true) ||
                checkEnglish(name, mostCommonTriGrams, true) ||
                checkEnglish(name, biGramsThatNeverOccur, false);
    }

    // Iterate through array of characters depending on what we want
    private static boolean checkEnglish(String nameToLowerStripped, String[] array, boolean checkHas) {
        for (String b: array
             ) {

            if(nameToLowerStripped.contains(b)){
                return checkHas;
            }

        }
        return !checkHas;
    }
}