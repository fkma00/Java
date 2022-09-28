import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.io.File;
import java.io.FileNotFoundException;

public class FilLeser implements Runnable {
private Scanner fil;
private String filNavn;
private CountDownLatch CountDownLatch;
private Beholder beholder;

/*Har referanse til et CountDownlatch objekt som vi benytter når programmet kjøres for å lage en barriere
for traaden slik at vi venter til alle er ferdige for vi gaar videre med programmet*/
    public FilLeser(String f, CountDownLatch cDL, Beholder b) {
        filNavn = f;
        CountDownLatch = cDL;
        beholder = b;
    }

    @Override
    public void run() {
        Scanner fil = null;
        try {fil = new Scanner(new File(filNavn));} 
        catch (FileNotFoundException e) {System.out.println("Filen eksisterer ikke");}
        
        HashMap<String,SubSekvens> hashMap = new HashMap<String,SubSekvens>();

        while(fil.hasNextLine()) {
            String linje = fil.nextLine();
            if(linje.contains("amino_acid")) {linje = fil.nextLine();}
            
            //Setter Subsekvens lengden lik 3
            int lengde = 3;
            //Saa lenge telleren er mindre eller lik lengden av sekvensen
            //minus lengden av subsekvensen
            for(int i = 0; i <= linje.length()-lengde; i++){
                //Setter data lik daataen på indeksen vi får paa i
                //f.eks forste blir indeks 0,1,2 og andre blir indeks 1,2,3 av sekvensen
                //Dataen fra de tre indeksene vil utgjore en subsekvens
                String data = linje.substring(i, i+lengde);

                //System.out.println(data);

                //Setter subsekvensen lik nokkelen til Hashmappet
                //Og forekommer 1 gang for hver gang vi finner en subsekvens 
                SubSekvens subSekvens = new SubSekvens(data, 1);
                //Legger inn i Hashmappet, data er string men er helt lik
                //subsekvens bare i String form
                hashMap.put(data, subSekvens);
            }
        }

        //Legger Hashmappet/subsekvensen inn i beholderen
        try {beholder.leggTilHashMap(hashMap);}
        catch (Exception e) {System.out.println("Traaden ble avbrutt når den la til en Hashmap");}
        //Teller ned CountDownLatch for hver gang programmet kjores aka hver gang
        //vi lager en subsekvens/hashmap
        CountDownLatch.countDown();
        System.out.println("CountDownLatch verdi: " + CountDownLatch.getCount());
        fil.close();     
    }
}