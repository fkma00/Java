class BlaaResept extends Resept{
  BlaaResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
    super(legemiddel, utskrivendeLege, pasient, reit);
    int nyPris = (int) Math.round(legemiddel.hentPris() * 0.25); //Avrunder svaret jeg får ved bruk av Math.round til nærmeste heltall.
    prisAaBetale = nyPris;
  }

  @Override
  public String toString(){
    return "Resept type: " + this.farge() + "\nLegemiddel: " + legemiddel.hentNavn() + "\nPris aa betale: " + this.prisAaBetale() + "\nLegens navn: "
            + utskrivendeLege.hentNavn() + "\nPasient: " + this.pasient.navn + "\nAntall reit: " + reit + "\n";
  }

  @Override
  public String farge(){
    return "blaa";
  }

}
