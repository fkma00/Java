public class SortertLenkeliste<T extends Comparable<T>> extends Lenkeliste<T> {

    @Override
    public void leggTil(T x){
         Node peker = start;
         Node nyNode = new Node(x);
         boolean sortert = false;

         while (!sortert){
             /*Hvis lista er tom, så legges elementet til som første element*/
             if (peker.neste == null){
                 peker.neste = nyNode;
                 sortert = true;
             }
            /*Hvis verdien av dataen i elementet som programmet peker på er
            mindre verdt enn dataen som man oppgir som parameter,
            så går man til neste element*/
             else if (peker.neste.data.compareTo(x) < 0){
                peker = peker.neste;
             }
            /*Hvis verdien av dataen i elementet programmet peker på er mer verdt eller like verdt
            i forhold til dataen som man oppgir som parameter, så vil elementet det pekes på og alle de
            elementene bak den flyttes et hakk bakover, også settes det nye elementet i plassen mellom elementet pekeren befinner seg i og det elementet pekeren peker på.*/
             else if (peker.neste.data.compareTo(x) > 0 || peker.neste.data.compareTo(x) == 0){
                nyNode.neste = peker.neste;
                peker.neste = nyNode;
                sortert = true;

             }
         }
    }

    @Override
    public T fjern(){
        Node peker = start;
        T data;

        if (peker.neste == null){
            throw new UgyldigListeIndeks(0);
        }
        /*Det itereres til nest siste element, henter ut dataen til siste element og fjerner den*/
        else{
            for (int i = 0; i < stoerrelse() - 1; i++){
                peker = peker.neste;
            }

            data = peker.neste.data;
            peker.neste = null;
            return data;
        }
    }

    @Override
    public void sett(int pos, T x){
        throw new UnsupportedOperationException();

    }

    @Override
    public void leggTil(int pos, T x){
        throw new UnsupportedOperationException();
    }
}
