abstract class Resept{

  protected static int teller = 1;
  protected int id;
  protected Legemiddel legemiddel;
  protected Lege utskrivendeLege;
  protected Pasient pasient;
  protected int reit;
  protected int prisAaBetale;

  Resept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
    this.legemiddel = legemiddel;
    this.utskrivendeLege = utskrivendeLege;
    this.pasient = pasient;
    this.reit = reit;
    this.id = teller;
    prisAaBetale = this.legemiddel.hentPris();
    teller++;
  }

  public int hentId(){
    return id;
  }

  public String hentLegemiddel(){
    return legemiddel.hentNavn();
  }

  public String hentLege(){
    return utskrivendeLege.hentNavn();
  }

  public Pasient hentPasient(){
    return pasient;
  }

  public int hentReit(){
    return reit;
  }

  public boolean bruk(){
    return this.hentReit() > 0;
  }// Hvis Reit er større enn 0, return true

  abstract public String farge();

  public int prisAaBetale(){
    return prisAaBetale;
  }

  public String toString(){
    return hentId() + ": " + hentLegemiddel() + "(" + hentReit() + "reit)";
  }
}
