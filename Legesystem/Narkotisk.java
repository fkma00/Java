class Narkotisk extends Legemiddel{
  private int styrke;

  Narkotisk(String navn, int pris, double virkestoff, int styrke){
    super(navn, pris, virkestoff);
    this.styrke = styrke;
  }

  public int hentNarkotiskStyrke(){
    return styrke;
  }
  @Override
  public int hentStyrke(){
    return styrke;
  }

  @Override
  public String hentType(){
    return "narkotisk";
  }
  @Override
  public String toString() {
    return "Navn: " + navn + "\nType: Narkotisk" + "\nPris: " + pris + "\nVirkestoff mengde: " + virkestoff + "mg\nNarkotisk styrke: " + styrke + "\n";
  }
}
