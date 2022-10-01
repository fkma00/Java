public class Stabel<T> extends Lenkeliste<T>{
    /*Legger til et nytt element i slutten av lista*/
    public void leggPaa (T x){
        leggTil(x);
    }
    /*Returnerer og fjerner siste element i lista*/
    public T taAv(){
        return fjern(stoerrelse() - 1);
    }
}
