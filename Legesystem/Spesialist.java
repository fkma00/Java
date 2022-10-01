class Spesialist extends Lege implements Godkjenningsfritak{


  Spesialist(String navn, String kontrollID){
    super(navn);
    this.kontrollID = kontrollID;
  }

  @Override
  public String hentKontrollID(){
    return kontrollID;
  }

  @Override
  public String toString(){
    return "Navnet til spesialisten: " + navn + "\nKontroll ID: " + kontrollID + "\n";
  }
}
