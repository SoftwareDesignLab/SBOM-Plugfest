/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package plugfest.tooling;

import java.io.IOException;
import java.util.*;
import plugfest.tooling.differ.*;
import plugfest.tooling.sbom.*;


public class App {

    public static void main(String[] args) throws IOException {
        
        if(args.length != 2) {
            System.out.println("(Only) 2 files required");
            System.exit(0);
        }

        // QA Pipeline code here
        //
        // if ( we have a file ) : then check schema
        //      if ( file is invalid schema ) : dont continue
        //      else : continue
        //
        // End QA Pipeline

        SBOM sbom_one = SPDXParser.parse(args[0]);
        SBOM sbom_two = SPDXParser.parse(args[1]);

        FullDiff fd = new FullDiff(sbom_one,  sbom_two);
        fd.diff().print();
        
    }
}
