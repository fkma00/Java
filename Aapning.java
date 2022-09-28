import java.util.ArrayList;

//Oblig 6

public class Aapning extends HvitRute{
    
    public Aapning(Labyrint lab, int x, int y) {
        super(lab, x, y);
    }

    @Override
    public void gaa(Rute ruteForrige, ArrayList<Tuppel> tuppelListe) {
        /*Lagrer en lokal kopi av Arraylisten for hver gang jeg kommer til en ny rute 
        for å ikke blande de forskjellige utveiene til en aapning fra en gitt rute*/
        ArrayList<Tuppel> nySti = new ArrayList<Tuppel>(tuppelListe);
        //Legger til en Tuppel i listen for hver aapning funnet
        nySti.add(new Tuppel(kolonne, rad));
        //Legger til en utvei i listen over tuppler for labyrinten
        labyrintRute.leggTilUtvei(nySti);
        //System.out.println("(" + rad +", " + kolonne + ")");
        //Metoden skal stoppe når vi har kommet til en aapning, vi kaller derfor ikke på
        //metoden gaa igjen
    }
}
