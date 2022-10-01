class Vanedannende extends Legemiddel{
  private int styrke;

  Vanedannende(String navn, int pris, double virkestoff, int styrke){
    super(navn, pris, virkestoff);
    this.styrke = styrke;
  }

  @Override
  public int hentStyrke(){
    return styrke;
  }

  @Override
  public String hentType(){
    return "vanedannende";
  }
  @Override
  public String toString() {
    return "Navn: " + navn + "\nType: Vanedannende" + "\nPris: " + pris + "\nVirkestoff mengde: " + virkestoff + "mg\nVanedannende styrke: " + styrke + "\n";
  }
}
