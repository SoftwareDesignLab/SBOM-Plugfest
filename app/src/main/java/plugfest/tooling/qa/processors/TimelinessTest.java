package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Set;

/**
 * File: TimelinessTest.java
 * Tests a component to ensure that it is up to date
 *
 * @author Juan Francisco Patino
 */

public class TimelinessTest extends MetricTest {

    private static final int MAX_CONNECTION_TIMEOUT = 1000;
    protected TimelinessTest() {
        super("TimelinessTest Test");
    }

    @Override
    public ArrayList<String> test(Component c) {

        final ArrayList<String> testResults = new ArrayList<>();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        Set<String> purl = c.getPURL();
        if(purl.isEmpty()){
            testResults.add(String.format("FAILED: Component %s has no PURL", UUIDShort));
            return testResults;
        }

        try{
            String[] fromPURL = extractedFromPURL(purl);
            String name = c.getName().toLowerCase();
            String nameFromPURL = fromPURL[0].toLowerCase();
            String version = c.getVersion().toLowerCase();
            String versionFromPURL = fromPURL[1].toLowerCase();
            String publisher = c.getPublisher().toLowerCase();
            String publisherFromPURL = fromPURL[2].toLowerCase().strip();

            // check whatever is online at least contains this component, or vice versa
            if(!((name.contains(nameFromPURL)|| nameFromPURL.contains(name))))
                testResults.add(String.format("FAILED: Component %s Name is not up to date", UUIDShort));

            if(!((version.contains(versionFromPURL)|| versionFromPURL.contains(version))))
                testResults.add(String.format("FAILED: Component %s can be updated from %s to %s", UUIDShort, version, versionFromPURL));

            if(!((publisher.contains(publisherFromPURL)|| publisherFromPURL.contains(publisher))))
                testResults.add(String.format("FAILED: Component %s Publisher Name is not up to date", UUIDShort));
        }
        catch(IOException e){
            testResults.add(String.format("FAILED: Error in testing component %s:\n%s", UUIDShort, e.getMessage()));

        }
        if(testResults.isEmpty()) testResults.add("PASSED");
        return testResults;// todo refactor with Ian's testResult class
    }

    //extract name, version, and publisher from online
    private static String[] extractedFromPURL(Set<String> purl) throws IOException {
        String p = purl.toArray()[0].toString();
        if(p.contains("alpine")){
            return extractFromAlpine(p);
        }
        return extractFromDebian(p); //todo: we don't test for this yet
    }

    //extract name, version, and publisher from Alpine linux package manager online
    private static String[] extractFromAlpine(String p) throws IOException {

        String[] purlSplit = p.split("[/@]");
        String nameFromPurl = purlSplit[2];
        HttpURLConnection q = queryURL("https://pkgs.alpinelinux.org/packages?name=" + nameFromPurl);
        htmlResult result = getHtmlResult(q); // I had the IDE do this
        String html = result.response().toString();
        result.in().close();

        // if name not found
        if(html.contains("not found"))
            return new String[]{"", "", ""};

        //otherwise
        String table = html.split("<tbody>")[1];
        String row = table.split("<tr>")[1]; //we just care about the top row - most recent result
        String[] columns = row.split("<td");

        String nameColumn = "";
        String versionColumn = "";
        String publisherColumn = "";

        for (String column: columns
             ) {

            if(column.contains("package\">"))
                nameColumn = column;
            else if(column.contains("version\">"))
                versionColumn = column;
            else if(column.contains("maintainer\">")) {
                publisherColumn = column;
                break;
            }
        }
        return new String[]{getSpecific(nameColumn), getSpecific(versionColumn), getSpecific(publisherColumn).strip()};
    }

    //extract name, version, and publisher from Debian linux package manager online
    private static String[] extractFromDebian(String p){
        return null;
    }

    //from the last HTML element we narrow down to, find what we are looking for at the top of the table
    private static String getSpecific(String column) {
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

    //get HTML from connection
    private static htmlResult getHtmlResult(HttpURLConnection q) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(q.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return new htmlResult(in, response);
    }

    //for readability
    private record htmlResult(BufferedReader in, StringBuffer response) {
    }

    //queryURL() from Parser.java in BenchmarkParser
    protected static HttpURLConnection queryURL(String urlString) throws IOException {
        try {
            final URL url = new URL(urlString);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            connection.connect();
            return connection;
        }
        catch (SocketTimeoutException ignored) {
            throw new SocketTimeoutException("Connection timed out...");
        }
    }
}


