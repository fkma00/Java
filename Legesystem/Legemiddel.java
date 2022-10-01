public abstract class Legemiddel{

  protected String navn;
  protected int id;
  protected static int teller = 1;
  protected int pris;
  protected double virkestoff;

  Legemiddel(String navn, int pris, double virkestoff){
    this.navn = navn;
    this.id = teller;
    this.pris = pris;
    this.virkestoff = virkestoff;
    teller++;
  }

  public String hentType(){
    return "";
  }

  public int hentStyrke(){
    return 0;
  }

  public int hentId(){
    return id;
  }

  public String hentNavn(){
    return navn;
  }

  public int hentPris(){
    return pris;
  }

  public double hentVirkestoff(){
    return virkestoff;
  }

  public void settNyPris(int pris){
    this.pris = pris;
  }
}
