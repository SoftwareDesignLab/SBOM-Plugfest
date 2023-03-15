package plugfest.tooling.metrics;

/**
 * Imports SPDX Tools
 */
import org.spdx.tools.CompareSpdxDocs;
import org.spdx.tools.GenerateVerificationCode;
import org.spdx.tools.InvalidFileNameException;
import org.spdx.tools.MatchingStandardLicenses;
import org.spdx.tools.OnlineToolException;
import org.spdx.tools.RdfSchemaToJsonContext;
import org.spdx.tools.RdfSchemaToJsonSchema;
import org.spdx.tools.RdfSchemaToXsd;
import org.spdx.tools.SpdxConverter;
import org.spdx.tools.SpdxConverterException;
import org.spdx.tools.SpdxToolsHelper;
import org.spdx.tools.SpdxVerificationException;
import org.spdx.tools.SpdxViewer;
import org.spdx.tools.Verify;

/**
 * Imports SPDX Schema Tools
 */
import org.spdx.tools.schema.AbstractOwlRdfConverter;
import org.spdx.tools.schema.OwlToJsonContext;
import org.spdx.tools.schema.OwlToJsonSchema;
import org.spdx.tools.schema.OwlToXsd;
import org.spdx.tools.schema.SchemaException;

import com.google.protobuf.Timestamp;

/**
 * Imports SPDX Compare Tools
 */
import org.spdx.tools.compare.AbstractFileCompareSheet;
import org.spdx.tools.compare.AbstractSheet;
import org.spdx.tools.compare.CompareHelper;
import org.spdx.tools.compare.CreatorSheet;
import org.spdx.tools.compare.DocumentAnnotationSheet;
import org.spdx.tools.compare.DocumentRelationshipSheet;
import org.spdx.tools.compare.DocumentSheet;
import org.spdx.tools.compare.ExternalReferencesSheet;
import org.spdx.tools.compare.ExtractedLicenseSheet;
import org.spdx.tools.compare.FileAnnotationSheet;
import org.spdx.tools.compare.FileAttributionSheet;
import org.spdx.tools.compare.FileChecksumSheet;
import org.spdx.tools.compare.FileCommentSheet;
import org.spdx.tools.compare.FileConcludedSheet;
import org.spdx.tools.compare.FileContributorsSheet;
import org.spdx.tools.compare.FileCopyrightSheet;
import org.spdx.tools.compare.FileLicenseCommentsSheet;
import org.spdx.tools.compare.FileLicenseInfoSheet;
import org.spdx.tools.compare.FileNoticeSheet;
import org.spdx.tools.compare.FileRelationshipSheet;
import org.spdx.tools.compare.FileSpdxIdSheet;
import org.spdx.tools.compare.FileTypeSheet;
import org.spdx.tools.compare.MultiDocumentSpreadsheet;
import org.spdx.tools.compare.NormalizedFileNameComparator;
import org.spdx.tools.compare.PackageSheet;
import org.spdx.tools.compare.SnippetSheet;
import org.spdx.tools.compare.VerificationSheet;

/**
 * Imports Native Java Libraries
 */
import java.util.Date;

/**
 * Class for Verifying SPDX Files
 */
public class Metrics {
    
    private String filepath;

    public Metrics(String _filepath) {
        this.filepath = _filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    public void compare(String sbom1, String sbom2) {
        System.out.println("Running Comparison on SBOM Files: "+sbom1+" , "+sbom2);
        Date date = new Date();
        long timestamp = date.getTime();
        String sbom_path1 = (this.filepath+"/"+sbom1);
        String sbom_path2 = (this.filepath+"/"+sbom2);
        String outputFile = "comparison_"+String.valueOf(timestamp)+".xlsx";
        //System.out.println("SBOM File#1: "+sbom_path1);
        //System.out.println("SBOM File#2: "+sbom_path2);
        //System.out.println("Output File: "+outputFile);
        String[] compareArgs = {outputFile, sbom_path1, sbom_path2};
        CompareSpdxDocs.main(compareArgs);
    }

    public void verify(String sbom){
        System.out.println("Running Verification on SBOM File: "+sbom);
        String sbom_path = (this.filepath+"/"+sbom);
        String[] sboms = {sbom_path};
        Verify.main(sboms);
    }
}
