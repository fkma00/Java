import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.io.File;

public class SekvensMain {

    public static void main(String[] args){
        int antallFlettere = 0;
        try {
            //Bestemmer antall flettere for beholderne i terminalen i form av en parameter til programmet
            antallFlettere = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Send inn et argument med programmet om hvor mange flettere du ønsker");
            System.exit(0);
        }

        Scanner filLeser = null;
        try {
            //filLeser = new Scanner(new File("TestData/" + "metadata.csv"));
            filLeser = new Scanner(new File("Data/" + "metadata.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke fil");
        }
        
        //Lager to beholdere for å skille dataen/subsekvensene fra de friske og syke pasientene
        Beholder friske = new Beholder();
        Beholder syke = new Beholder();
        /*En filliste for alle filene vi leser fra metadata filen. I hovedprogrammet
        leser vi filene fra metadata og i filleser tråden leser vi dataen fra hver enkelt fil*/
        ArrayList<String> filListe = new ArrayList<String>();
        //Lager enn Boolean Arraylist for å kunne skille om pasientene er syke
        //eller friske fra dataen vi får
        ArrayList<Boolean> pasientTilstand = new ArrayList<Boolean>();
        boolean tilstand = true;

        while(filLeser.hasNextLine()) {       
            String linje = filLeser.nextLine();
            if(linje.contains("repertoire_file")) {linje = filLeser.nextLine();}
            else if(linje.contains("testfiler")){linje = filLeser.nextLine();}

            String[] filData = linje.split(",");
            //String filNavn = "TestData/" + filData[0];
            String filNavn = "Data/" + filData[0];

                //Hvis String på indeks 1 etter split på komma er lik "False"
                if(filData[1].equals("False")) {
                    //Legger vi inn vi at pasienten er frisk, altså negativt for virus
                    pasientTilstand.add(false);
                }
                else{
                    //Ellers legger vi inn at pasienten er syk, altså positivt for virus
                    pasientTilstand.add(true);}
            //Legger inn filen som representerer en pasient inn i filliste    
            filListe.add(filNavn);
        }

        //Oppretter en CountDownLatch på storrelse med antall filer
        //Vil skape en barriere som venter på at alle trådene er ferdige før vi går
        //videre med programmet. Vil telle ned fra antall filer ned til 0, for eksempel 120 til 0
        CountDownLatch cdl = new CountDownLatch(filListe.size());
        
        for (int i = 0; i < pasientTilstand.size(); i++) {
            //Henter tilstanden fra indeks i, i pasentTilstand listen
            boolean tilstanden = pasientTilstand.get(i);
            //Henter fil for å få data fra pasient på indeks i
            String fil = filListe.get(i);
            /*Hvis tilstanden er true er pasienten syk og vi oppretter tråder for å lese
            dataen og legge det inn i sykbeholderen, hvis false gjør vi det samme med friskbeholderen*/
            if(tilstanden) {
                new Thread(new FilLeser(fil, cdl, syke)).start();}
            else{
                new Thread(new FilLeser(fil, cdl, friske)).start();}
        }

        try {
            //Venter til alle trådene er fullførte med en barriere skapt av CountDownLatch
            cdl.await();
            //Setter trådene til å dvale i 3000 millisekunder
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Countdown ventingen ble avbrutt");
        }

        System.out.println("Antall maps for fletter i beholderne " + 
        "\nFriskbeholder: "+ friske.beholderStoerrelse() + 
        "\nSykbeholder: " + syke.beholderStoerrelse());

        /*Lager en Arraylist for filfletterne for å bruke join som barriere for trådene */
        ArrayList<Thread> filFletterListe = new ArrayList<Thread>();
        //Så lenge telleren er mindre enn antallflette bestemt som input til programmer
        for (int j = 0; j < antallFlettere; j++) {
            //Oppretter en flettetråd for friskbeholderen
            Thread friskFletter = new Thread(new SekvensFletter(friske));
            //Legger flettetråden inn i listen
            filFletterListe.add(friskFletter);
            //starter tråden
            friskFletter.start();
        
            //Det samme som over bare for de syke
            Thread sykFletter = new Thread(new SekvensFletter(syke));
            filFletterListe.add(sykFletter);
            sykFletter.start();
        }

        //For hver filfletter vi har lagt inn i listen
        for (Thread filFletter : filFletterListe) {
            try {
                //Join for å vente til alle trådene er fullførte før vi går videre i programmet
                //Skaper en barriere for de fullførte trådene som venter til alle er ferdige
                filFletter.join();
            } catch (InterruptedException e) {
                System.out.println("Filfletter barrieren ble avbrutt");
            }
        }

        /*Etter at flettingen er ferdig for begge beholderne henter vi ut det 
        første og siste hashmappet inni beholderen ut */
        HashMap<String,SubSekvens> friskeSekvenser = friske.hentUtfraBeholder();
        HashMap<String,SubSekvens> sykeSekvenser = syke.hentUtfraBeholder();

        System.out.println("Antall maps etter fletting og endelig HashMap" +
        "er hentet ut: " + "Friske: " + friske.beholderStoerrelse() + " Syke: " + syke.beholderStoerrelse());
        
        int friskteller = 0;
        //Iterer gjennom alle subsekvensene som forekommer hos de friske
        for (String nokkel : friskeSekvenser.keySet()) {
            //Oppretter en subsekvens objekt med nokkelen
            SubSekvens friskSubSekvens = friskeSekvenser.get(nokkel);
            
            //Hvis subsekvensen forekommer 5 eller flere ganger plusser vi telleren
            //for de friske pasientene
            if (friskSubSekvens.hentantallSubSekvens() >= 5) {
                //System.out.println(nokkel);
                friskteller++;
            }
        }
        
        System.out.println("Dominante subsekvenser hos friske som forekommer 5 eller flere ganger: " + friskteller);
        
        int sykteller = 0;
        for (String nokkel : sykeSekvenser.keySet()) {
            SubSekvens sykSubSekvens = sykeSekvenser.get(nokkel);
            if (sykSubSekvens.hentantallSubSekvens() >= 5) {
                // System.out.println(nokkel);
                sykteller++;
            }
        }
        
        System.out.println("Dominante subsekvenser hos syke som forekommer 5 eller flere ganger: " + sykteller);

        System.out.println("\nAlle subsekvenser der antall ​forekomster hos personer som har hatt viruset " +
        "\nminus antall ​forekomster hos personer som ikke har hatt viruset er større enn ​eller ​lik 5: " +
        "\nDifferansen sier hvor mange flere ganger subsekvensen forekommer hos de som har hatt viruset");

        //Iterer gjennom alle subsekvensene hos de syke
        for (String syknokkel : sykeSekvenser.keySet()) {
            //Setter en syk og frisk subsekvens lik den subsekvensen vi får for hver iterasjon
            SubSekvens sykSubSekvens = sykeSekvenser.get(syknokkel);
            SubSekvens friskSubSekvens = friskeSekvenser.get(syknokkel);
            
            //Hvis subsekvensen hos de syke også forekommer hos de friske
            if(friskSubSekvens != null) {
                //Setter en variabel lik antall ganger den forekommer
                int antallForekomsterFrisk = friskSubSekvens.hentantallSubSekvens();
                //Hvis subsekvensen hos de syke forekommer minus antall ganger den også forekommer hos de friske
                //Er større eller lik 5
                if(sykSubSekvens.hentantallSubSekvens() - antallForekomsterFrisk >= 5) {
                    //System.out.println(syknokkel);
                    //printer ut subsekvensen og differansen fra antall ganger den forekommer hos de syke minus antall ganger
                    //den forekommer hos de friske
                    System.out.println("\nSubsekvens: " + syknokkel + 
                    "\nAntall forekomster hos de syke: " + sykSubSekvens.hentantallSubSekvens() +
                    "\nAntall forekomster hos de friske: " + friskSubSekvens.hentantallSubSekvens() +
                    "\nDifferanse: " + (sykSubSekvens.hentantallSubSekvens() - antallForekomsterFrisk));
                }
            }
        }
        filLeser.close();
    }
}