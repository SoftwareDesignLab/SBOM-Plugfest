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
public class SPDXMetrics {
    
    private String filepath;

    public SPDXMetrics(String _filepath) {
        this.filepath = _filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String _filepath) {
        this.filepath = _filepath;
    }

    public void compare(String[] sbom_files) {
        System.out.println("Running Comparison on SBOM Files: "+sbom_files);
        Date date = new Date();
        long timestamp = date.getTime();
        String[] compareArgs = new String[(sbom_files.length+1)];
        String outputFile = "comparison_"+String.valueOf(timestamp)+".xlsx";
        compareArgs[0] = outputFile;
        for(int i = 0; i < sbom_files.length; i++) {
            compareArgs[i+1] = this.filepath+"/"+sbom_files[i];
        }

        CompareSpdxDocs.main(compareArgs);
    }

    public void verify(String sbom){
        System.out.println("Running Verification on SPDX SBOM File: "+sbom);
        String sbom_file = (this.filepath+"/"+sbom);
        String[] sboms = { sbom_file };
        Verify.main(sboms);
    }
}
