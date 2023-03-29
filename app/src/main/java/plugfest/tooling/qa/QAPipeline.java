package plugfest.tooling.qa;

import plugfest.tooling.sbom.SBOM;

public class QAPipeline {



    public QAPipeline(){

    }


    public void process(SBOM sbom){
        sbom.getTimestamp();
    }
}
