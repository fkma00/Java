import java.util.HashMap;

public class SekvensFletter implements Runnable {
private Beholder beholder;

    public SekvensFletter(Beholder b) {
        beholder = b;
    }

    @Override
    public void run() {
        //System.out.println("Storrelse på fletter før while " + beholder.beholderStoerrelse());

        while(beholder.beholderStoerrelse() > 1) {
            //Henter ut og fjerner de to forste hashmapene i beholderen
            HashMap<String,SubSekvens> map1 = beholder.hentUtfraBeholder();
            HashMap<String,SubSekvens> map2 = beholder.hentUtfraBeholder();
            //Fletter dem sammen
            HashMap<String,SubSekvens> sammenSlaat = Beholder.flett(map1, map2);
            //Putter det flettede resultatet tilbake i beholderen
            beholder.settInniBeholder(sammenSlaat);
        }
        //System.out.println("Storrelse på fletter " + beholder.beholderStoerrelse());
    }
}