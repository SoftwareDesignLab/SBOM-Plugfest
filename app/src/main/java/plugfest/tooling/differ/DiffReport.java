package plugfest.tooling.differ;


import java.util.ArrayList;


public class DiffReport {
    
    private static String dfn = "NONAME", cfn = "NONAME";
    private static final ArrayList<Object> replist = new ArrayList<Object>();
    private String  linenum = "-1";
    private String  dlistcontent = "";
    private String  clistcontent = "";
    
    public DiffReport(){};
    public DiffReport(Object ln, Object dc, Object cc){
           this.linenum = ln.toString();
           this.dlistcontent = dc.toString();
           this.clistcontent = cc.toString();
    };
    
    public void append(Object s){
        replist.add(s);
    }
    
    private void printHeader(){
        System.out.println("< " + this.dfn + " : " + "> " + this.cfn + "\n-----");
    }
    
    private void printHeader(String dfn, String cfn){
        System.out.println("< " + dfn + " : " + "> " + cfn + "\n-----");
    }
    
    public void print(){
        if(! this.dfn.equals("NONAME")) {
           this.printHeader();
        }
        for(Object i : replist)
        {
            System.out.println(i);
        }
    }
    
    public void setFn(String dfn, String cfn){
        this.dfn = dfn;
        this.cfn = cfn;
    }
    
   // Overriding toString() method of String class
    @Override
    public String toString() {
        return this.linenum  + "\n=====\n" + 
               "< " + this.dlistcontent + "\n" +
               "> " + this.clistcontent + "\n";
    }
}

