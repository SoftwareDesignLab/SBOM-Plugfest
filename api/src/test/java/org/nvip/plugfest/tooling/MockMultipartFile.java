package org.nvip.plugfest.tooling;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * File: MockMultipartFile.java
 * Mock MultipartFile class for testing purposes
 *
 * @author Juan Francisco Patino
 */
public class MockMultipartFile implements MultipartFile {

    private final String path;
    private byte[] content;

    /**
     * Constructor
     * @param path - path of the file
     */
    public MockMultipartFile(String path){

        this.path = path;
        StringBuilder s = new StringBuilder();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                s.append('\n').append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        content = s.toString().getBytes(StandardCharsets.UTF_8);
    }

    ///
    /// Getters and Setters
    ///

    @Override
    public String getName() {
        return path.substring(path.toLowerCase().lastIndexOf('/'));
    }

    @Override
    public String getOriginalFilename() {
        return path;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    ///
    /// Overrides
    ///

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }
}
