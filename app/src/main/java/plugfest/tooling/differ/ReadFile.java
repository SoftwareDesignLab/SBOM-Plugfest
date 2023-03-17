package plugfest.tooling.differ;

import java.io.File; 
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.util.ArrayList;

public class ReadFile {
    
    private String fn = "";
    private final ArrayList<Object> s = new ArrayList<Object>();
    
    public ReadFile(){};

    public ReadFile(String fn) {
        this.fn = fn;
        try {
                File fileObj = new File(fn);
                Scanner myReader = new Scanner(fileObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    s.add(data);
                }
                myReader.close();
            } 
            catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
    }
    
    public ArrayList<Object> getdata() {
        return this.s;
    }
    
    public String getfilename() {
        return this.fn;
    }
}
