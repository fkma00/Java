class MilitaerResept extends HvitResept{
  public MilitaerResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit){
    super(legemiddel, utskrivendeLege, pasient, reit);
    prisAaBetale = 0;
  }

  @Override
  public String toString(){
    return "Resept type: " + this.farge() + "\nLegemiddel: " + legemiddel.hentNavn() + "\nPris aa betale: " + this.prisAaBetale() +
            "\nLegens navn: " + utskrivendeLege.hentNavn() + "\nPasient: " + this.pasient.navn + "\nAntall reit: " + reit + "\n";
  }

  @Override
  public String farge(){
    return "militaer";
  }
}
