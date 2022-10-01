
public class UgyldigInfo extends Exception{
    public UgyldigInfo(String[] data){
        super("Ugyldig informasjon: " + data);
    }
}