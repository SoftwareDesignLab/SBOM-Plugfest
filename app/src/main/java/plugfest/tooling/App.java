/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package plugfest.tooling;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.cyclonedx.CycloneDxSchema;

import plugfest.tooling.differ.*;
import plugfest.tooling.metrics.*;
import plugfest.tooling.sbom.*;


public class App {

    private static final int MIN_ARGS = 2;

    private static final int MAX_ARGS = 3;

    /**
     * FileName: App.java
     * Receives a command and one to two files.
     * Depending on the command, the app will do one of the following:
     *  -q : A quality check (for one file only)
     *  -m : A metrics run (for one file only))
     *  -d : A diff comparison (for two files only)
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        if(args.length < MIN_ARGS) {
            System.out.println(
                    "Too few arguments. Requires: a common, and one to two files.\n" +
                            "Example: tool.java -q users/admin/files/1.xml users/admin/files/2.xml");
            System.exit(0);
        } else if (args.length > MAX_ARGS) {
            System.out.println(
                    "Too many arguments. Requires: a common, and one to two files.\n" +
                            "Example: tool.java -q users/admin/files/1.xml users/admin/files/2.xml");
            System.exit(0);
        }

        if(!new File(args[1]).exists()) {
            System.out.println("Second file can not be found. Exiting...");
            System.exit(0);
        }



        /**
        if(args[0].contains("-q")) {
            // QA Pipeline code here
            //
            // if ( we have a file ) : then check schema
            //      if ( file is invalid schema ) : dont continue
            //      else : continue
            //
            // End QA Pipeline
        } else if (args[0].contains("-m")) {
            Metrics metrics = new Metrics(args[1]);
            metrics.verify(args[1]);

        } else if (args[0].contains("-d")) {
            if(!new File(args[2]).exists()) {
                System.out.println("Second file can not be found. Exiting...");
                System.exit(0);
            }

            SBOM sbom_one = SPDXParser.parse(args[1]);
            SBOM sbom_two = SPDXParser.parse(args[2]);

            FullDiff fd = new FullDiff(sbom_one, sbom_two);
            fd.diff().print();
        } else {
            System.out.println("Invalid command given. Should be '-q' for quality check, '-m' for metrics, or 'd' for diff.");
        }
        */

        
    }
}
