import java.io.File;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Oblig 7

//Rute er abstrakt klasse da vi ikke skal opprette noen direkte Rute objekter,
//kun hvit, soret eller aapning som er subklasser av Rute
public abstract class Rute extends JButton {

protected int kolonne;
protected int rad;
protected Labyrint labyrintRute;
protected Rute nordNabo;
protected Rute sorNabo;
protected Rute vestNabo;
protected Rute ostNabo;
protected ArrayList<Tuppel> tuppelListe;

    protected Rute(Labyrint lab, int x, int y){ 
        labyrintRute = lab;
        kolonne = x;
        rad = y;
    }

    //Gjør tilTegn abstract da den må implementeres og være forskjellig for hvit og sort rute
    abstract public char tilTegn();

    //Gjør gaa metoden abstract da den maa implementeres og være 
    //forskjellig i alle rute klassene 
    public abstract void gaa(Rute ruteForrige, ArrayList<Tuppel> tuppelListe);

    //Lager sett metoder for å sette nye verdier til naborutene
    public void settNord(Rute nN) {
        nordNabo = nN;
    }

    public void settSor(Rute sN) {
        sorNabo = sN;
    }

    public void settVest(Rute vN) {
        vestNabo = vN;
    }
    
    public void settOst(Rute oN) {
        ostNabo = oN;
    }

    public void finnUtVei() {
        ArrayList<Tuppel> tuppelListe = new ArrayList<Tuppel>();
        //Kaller paa gaa metoden for aa finne utvei fra labyrinten
        gaa(this, tuppelListe);
    }

    //Kaller på tilTegn og returnerer 
    public String toString() {
        return tilTegn() + "";
    }
 
    public abstract void ruteGUI();

    public abstract void reset();
}