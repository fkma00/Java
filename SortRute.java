import java.awt.Color;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SortRute extends Rute {
    
    public SortRute(Labyrint lab, int x, int y) {
        super(lab, x, y);
    }

    @Override
    public char tilTegn() {
        return '#';
    }

    @Override
    public void gaa(Rute ruteForrige, ArrayList<Tuppel> tuppelListe) {
        //Vi Overrider og lar metoden stå tom
        //Dermed vil metoden prøve å gå til et Sortruteobjekt men stoppe.
    }

    @Override
    public void ruteGUI() {
        class Rutevelger implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
            }
        }
        addActionListener(new Rutevelger());
        setText("#");
        //Setter de sorte rutene til å ha sort bakgrunn
        // setBackground(Color.BLACK);
        // setOpaque(true);
        // setBorderPainted(false);

    }

    public void reset() {}
}
