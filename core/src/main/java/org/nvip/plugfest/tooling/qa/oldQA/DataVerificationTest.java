package org.nvip.plugfest.tooling.qa.oldQA;

import jregex.Matcher;
import jregex.Pattern;
import org.nvip.plugfest.tooling.qa.oldQA.test_results.Test;
import org.nvip.plugfest.tooling.qa.oldQA.test_results.TestResults;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Set;

/**
 * <b>File</b>: DataVerificationTest.java<br>
 * <b>Description</b>: Tests a component to ensure it
 * still exists on its respective package manager
 *
 * @author Juan Francisco Patino
 */
public class DataVerificationTest extends MetricTest {

    private static final int MAX_CONNECTION_TIMEOUT = 1000;

    /**
     * Utility record to track extracted results
     *
     * @param component name of component
     * @param version version of component
     * @param publisher name of publisher
     */
    private record extractedResult(String component, String version, String publisher) {
    }

    /**
     * Constructor for DataVerificationTest
     */
    protected DataVerificationTest() {
        super("Data Verification Test");
    }

    /**
     * General test to verify component data
     *
     * @param c component to test
     * @return test results
     */
    @Override
    public TestResults test(Component c) {

        final TestResults testResults = new TestResults(c); // Init TestResults for this component

        //check if the component is missing a purl
        Set<PURL> purls = c.getPurls();
        if(purls.isEmpty()){
            testResults.addTest(new Test(false, "Component has no PURL"));
            return testResults;
        }
        //if not run the test
        for (PURL p: c.getPurls()){

            try{
                // type from https://github.com/package-url/purl-spec/blob/master/PURL-TYPES.rst
                extractedResult fromOnline;

                // Scrape website based on type
                switch (p.getType().toLowerCase()) {
                    case "apk" -> fromOnline = extractFromApk(p);
                    case "maven" -> fromOnline = extractFromMaven(p);
                    case "pypi" -> fromOnline = extractFromPyPi(p);

                    // Unknown or unsupported type
                    default -> {
                        continue;
                    }
                }

                // failed to get data
                if (fromOnline == null)
                    continue;

                // pull the data from the purl and from the package manager
                String onlineName = fromOnline.component.toLowerCase();
                String onlineVersion = fromOnline.version.toLowerCase();
                String onlinePublisher = fromOnline.publisher.toLowerCase().strip();

                String type = p.getType().toLowerCase();
                String publisher = (c.getPublisher() == null ? "" : c.getPublisher().toLowerCase() );

                // check whatever is online at least contains this component, or vice versa
                if(p.getName() == null || !((p.getName().contains(onlineName)|| onlineName.contains(p.getName()))))
                    testResults.addTest(new Test(false, "Name '", p.getName(), "' does not match '",
                            onlineName, "' in ", type));

                if(p.getVersion() == null || !onlineVersion.contains(p.getVersion()))
                    testResults.addTest(new Test(false,"Version '",p.getVersion(),"' not found in ",
                            type, " database"));

                if(!((publisher.contains(onlinePublisher)|| onlinePublisher.contains(publisher))))
                    testResults.addTest(new Test(false,"Publisher Name '", publisher,
                            "' does not match '", onlinePublisher,"' in ", type, " database"));
            }
            catch(IOException e){
                testResults.addTest(new Test(true,"Error accessing ",
                        p.getType().toLowerCase(),
                        " database\n", e.getMessage()));

            }

        }

        if(testResults.getTests().size() == 0) testResults.addTest(new Test(true,"Component was " +
                "found online"));
        return testResults;
    }
    //todo refactor following sections into a more structure for w/ classes
    ///
    /// apk
    ///

    /**
     * Extract data for APK-based packages.
     * Source: <a href="https://pkgs.alpinelinux.org/packages">...</a>
     *
     * @param p purl to use to query for info
     * @return extractedResult with component name, version, and publisher that we were able to scrape
     * @throws IOException issue with http connection
     */
    private extractedResult extractFromApk(PURL p) throws IOException {

        HttpURLConnection q = queryURL("https://pkgs.alpinelinux.org/packages?name=" + p.getName());
        htmlResult result = getHtmlResult(q); // I had the IDE do this
        String html = result.response().toString();
        result.in().close();

        // if name not found
        if(html.contains("not found"))
            return null;

        // otherwise
        String table = html.split("<tbody>")[1];
        String row = table.split("<tr>")[1]; // we just care about the top row - most recent result
        String[] columns = row.split("<td");

        String nameColumn = "";
        String publisherColumn = "";

        for (String column: columns) {

            if(column.contains("package\">"))
                nameColumn = column;

            else if(column.contains("maintainer\">")) {
                publisherColumn = column;
                break;
            }
        }
        return new extractedResult(getSpecific(nameColumn), checkVersions(table), getSpecific(publisherColumn).strip());
    }

    /**
      @param table in the form of a string
      @return all version numbers from query
     */
    private String checkVersions(String table){

        StringBuilder versions = new StringBuilder();
        String[] rows = table.split("<tr>");
        for (String row: rows
             ) {

            String[] columns = row.split("<td");
            for (String col: columns
                 ) {
                if(col.contains("version\">"))
                    versions.append(getSpecific(col)).append(", ");

            }

        }

        if(versions.toString().length() == 0) {
            return "";
        }
        else {
            return versions.substring(0, versions.toString().length() - 1);
        }


    }

    /**
        From the last HTML element we narrow down to, find what we are looking for at the top of the table
        @param column table column in the form of a string
        @return specific word at the end of the column, right before '/a>'
     */
    private String getSpecific(String column) {
        String[] elements = column.split("[<>]");
        String found = "";
        for(int i = 0; i < elements.length; i++){
            if(elements[i].equals("/a")){
                found = elements[i-1];
                break;
            }
        }
        return found;
    }

    ///
    /// maven
    ///

    /**
     * Extract data for maven based packages.
     * Source: <a href="https://mvnrepository.com/">...</a>
     *
     * @param purl purl to use to query for info
     * @return extractedResult with component name, version, and publisher that we were able to scrape
     * @throws IOException issue with http connection
     */
    private extractedResult extractFromMaven(PURL purl) throws IOException {

        // maven requires namespace
        if(purl.getNamespace() == null || purl.getNamespace().size() == 0)
            return null;

        // maven requires version
        if(purl.getVersion() == null)
            return null;

        // build namespace for request
        StringBuilder namespaceUrl = new StringBuilder();
        for(int i = 0; i < purl.getNamespace().size(); i++)
            namespaceUrl.append(purl.getNamespace().get(i).toLowerCase()).append("/");

        // Query page
        HttpURLConnection q = queryURL("https://mvnrepository.com/artifact/" +
                namespaceUrl +
                purl.getName().toLowerCase() +
                "/" + purl.getVersion());
        htmlResult result = getHtmlResult(q);   // todo returns 403 using this url
        String html = result.response().toString();
        result.in().close();

        Matcher m = new Pattern("<h2.*\"im-title\".*?>.*?>(.*?)<\\/a>.*?>(.*?)<.*<\\/h2>").matcher(html);

        // couldn't find data
        if(!m.find())
            return null;

        return new extractedResult(m.group(1), m.group(2), "PLACEHOLDER");      // todo placeholder
    }

    ///
    /// pypi
    ///
    private extractedResult extractFromPyPi(PURL purl) throws IOException {

        // Query page
        HttpURLConnection q = queryURL("https://pypi.org/project/" +
                purl.getName().toLowerCase() +
                (purl.getVersion() != null ? "/" + purl.getVersion() : ""));

        // bad response
        if(q.getResponseCode() != HttpURLConnection.HTTP_OK)
            return null;

        return new extractedResult(purl.getName(), (purl.getVersion() != null ? "/" + purl.getVersion() : ""), "PLACEHOLDER");      // todo placeholder
    }
    ///
    /// Http request section
    ///

    /**
        Adapted from queryURL() from Parser.java in BenchmarkParser
        @param urlString the URL
        @return HTTP connection
     */
    protected HttpURLConnection queryURL(String urlString) throws IOException {
        try {
            final URL url = new URL(urlString);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            connection.connect();
            return connection;
        }
        catch (SocketTimeoutException ignored) {
            throw new SocketTimeoutException("Connection timed out...");
        }
    }

    /**
     Given an http connection, return the HTML
     @param q HTML connection
     @return the HTML
     */
    private htmlResult getHtmlResult(HttpURLConnection q) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(q.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return new htmlResult(in, response);
    }

    /**
     Created by IDE for readability
     */
    private record htmlResult(BufferedReader in, StringBuffer response) {
    }

}

