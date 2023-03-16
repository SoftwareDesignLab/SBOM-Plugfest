package plugfest.tooling.differ;

import java.util.ArrayList;


public class FullDiff {
    private static Long maxSize= 100000L;
    
    private ReadFile d = null; 
    private ReadFile c = null;
    private DiffReport report = new DiffReport();
    
    public FullDiff(){};
    
    public FullDiff(ReadFile d, ReadFile c){
         this.d = d;
         this.c = c;
    }
    
    public DiffReport diff()
    {
        ArrayList<Object> dd = this.d.getdata();
        ArrayList<Object> cc = this.c.getdata();
        int dsize = dd.size();
        int csize = cc.size();
        int c; // = (dsize < csize) ? dsize: csize;
        
        if(dsize < csize){
            c = dsize;
            this.report.setFn(this.d.getfilename(), this.c.getfilename());
        }
        else {
            c = csize;
            this.report.setFn(this.c.getfilename(), this.d.getfilename());
            dd = this.c.getdata();
            cc = this.d.getdata();
        }
        
        //check file size????????
//        if(dsize != csize) {
//          report.append(new DiffReport( 0,"file 1's size is not the same as file 2",
//                                             "file 2's size is not the same as file 1"));
//        }
        
        for(int i=0; i<c; i++)
        {
            if( ! dd.get(i).toString().equals(cc.get(i).toString())) {
                //finding the position of the first diff char
                
                //if c > maxsize:
                    //printout header
                    //output result to console 
                //else
                report.append(new DiffReport( i+1, dd.get(i), cc.get(i)));
            }               
        }
        
        //output the rest from the longer file:
           //stdout or report.append();
        
        return report;
    }
    
    public void append(Object s){
        report.append(s);
    }
}

