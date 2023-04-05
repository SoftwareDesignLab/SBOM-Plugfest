package plugfest.tooling.sbom;
/**
 * File: PURL.java
 * Object representation of the Package URl for a component
 *
 * @author Juan Francisco Patino
 */
public class PURL {

    private String name;
    private String version;
    private ComponentPackageManager pm;
    private String PURLString;
    private PURL child;

    public PURL(String PURL){
        this.PURLString = PURL;
        addFromString();
    }

    public PURL(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ComponentPackageManager getPackageManager() {
        return pm;
    }

    public void setPackageManager(ComponentPackageManager pm) {
        this.pm = pm;
    }

    public void setPURLString(String PURLString) {
        this.PURLString = PURLString;
    }

    public void addFromString(){
        assert PURLString != null;
        addFromString(PURLString);
    }

    public void addChild(PURL c){
        PURL temp = this.getChild();
        while(temp != null){
            temp = temp.getChild();
        }
        temp = c;
    }

    public void addFromString(String purl){
        String p = purl.toLowerCase();
        if(p.contains("alpine"))
            setPackageManager(ComponentPackageManager.Alpine);
        else if(p.contains("debian"))
            setPackageManager(ComponentPackageManager.Debian);
        else // add cases here as PMs are added
            setPackageManager(ComponentPackageManager.Python);

        switch (this.pm){
            case Alpine:
                String[] purlSplit = p.split("[/@]");
                this.name = purlSplit[2];
                purlSplit = p.split("[@?]");
                this.version = purlSplit[1];
                break;
            case Debian:
        }

    }

    public PURL getChild() {
        return child;
    }

    public void setChild(PURL child) {
        this.child = child;
    }
    @Override
    public String toString() {
        return PURLString;
    }
}
