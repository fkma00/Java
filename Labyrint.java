import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Oblig 7

public class Labyrint extends JPanel{
private File labyrintFil;
private Rute rute;
public Rute[][] labyrint;
public static ArrayList<ArrayList<Tuppel>> utveier;
private int antallKolonner = 0;
private int antallRader = 0;

    public Labyrint(File fil) throws FileNotFoundException{
        labyrintFil = fil;
        lesFil();
    }

    private void lesFil() throws FileNotFoundException {
        Scanner fil = new Scanner(labyrintFil);

        //I innlesing av storrelsen for labyrintfilen kommer rader før kolonner, men for 
        //rute koordinatene hart jeg kolonne før rader, x for kolonne og y for rad
        //Når jeg referer til det tomdimensjonale rutenettet som er labyrinten setter jeg 
        //rader før kolonner
        antallRader = fil.nextInt();
        antallKolonner = fil.nextInt();
        
        fil.nextLine();

        //Oppretter en labyrint i form av en to-dimensjonal Array
        //Storrelsen bestemmer jeg med tallene lest inn fra forste linje
        labyrint = new Rute[antallRader][antallKolonner];

        String linje = "";
        
        for (int rad = 0; rad < antallRader; rad++) {
            linje = fil.nextLine();
            for (int kolonne = 0; kolonne < antallKolonner; kolonne++) {
                if(linje.charAt(kolonne) == ('#')){
                    Rute sortRute = new SortRute(this, kolonne, rad);
                    labyrint[rad][kolonne] = sortRute;
                    sortRute.ruteGUI();
                }

                //Aapninger kan ikke være kanter i en labyrint, kun langs kanten
                else if(linje.charAt(kolonne) == ('.')) {
                    //Aapninger på nordkanten
                    if(rad == 0 && kolonne > 0 && kolonne < antallKolonner) {
                        Rute aapning = new Aapning(this, kolonne, rad);
                        labyrint[rad][kolonne] = aapning;
                        aapning.ruteGUI();
                        System.out.println("Nordaapning: (" + kolonne + ", " + rad + ")");                    
                    }
                    //Aapninger på sorkanten
                    else if(rad == antallRader-1 && kolonne > 0 && kolonne < antallKolonner) {
                        Rute aapning = new Aapning(this, kolonne, rad);
                        labyrint[rad][kolonne] = aapning;
                        aapning.ruteGUI();
                        System.out.println("Soraapning: (" + kolonne + ", " + rad + ")");                    
                    }
                    //Aapninger på vestkanten
                    else if(kolonne == 0 && rad > 0 && rad < antallRader) {
                        Rute aapning = new Aapning(this, kolonne, rad);
                        labyrint[rad][kolonne] = aapning;
                        aapning.ruteGUI();
                        System.out.println("Vestaapning: (" + kolonne + ", " + rad + ")");
                    }
                    //Aapninger på ostkanten
                    else if(kolonne == antallKolonner-1 && rad > 0 && rad < antallRader) {
                        Rute aapning = new Aapning(this, kolonne, rad);
                        labyrint[rad][kolonne] = aapning;
                        aapning.ruteGUI();
                        System.out.println("Ostaapning: (" + kolonne + ", " + rad + ")");                    
                    }
                    //Hvis det ikke er en hvitrute langs kanten er det en vanlig hvit rute
                    else {
                        Rute hvitRute = new HvitRute(this, kolonne, rad);
                        labyrint[rad][kolonne] = hvitRute;
                        hvitRute.ruteGUI();
                    }
                }           
            }
        }
        
        //Nøstet forløkke med formål å sette naboer
        for (int rad = 0; rad < antallRader; rad++) {
            for (int kolonne = 0; kolonne < antallKolonner; kolonne++) {
                //Setter naboen nord for ruten i følgende indeks til å være på indeksen 1 rad mindre
                if(rad > 0) {
                    labyrint[rad][kolonne].settNord(labyrint[rad-1][kolonne]);
                }
                //Setter naboen sør for ruten i følgende indeks til å være på indeksen 1 rad mer
                if(rad < antallRader-1) {
                    // System.out.println("Sor");
                    // System.out.println(rad +  " " + kolonne + " " + antallRader  + " " + antallKolonner);
                    labyrint[rad][kolonne].settSor(labyrint[rad+1][kolonne]);
                }
                //Setter naboen vest for ruten i følgende indeks til å være på indeksen 1 kolonne mindre
                if(kolonne > 0) {
                    labyrint[rad][kolonne].settVest(labyrint[rad][kolonne-1]);
                }
                //Setter naboen øst for ruten i følgende indeks til å være på indeksen 1 kolonne mer
                if(kolonne < antallKolonner-1) {
                    labyrint[rad][kolonne].settOst(labyrint[rad][kolonne+1]);
                }
            }
        }
        fil.close();
    }

    public Rute hentRute(int kolonne, int rad) {
        return labyrint[rad][kolonne];
    }

    public void leggTilUtvei(ArrayList<Tuppel> tuppelUtvei) {
        //Legger inn Arraylisten over tuppler inn i Arraylisten som
        //skal ta vare på arraylistene av tuppel objekter
        //Metoden bruker jeg i aapning klassen for hver aapning vi finner
        utveier.add(tuppelUtvei);
    }

    public ArrayList<ArrayList<Tuppel>> finnUtveiFra(int x, int y) {
        //Oppretter Arraylisten som skal ta vare på Arraylister av Tuppel objekter
        utveier = new ArrayList<ArrayList<Tuppel>>();
        //Henter rute og kaller på metoden for å finne utvei fra ruten
        hentRute(x, y).finnUtVei();
        //returnerer Arraylisten som holder på arraylisten av tuppel objekter
        //for å få oversikt over utveier fra ruten i labyrinten
        return utveier;
    }

    public void labyrintGUI() {
        //Setter rutenett størrelsen til å være lik antall rader og kolonner fra en labyrinten
        setLayout(new GridLayout(antallRader, antallKolonner));
        for (int rad = 0; rad < antallRader; rad++) {
            for (int kolonne = 0; kolonne < antallKolonner; kolonne++) {
                //Legger til Rute objektene som er JButtons inn i labyrinten som er en JPanel
                add(labyrint[rad][kolonne]);
            }
        }
    }

    //Metode for å nullstille labyrinten for når man trykker på en ny rute
    public void reset() {
        //Iterer gjennom alle Rute objektene i labyrinten
        for (int rad = 0; rad < antallRader; rad++) {
            for (int kolonne = 0; kolonne < antallKolonner; kolonne++) {
                Rute r = labyrint[rad][kolonne];
                //Kaller på reset metoden i HvitRute for å sette en blank tekst på rutene for nullstilling
                //for hver gang man trykker på en rute i labyrinten, når jeg itererer gjennom alle rutene i labyrinten
                r.reset();
            }
        }
    }

    @Override
    public String toString() {
        //for hver Array av rader i labyrinten
        for (Rute[] rad : labyrint) {
            //for hver rute i rad Array
            for (Rute rute : rad) {
                System.out.print(rute);
            }
            System.out.println();
        }
        return "";
    }
}