import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HvitRute extends Rute {

    public HvitRute(Labyrint lab, int x, int y) {
        super(lab, x, y);
    }

    @Override
    public char tilTegn() {
        return '.';
    }

    @Override
    public void gaa(Rute ruteForrige, ArrayList<Tuppel> tuppelListe) {
        /*Lagrer en lokal kopi av Arraylisten for hver gang jeg kommer til en ny rute 
        for å ikke blande de forskjellige utveiene til en aapning fra en gitt rute*/
        tuppelListe = new ArrayList<Tuppel>(tuppelListe);
        //Legger til et nytt Tuppel objekt for hver hvitrute vi gaar forbi
        tuppelListe.add(new Tuppel(kolonne, rad));
        //naboene gaar kun en vei hvis neste objekt de gaar til ikke er null/ingenting, men et Rute objekt
        //Eller hvis det ikke er ruten de kom fra
        if(nordNabo != null && nordNabo != ruteForrige) {
            nordNabo.gaa(this, tuppelListe);
        }
        if(sorNabo != null && sorNabo != ruteForrige) {
            sorNabo.gaa(this, tuppelListe);
        }
        if(vestNabo != null && vestNabo != ruteForrige){
            vestNabo.gaa(this, tuppelListe);
        }
        if(ostNabo != null && ostNabo != ruteForrige) {
            ostNabo.gaa(this, tuppelListe);
        }
    }

    @Override
    public void ruteGUI() {

        class Rutevelger implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                //Kaller på reset metoden fra Labyrint
                labyrintRute.reset();
                //Utveier er lik retur verdien vi får av å kalle på finnUtveiFra
                ArrayList<ArrayList<Tuppel>> utveier = labyrintRute.finnUtveiFra(kolonne, rad);
                //Henter den første Arraylisten fra utveier
                ArrayList<Tuppel> sti = utveier.get(0);
                //Iterer gjennom stien vi henter ut fra utveier
                for (Tuppel tuppel : sti) {
                    //Kaller først på labyrinten Rute referer til og deretter den to-dimensjonale
                    //Arrayet i Labyrint og bruker tuppels rad og kolonne for å finne riktig indeks i
                    //to-dimensjonale arrayet og setter tekst på disse rutene på stien med X
                    labyrintRute.labyrint[tuppel.rad][tuppel.kolonne].setText("X");            
                }
                //Kaller på antUtveier i Hovedprogram klassen for å skrive ut antall utveier funnet fra en valgt rute
                Hovedprogram.antUtveier();
                System.out.println(utveier);
                
                
            }
        }
        addActionListener(new Rutevelger());
    }

    public void reset() {
        this.setText(" ");
    }
}
