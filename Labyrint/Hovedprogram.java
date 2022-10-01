import java.io.File;
import java.io.FileNotFoundException;

import java.awt.*;
import javax.swing.*;

class Hovedprogram {

static File fil;
static Labyrint lab;
static JPanel panel;
static JLabel antUtveier;

    public static void main(String[] args) {

        JFileChooser velger = new JFileChooser();
        int resultat = velger.showOpenDialog(null);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            fil = velger.getSelectedFile();
        } else {
            System.exit(0);
        }

        //Lager en JFrame for å legge til paneler i
        JFrame vindu = new JFrame("Labyrint GUI");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        vindu.add(panel);

        //Setter opp Borderlayout i panelet
        panel.setLayout(new BorderLayout());
        //Lager en nord del for en overskrift for programmet
        JPanel nord = new JPanel();
        panel.add(nord, BorderLayout.NORTH);
        JLabel overskrift = new JLabel("Trykk på en rute for å finne utvei fra labyrinten");
        nord.add(overskrift);

        Labyrint lab = null;
        try {
            //lab er lik den valgte filen fra Jfile chooser
            lab = new Labyrint(fil);
            //Legger den til i sentrum av panelet
            panel.add(lab, BorderLayout.CENTER);
            lab.labyrintGUI();
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke fil");
        }

        //Legger til print av antall utveier funnet fra en rute sør i panelet
        JPanel south = new JPanel();
        panel.add(south, BorderLayout.SOUTH);
        antUtveier = new JLabel();
        south.add(antUtveier);

        vindu.pack();
        vindu.setVisible(true);
    }

    //En statisk metode for å skrive ut antall utveier funet fra en valgt rute
    public static void antUtveier() {
        antUtveier.setText("Antall utveier funnet fra valgt rute: " + lab.utveier.size());
    }
}