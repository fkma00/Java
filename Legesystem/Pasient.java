class Pasient {
  	String navn;
    String fodselsnummer;
    int pasientId;
    private static int teller = 1;
    Stabel<Resept> resepter;

  	public Pasient(String n, String fn) {
      navn = n;
      fodselsnummer = fn;
      pasientId = teller;
      teller++;
      resepter = new Stabel<Resept>();
    }

    public String toString(){
        return pasientId + ": " + navn + "(fnr " + fodselsnummer + ")";
    }

}
