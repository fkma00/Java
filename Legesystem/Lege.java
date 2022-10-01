import java.util.ArrayList;

public class Lege implements Comparable<Lege>{
  int nmidler;
  protected String navn;
  protected Lenkeliste<Resept> utskrevedeResepter = new Lenkeliste<>();
  protected String kontrollID;

  Lege(String navn){
    this.navn = navn;
    kontrollID = "0";

  }
  public String hentKontrollID(){
    return kontrollID;
  }

  public String hentNavn(){
    return navn;
  }

  @Override
  public String toString(){
    return "Navnet til legen: " + navn + "\n";
  }

  @Override
  public int compareTo(Lege lege){
    return navn.compareTo(lege.hentNavn());
  }

  public ArrayList hentResepter(){
    ArrayList<Resept> res = new ArrayList<>();
    //Iterator iterator = utskrevedeResepter.iterator();
    for(Resept resept : utskrevedeResepter){
        //Resept resept = iterator.peker.data;
        res.add(resept);
    }

    return res;
  }

  public HvitResept skrivHvitResept(Legemiddel legemiddel, Pasient pasient, int reit) throws UlovligUtskrift{
    HvitResept hvitResept = new HvitResept(legemiddel, this, pasient, reit);
    if (legemiddel instanceof Narkotisk && !(this instanceof Spesialist)) {
      throw new UlovligUtskrift(this, legemiddel, pasient.pasientId);
    }
    if (legemiddel instanceof Narkotisk){
      nmidler++;
    }
    utskrevedeResepter.leggTil(hvitResept);
    return hvitResept;
  }

  public MilitaerResept skrivMilitaerResept(Legemiddel legemiddel, Pasient pasient, int reit) throws UlovligUtskrift{
    MilitaerResept militaerResept = new MilitaerResept(legemiddel, this, pasient, reit);
    if (legemiddel instanceof Narkotisk && !(this instanceof Spesialist)) {
      throw new UlovligUtskrift(this, legemiddel, pasient.pasientId);
    }
    if (legemiddel instanceof Narkotisk){
      nmidler++;
    }
    utskrevedeResepter.leggTil(militaerResept);
    return militaerResept;
  }

  public PResept skrivPResept(Legemiddel legemiddel, Pasient pasient) throws UlovligUtskrift{
    PResept pResept = new PResept(legemiddel, this, pasient);
    if (legemiddel instanceof Narkotisk && !(this instanceof Spesialist)) {
      throw new UlovligUtskrift(this, legemiddel, pasient.pasientId);
    }
    if (legemiddel instanceof Narkotisk){
      nmidler++;
    }
    utskrevedeResepter.leggTil(pResept);
    return pResept;
  }

  public BlaaResept skrivBlaaResept(Legemiddel legemiddel, Pasient pasient, int reit) throws UlovligUtskrift{
    BlaaResept blaaResept = new BlaaResept(legemiddel, this, pasient, reit);
    if (legemiddel instanceof Narkotisk && !(this instanceof Spesialist)) {
      throw new UlovligUtskrift(this, legemiddel, pasient.pasientId);
    }
    if (legemiddel instanceof Narkotisk){
      nmidler++;
    }
    utskrevedeResepter.leggTil(blaaResept);
    return blaaResept;
  }

}