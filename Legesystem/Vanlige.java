class Vanlige extends Legemiddel{
  Vanlige(String navn, int pris, double virkestoff){
    super(navn, pris, virkestoff);
  }

  @Override
  public String hentType(){
    return "vanlig";
  }

  @Override
  public String toString() {
    return "Navn: " + navn + "\nType: Vanlig" + "\nPris: " + pris + "\nVirkestoff mengde: " + virkestoff + "\n";
  }
}
