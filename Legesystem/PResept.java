class PResept extends HvitResept{

  final private static int rabatt = 108;

  PResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient){
    super(legemiddel, utskrivendeLege, pasient, 3);
  }

  @Override
  public String toString(){
    return "Resept type: " + this.farge() + "\nLegemiddel: " + legemiddel.hentNavn() + "\nPris aa betale: " + this.prisAaBetale() + "\nLegens navn: " + utskrivendeLege.hentNavn() + "\nPasient: " + this.pasient.navn + "\nAntall reit: " + reit + "\n";
  }

  @Override
  public String farge(){
    return "P resept";
  }

  @Override
  public int prisAaBetale(){
    prisAaBetale = legemiddel.hentPris() - rabatt;
    if (prisAaBetale < 0){
      prisAaBetale = 0;
    }
    return prisAaBetale;
  }

}
