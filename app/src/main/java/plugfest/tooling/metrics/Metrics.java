package plugfest.tooling.metrics;

// Imports will be placed here

/**
 * Class for General SBOM Metrics
 */
public class Metrics {
    
    private String componentName;
    private double version;
    private String packageURL;
    private String[] dependencyNames;

    // If we are having a hash value as a property, we should determine what Hash schema/source we are using.
    //private Hash componentHash;
    
    private String lifecyclePhase;
    private String license;
    private String authSignature;
    private String integritySignature;
    private String vex;

    public Metrics() {

    }
}
