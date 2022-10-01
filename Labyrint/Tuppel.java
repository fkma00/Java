public class Tuppel {
public int kolonne;
public int rad;

    public Tuppel(int x, int y) {
        kolonne = x;
        rad = y;
    }

    @Override
    public String toString(){
        //Skriver ut koordinatene til ruten vi gaar forbi
        return "(" + kolonne +", " + rad + ")" ;
    }
}
