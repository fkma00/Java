import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Beholder {
ArrayList<HashMap<String,SubSekvens>> maps;
private Lock laas = new ReentrantLock();
private Condition ikkeTom = laas.newCondition();

/*ALLE metoder som blir benyttet av traader maa laases med en Lock 
og aapnes til slutt naar metoden er ferdig aa kjore */

/*Laasing av metoder skjer i monitor, har brukt Beholderen som monitor for aa
enkelt kunne referere tils samme beholder i hovedprogrammet*/

    public Beholder() {
        maps = new ArrayList<HashMap<String,SubSekvens>>();
    }

    public void settInniBeholder(HashMap<String,SubSekvens> map) {
        laas.lock();
        //Legger gitt HashMap inn i beholderen
        try {maps.add(map);}
        catch(Exception e){}
        finally{laas.unlock();}
        
    }

    public HashMap<String,SubSekvens> hentUtfraBeholder() {
        //System.out.println(maps.size() + " storrelse");

        laas.lock();
        try {
            //Hvis beholderen er tom venter vi til filleser traaden legger inn et hashmap
            //for vi henter ut for å flette sammen
            while(maps.size() == 0) {
                ikkeTom.await();
                break;
            }
            //Henter og returnerer det forste Hashmappet i beholderen
            return maps.remove(0);
        }
        catch(Exception e) {
            System.out.println("Fletteren ble avbrutt da vi hentet en HashMap fra beholder");}
        finally{laas.unlock();}
        return null;
    }

    public int beholderStoerrelse() {
        return maps.size();
    }

    public static HashMap<String,SubSekvens> flett(HashMap<String,SubSekvens> map1,
    HashMap<String,SubSekvens> map2) {
        //Iterer gjennom alle noklene i hashmappet
        for (String nokkel : map1.keySet()) {

            // if(map2 == null || nokkel == null) {
            //     System.out.println("gdfgdfgdg" + map2 + nokkel);
            // }

            /*Om map2 har samme nokkel som map1 henter vi antall ganger det forekommer i map1
            og oeker antallet med antall ganger det forekommer i map2*/
            if (map2.containsKey(nokkel)) {
                map1.get(nokkel).oekAntall(map2.get(nokkel).hentantallSubSekvens());
            }
        }

        for (String nokkel : map2.keySet()) {
            /*Hvis map1 ikke inneholer en nokkel som map2 har, legger vi det inn i map1*/
            if(!(map1.containsKey(nokkel))) {
                map1.put(nokkel, map2.get(nokkel));
            }
        }
        return map1;
    }

    /*Metode for filleseren til å legge inn et nytt Hashmap basert på dataen fra filene*/
    public void leggTilHashMap(HashMap<String,SubSekvens> hashMap) {
        laas.lock();
        try {
            maps.add(hashMap);
            //System.out.println("Beholder storrelse " + maps.size());
            //Signaliserer til Condition ikkeTom om at størrelsen på beholderen nå ikke er 0
            ikkeTom.signalAll();
        }
        catch(Exception e) {System.out.println("Traaden ble avbrutt når den la til en Hashmap");}
        finally {laas.unlock();}
    }
}
