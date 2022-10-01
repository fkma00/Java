import java.util.Iterator;
public class Lenkeliste<T> implements Liste<T>{
    
    class Node{
        Node neste = null;
        T data;
        Node (T x) {
            data = x;
        }
    }
    protected class LenkelisteIterator implements Iterator<T>{
        public Node peker;
        public int pos;

        public LenkelisteIterator(){
            peker = start;
            pos = -1;
        }

        public T next(){
            peker = peker.neste;
            pos++;
            return peker.data;
        }

        public boolean hasNext(){
            return peker.neste != null;
        }
    }

    protected Node start = new Node(null);

    @Override
    public Iterator<T> iterator(){
      return new LenkelisteIterator();
    }



    public int stoerrelse(){
        Node peker = start;
        int teller = 0;

        while (peker.neste != null){
            teller++;
            peker = peker.neste;
        }

        return teller;
    }

    public void leggTil(int pos, T x){
        Node peker = start;
        Node nyNode = new Node(x);

        /*Ugyldig liste indeks hvis posisjon er mindre enn 0 eller større enn størrelsen.
        Posisjon kan være indeks+1 (Altså stoerrelse() verdien) fordi da legges det et element bak siste element*/
        if (pos < 0 || pos > this.stoerrelse()){
            throw new UgyldigListeIndeks(pos);
        }

        for (int i = 0; i < pos; i++){
            peker = peker.neste;
        }

        nyNode.neste = peker.neste;
        peker.neste = nyNode;

    }

    public void leggTil(T x){

        Node peker = start;
        Node nyNode = new Node(x);

        while (peker.neste != null){
            peker = peker.neste;
        }
        peker.neste = nyNode;
    }

    public void sett(int pos, T x){
        Node peker = start;

        /*Posisjon er ugyldig hvis det er utenfor indeks verdiene, altså mindre enn 0, og større eller lik stoerrelse().*/
        if (pos < 0 || pos >= this.stoerrelse()){
            throw new UgyldigListeIndeks(pos);
        }
        /*Ellers itereres det til posisjon +1 for at peker variabelen er elementet vi letet etter*/
        else{
            for (int i = 0; i < pos + 1; i++){
                peker = peker.neste;
            }
            peker.data = x;
        }
    }

    public T hent(int pos){
        Node peker = start;

        if (pos < 0 || pos >= this.stoerrelse()){
            throw new UgyldigListeIndeks(pos);
        }

        else {
            for (int i = 0; i < pos + 1; i++) {
                peker = peker.neste;
            }
            return peker.data;
        }

    }

    public T fjern(int pos){
        Node peker = start;
        T data;

        if (pos < 0 || pos >= this.stoerrelse()){
            throw new UgyldigListeIndeks(pos);
        }
        /*Hvis det finnes kun ett element i lista, så henter man ut data og gjør listen tom*/
        else if (pos == 0 && peker.neste.neste == null){
            data = peker.neste.data;
            peker.neste = null;
            return data;
        }
        /*Hvis posisjonen tilsvarer siste indeks, så itererer man frem til nest siste element og bearbeider siste element derifra*/
        else if (pos == this.stoerrelse() - 1){


            for (int i = 0; i < pos; i++){
                peker = peker.neste;
            }

            data = peker.neste.data;
            peker.neste = null;
            return data;
        }
        /*Ellers itererer man til elementet foran den i det angitte posisjonen og bearbeider elementet i det angitte posisjoen derifra. */
        else{
            for (int i = 0; i < pos; i++){
                peker = peker.neste;
            }
            data = peker.neste.data;
            peker.neste = peker.neste.neste;
            return data;
        }


    }

    public T fjern(){
        Node peker = start;
        T data;
        /*Hvis det ikke finnes noe å fjerne, så kastes det ugyldig liste indeks*/
        if (peker.neste == null){
            throw new UgyldigListeIndeks(0);
        }
        /*Hvis det finnes kun ett element, så henter man ut data og tømmer lista*/
        else if(peker.neste.neste == null){
            data = peker.neste.data;
            peker.neste = null;
            return data;
        }
        /*Ellers henter man ut data fra første element, og får start til å peke på det andre elementet i lista*/
        else{
            data = peker.neste.data;
            peker.neste = peker.neste.neste;
            return data;
        }
    }

    public String toString() {
        String innhold = "";
        Node peker = start;

        if (stoerrelse() == 0) {
            return "Tom liste";
        }
        else{
            while (peker.neste != null){
                innhold = innhold + peker.neste.data + "\n";
                peker = peker.neste;
            }
            return innhold;

        }
    }

}
