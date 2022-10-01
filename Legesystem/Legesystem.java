    import java.util.*;
    import java.io.*;


    public class Legesystem {
        Lenkeliste<Pasient> pasienter;
        Lenkeliste<Legemiddel> legemidler;
        Lenkeliste<Lege> leger;
        SortertLenkeliste<String> sortertLegenavn;
        Lenkeliste<Resept> resepter;

        public Legesystem(){
            pasienter = new Lenkeliste<>();
            legemidler = new Lenkeliste<>();
            leger = new Lenkeliste<>();
            sortertLegenavn = new SortertLenkeliste<>();
            resepter = new Lenkeliste<>();
        }

        public static void main(String[] args) throws UgyldigInfo {
            Legesystem sys = new Legesystem();
            String input = "";
            boolean avslutt = false;
            try{
                sys.lesFil("Eksempelfil.txt");
                sys.info();
            } catch(Exception e){}

            while(!avslutt){
                System.out.println("---HOVEDMENY---\nTast inn F for aa faa en fullstendig oversikt over pasienter, leger, legemidler og resepter." +
                        "\nTast inn O for aa opprette og legge til nye elementer i systemet.\nTast inn B for aa bruke en gitt resept fra listen til en pasient" +
                        "\nTast inn S for aa skrive ut forskjellige former for statistikk\nTast inn D for aa skrive alle data til fil\nTast inn Q for aa avslutte programmet");


                Scanner userInput = new Scanner(System.in);
                input = userInput.nextLine().toUpperCase();

                if(input.equals("F")){
                    sys.Oversikt();
                }

                else if(input.equals("O")) {
                    sys.leggTilElementer();
                }

                else if(input.equals("B")){
                    sys.brukResept();
                }

                else if(input.equals("S")){
                    sys.skrivUtStatistikk();
                }

                else if(input.equals("D")){
                    sys.opprettFil();
                }

                else if(input.equals("Q")){
                    avslutt = true;
                }

                else{
                    System.out.println("Ugyldig kommando");
                }

            }

        }

        public void lesFil(String filnavn) throws UgyldigInfo, NumberFormatException, Exception {

                //lager en scanner, for aa skanne gjennom filen
                File tekstfil = new File(filnavn);
                Scanner fil = new Scanner(tekstfil);
                String linje = fil.nextLine();

                while (fil.hasNextLine()) {
                if(linje.contains("Pasienter")){
                    linje = fil.nextLine();
                    while(!linje.contains("#")){
                        String[] data = linje.split(",");
                        if (data.length == 2 && !data[0].equals("") && data[1].length() == 11) {
                            //lager en pasient med dataene navn=data[0] og fodselsnummer = data[1]
                            Pasient pasient = new Pasient(data[0], data[1]);
                            pasienter.leggTil(pasient);
                        } else {
                            System.out.println(linje);
                            throw new UgyldigInfo(data);
                        }
                    linje = fil.nextLine();
                    }
                }

                if(linje.contains("Legemidler")){
                    linje = fil.nextLine();
                    while(!linje.contains("#")){
                        String[] data = linje.split(",");
                        int pris = 0;
                        //sjekker at det finnes et navn paa legemiddelet
                        if (!data[0].equals("")) {
                            //if-tester finner ut hvilken type legemiddel
                            if (data[1].equals("narkotisk") && data.length == 5) {
                                pris = (int) Math.round(Double.parseDouble(data[2]));
                                Narkotisk narkotisk = new Narkotisk(data[0], pris, Double.parseDouble(data[3]), Integer.parseInt(data[4]));
                                legemidler.leggTil(narkotisk);
                            } else if (data[1].equals("vanedannende") && data.length == 5) {
                                pris = (int) Math.round(Double.parseDouble(data[2]));
                                Vanedannende vanedannende = new Vanedannende(data[0], pris, Double.parseDouble(data[3]), Integer.parseInt(data[4]));
                                legemidler.leggTil(vanedannende);
                            } else if (data[1].equals("vanlig") && data.length == 4) {
                                pris = (int) Math.round(Double.parseDouble(data[2]));
                                Vanlige vanlig = new Vanlige(data[0], pris, Double.parseDouble(data[3]));
                                legemidler.leggTil(vanlig);
                            }
                        }
                        //gaar til neste legemiddel
                        linje = fil.nextLine();
                    }
                }

                //Leger
                if(linje.contains("Leger")){
                    linje = fil.nextLine();
                    while(!linje.contains("#")){
                        String[] data = linje.split(",");
                        //if-test sjekker at det finnes et navn og en kontrollId
                        if (!data[0].equals("") && data.length == 2) {
                            //dersom kontrollId finnes (ikke er 0) opprettes en spesialist
                            try {
                                if (Integer.parseInt(data[1]) > 0) {
                                    Spesialist spesialist = new Spesialist(data[0], data[1]);
                                    sortertLegenavn.leggTil(spesialist.navn);
                                    leger.leggTil(spesialist);
                                } else {
                                    Lege lege = new Lege(data[0]);
                                    sortertLegenavn.leggTil(lege.navn);
                                    leger.leggTil(lege);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(e);
                            }

                        } else {
                            throw new UgyldigInfo(data);
                        }
                        linje = fil.nextLine();
                    }
                }

                if(linje.contains("Resepter")) {
                    Legemiddel legemiddel;
                    Lege lege;
                    Pasient pasient;
                    Resept resept;
                    String type;
                    while(fil.hasNextLine()){
                        linje = fil.nextLine();
                        //opretter objekter for aa kunne starte while-lokker
                        legemiddel = null;
                        lege = null;
                        pasient = null;
                        resept = null;
                        type = null;
                        String[] data = linje.split(",");

                        //legger input data'en som er felles for alle resepter korrekt
                        int legemiddelnummer = Integer.parseInt(data[0]);
                        String legenavn = data[1];
                        int pasientId = Integer.parseInt(data[2]);
                        type = data[3];

                        //finner korrekt legemiddel og lagrer legemiddelobjektet i variabel "legemiddel"
                        if (legemiddelnummer <= legemidler.stoerrelse()){
                            for (Legemiddel legemiddel1 : legemidler) {
                                if (legemiddel1.hentId() == legemiddelnummer) {
                                    legemiddel = legemiddel1;
                                }
                            }
                        }

                        //finner korrekt lege og lagrer legeobjektet i variabel "lege"
                        for (Lege lege1 : leger) {
                            if (lege1.hentNavn().equals(legenavn)) {
                                lege = lege1;
                            }
                        }

                        //finner korrekt pasient ved hjelp av id, og lagrer pasientobjektet i variabel "pasient"
                        for (Pasient pasient1 : pasienter) {
                            if (pasient1.pasientId == pasientId) {
                                pasient = pasient1;
                            }
                        }

                        if(legemiddel != null && lege != null && pasient != null){
                            if (type.equals("p") && data.length == 4) {
                                try {
                                    resept = lege.skrivPResept(legemiddel, pasient);
                                    resepter.leggTil(resept);
                                    pasient.resepter.leggTil(resept);
                                } catch (UlovligUtskrift e){}

                            }
                            else if (data.length == 5) {
                                int reit = Integer.parseInt(data[4]);
                                if (type.equals("hvit")) {
                                    try {
                                        resept = lege.skrivHvitResept(legemiddel, pasient, reit);
                                        resepter.leggTil(resept);
                                        pasient.resepter.leggTil(resept);
                                    }
                                    catch (UlovligUtskrift e){}
                                }
                                else if (type.equals("blaa")) {
                                    try {
                                        resept = lege.skrivBlaaResept(legemiddel, pasient, reit);
                                        resepter.leggTil(resept);
                                        pasient.resepter.leggTil(resept);
                                    }
                                    catch (UlovligUtskrift e){}

                                }
                                else if (type.equals("militaer") || type.equals("millitaer")) {
                                    try {
                                        resept = lege.skrivMilitaerResept(legemiddel, pasient, reit);
                                        resepter.leggTil(resept);
                                        pasient.resepter.leggTil(resept);
                                    }
                                    catch (UlovligUtskrift e){}
                                }
                            }
                        }

                    }
                }
            }
            fil.close();
        }

        private void Oversikt(){
            System.out.println("Leger:");
            System.out.println(leger);
            System.out.println("\nLegemidler: ");
            System.out.println(legemidler);
            System.out.println("\nPasienter:");
            System.out.println(pasienter);
            System.out.println("\nResepter:");
            System.out.println(resepter);

        }

        private void leggTilElementer() {
            String objektType;
            System.out.println("Hvilket objekt vil du opprette?: L - lege, P - pasient, LM - legemiddel, R - resept: ");

            Scanner nyObjekt = new Scanner(System.in);
            objektType = nyObjekt.nextLine().toUpperCase();
            if (objektType.equals("L")) {
                leggTilLege();
            }

            else if (objektType.equals("P")) {
                leggTilPasient();
            }

            else if(objektType.equals("LM")){
                leggTilLegemiddel();


            }
            else if(objektType.equals("R")){
                leggTilResept();
            }

            else{
                System.out.println("Ugyldig input, vennligst proev paa nytt");
                leggTilElementer();
                }
            }

        private void brukResept(){
            boolean gyldigPasient = false;
            boolean gyldigResept = false;
            int pasientId = 0;
            int reseptId = 0;
            Resept resept1 = null;
            System.out.println("\nTast inn ID-en til pasienten du vil se resepter for");


            for(Pasient pasient : pasienter){
                System.out.println("ID: " + pasient.pasientId + "\nNavn: " + pasient.navn + "\n");
            }
            while (!gyldigPasient){
                Scanner pasientInput = new Scanner(System.in);
                String valgtPasient = pasientInput.nextLine();
                try{
                    pasientId = Integer.parseInt(valgtPasient);
                }
                catch(NumberFormatException e){
                    System.out.println("Du maa skrive et tall");
                }
                if (pasientId > pasienter.stoerrelse() || pasientId < 1){
                    System.out.println("ID-en oppgitt er ikke gyldig, proev paa nytt");
                }
                else{
                    gyldigPasient = true;
                }
            }

            for(Pasient pasient : pasienter){
                if(pasientId == pasient.pasientId) {

                    if (pasient.resepter.stoerrelse() == 0) {
                        System.out.println("Pasienten har ingen resepter");
                    } else {
                        System.out.println("Tast inn ID-en til resepten du vil bruke");
                        for (Resept resept : pasient.resepter) {
                            System.out.println("ID: " + resept.hentId() + "\nLegemiddel: " + resept.hentLegemiddel() + "\n");
                        }

                        while (!gyldigResept) {
                            Scanner reseptInput = new Scanner(System.in);
                            String valgtResept = reseptInput.nextLine();
                            try {
                                reseptId = Integer.parseInt(valgtResept);
                            } catch (NumberFormatException e) {
                                System.out.println("Du maa skrive et tall");
                            }
                            for (Resept resept : pasient.resepter) {
                                if (reseptId == resept.hentId()) {
                                    resept1 = resept;
                                    gyldigResept = true;
                                }
                            }

                            if(!gyldigResept || reseptId > resepter.stoerrelse() || reseptId < 1){
                                System.out.println("ID-en oppgitt er ikke gyldig, proev paa nytt");
                            }
                        }

                        if (resept1.bruk()){
                            resept1.reit--;
                            System.out.println("Brukte resept paa " + resept1.hentLegemiddel() + ". Antall gjenvaerende reit: " + resept1.hentReit());
                        }
                        else{
                            System.out.println("Kunne ikke bruke resept paa " + resept1.hentLegemiddel() + "(ingen gjenvaerende reit)");
                        }
                    }
                }
            }
        }

        private void skrivUtStatistikk() {
            System.out.println("---UNDERMENY---");
            System.out.println("Tast inn V for aa se antall utskrevne resepter paa vanedannende legemidler\n" +
                    "Tast inn N for aa se antall utskrevne resepter paa narkotiske legemidler\n" +
                    "Tast inn S for aa se statistikk om mulig misbruk av narkotika\n" +
                    "Tast inn Q for aa avslutte undermenyen");
            Scanner input = new Scanner(System.in);
            String valg = input.nextLine().toUpperCase();
            boolean avslutt = false;

            while(!avslutt){
                int teller = 0;
                if (valg.equals("V")){
                    for(Resept resept : resepter){
                        if (resept.legemiddel instanceof Vanedannende){
                            teller++;
                        }
                    }
                    System.out.println("Antall utskrevne resepter med vanedannende legemidler: " + teller + "\n");
                    avslutt = true;
                }
                else if (valg.equals("N")){
                    for(Resept resept : resepter){
                        if (resept.legemiddel instanceof Narkotisk){
                            teller++;
                        }
                    }
                    System.out.println("Antall utskrevne resepter med narkotiske legemidler: " + teller + "\n");
                    avslutt = true;
                }
                else if (valg.equals("S")){
                    misbrukAvNmidlerStatistikk();
                    avslutt = true;
                }
                else if (valg.equals("Q")){
                    avslutt = true;
                }
                if(!avslutt){
                    System.out.println("Ugyldig input\n");
                    skrivUtStatistikk();
                }
            }
        }

        private void misbrukAvNmidlerStatistikk(){
            System.out.println("Her faar du tre valg:\n " +
                    "Tast inn L for aa faa navnet til alle leger som har skrevet ut narkotiske legemidler\n " +
                    "Tast inn P for aa faa navnet til alle pasienter med resepter som har gyldige narkotiske legemidler\n" +
                    " Tast inn Q for aa gaa tilbake til statistikk menyen");

            Scanner input = new Scanner(System.in);
            String valg = "";
            int teller = 0;
            boolean gyldigInput = false;

            while (!gyldigInput){
                valg = input.nextLine().toUpperCase();
                if (valg.equals("L") || valg.equals("P") || valg.equals("Q")){
                    gyldigInput = true;
                }
                else {
                    System.out.println("Ugyldig input, vennligst proev paa nytt\n");
                    System.out.println("Her faar du tre valg:\n " +
                            "Tast inn L for aa faa navnet til alle leger som har skrevet ut narkotiske legemidler\n " +
                            "Tast inn P for aa faa navnet til alle pasienter med resepter som har gyldige narkotiske legemidler\n" +
                            " Tast inn Q for aa gaa tilbake til statistikk menyen");
                }
            }

            if (valg.equals("L")){
                String legerMedNarkotiskeLegemidler = "";
                for(String legenavn : sortertLegenavn){
                    for(Lege lege : leger){
                        if (lege.hentNavn().equals(legenavn) && lege.nmidler > 0){
                            legerMedNarkotiskeLegemidler += lege.hentNavn() + " har skrevet ut " + lege.nmidler + " narkotiske legemidler\n";
                        }
                    }
                }
                System.out.println(legerMedNarkotiskeLegemidler);
            }

            else if (valg.equals("P")){
                String pasienterMedNarkotiskeLegemidler = "";
                for(Pasient pasient : pasienter){
                    for(Resept resept : pasient.resepter){
                        if (resept.legemiddel instanceof Narkotisk && resept.bruk()){
                            teller++;
                        }
                    }
                    if (teller > 0){
                        pasienterMedNarkotiskeLegemidler += pasient.navn + " har " + teller + " narkotiske legemidler registrert til navnet sitt\n";
                    }
                    teller = 0;
                }
                System.out.println(pasienterMedNarkotiskeLegemidler);
            }
            else if (valg.equals("Q")){
                skrivUtStatistikk();
            }
        }

        private Pasient sjekkPasientNavn(String pasientNavn){
            Pasient pasient = null;
            for (Pasient pasient1 : pasienter){
                if (pasient1.navn.equals(pasientNavn)){
                    return pasient1;
                }
            }

            System.out.println("Navnet oppgitt finnes ikke i systemet.");
            Scanner sjekk = new Scanner(System.in);
            String nyPasient = "";
            boolean gyldigInput = false;

            while (!gyldigInput) {
                System.out.println("Vil du opprette en ny pasient? JA/NEI");
                nyPasient = sjekk.nextLine().toUpperCase();

                if(nyPasient.equals("JA") || nyPasient.equals("NEI")){
                    gyldigInput = true;
                }
                else{
                    System.out.println("Ugyldig input");
                }
            }

            if (nyPasient.equals("JA")){
                pasient = leggTilPasient();
            }

            if (nyPasient.equals("NEI")){
                System.out.println("Vennligst skriv inn navnet paa nytt");
                String navn = sjekk.nextLine();
                pasient = sjekkPasientNavn(navn);
            }
            return pasient;
        }

        private Lege sjekkLegenavn(String legenavn){
            Lege lege = null;
            for (Lege lege1 : leger){
                if (lege1.hentNavn().equals(legenavn)){
                    return lege1;
                }
            }

            System.out.println("Navnet oppgitt finnes ikke i systemet.");
            Scanner sjekk = new Scanner(System.in);
            String nyLege = "";
            boolean gyldigInput = false;

            while (!gyldigInput) {
                System.out.println("Vil du opprette en ny lege? JA/NEI");
                nyLege = sjekk.nextLine().toUpperCase();

                if(nyLege.equals("JA") || nyLege.equals("NEI")){
                    gyldigInput = true;
                }
                else{
                    System.out.println("Ugyldig input");
                }
            }
            if (nyLege.equals("JA")){
                lege = leggTilLege();
            }

            if (nyLege.equals("NEI")){
                System.out.println("Vennligst skriv inn navnet paa nytt");
                String navn = sjekk.nextLine();
                lege = sjekkLegenavn(navn);
            }
            return lege;
        }

        private Legemiddel sjekkLegemiddel(String legemiddelnavn){
            Legemiddel legemiddel = null;
            for (Legemiddel legemiddel1 : legemidler){
                if (legemiddel1.hentNavn().equals(legemiddelnavn)){
                    return legemiddel1;
                }
            }
            System.out.println("Navnet oppgitt finnes ikke i systemet.");
            Scanner sjekk = new Scanner(System.in);
            String nyttLegemiddel = "";
            boolean gyldigInput = false;
            while (!gyldigInput){

                System.out.println("Vil du opprette et nytt legemiddel? JA/NEI");
                nyttLegemiddel = sjekk.nextLine().toUpperCase();

                if(nyttLegemiddel.equals("JA") || nyttLegemiddel.equals("NEI")){
                    gyldigInput = true;
                }
                else{
                    System.out.println("Ugyldig input");
                }
            }

            if (nyttLegemiddel.equals("JA")){
                legemiddel = leggTilLegemiddel();
            }

            if (nyttLegemiddel.equals("NEI")){
                System.out.println("Vennligst skriv inn navnet paa nytt");
                String navn = sjekk.nextLine();
                legemiddel = sjekkLegemiddel(navn);
            }
            return legemiddel;
        }

        private Pasient leggTilPasient(){
            System.out.println("\n--- OPPRETTELSE AV PASIENT ---\n");
            Scanner pasientObjekt = new Scanner(System.in);
            String navn = "";
            String fnr = "";
            boolean gyldigInput = false;
            boolean gyldigNavn = false;
            Long fnrInt = 0L;

            while(!gyldigNavn){
                System.out.println("Hva er pasientens navn?: ");
                navn = pasientObjekt.nextLine();
                if (!navn.isEmpty()){
                    gyldigNavn = true;
                }
                else{
                    System.out.println("Navn kan ikke vaere tom\n");
                }
            }

            while(!gyldigInput){
                System.out.println("Hva er pasientens foedselsnummer? (11 siffer) : ");
                fnr = pasientObjekt.nextLine();

                if (fnr.length() == 11){
                    try{
                        fnrInt.parseLong(fnr);
                        gyldigInput = true;
                    }

                    catch(NumberFormatException e){
                        System.out.println("Ugyldig input");
                    }
                }

                else{
                    System.out.println("Ugyldig input");
                }
            }

            Pasient pasient = new Pasient(navn, fnr);
            pasienter.leggTil(pasient);
            return pasient;
        }

        private Lege leggTilLege() {
            System.out.println("\n---  OPPRETTELSE AV LEGE ---\n");
            Scanner legeObjekt = new Scanner(System.in);

            String navn = "";
            String kontrollId = "";
            boolean gyldigNavn = false;

            while(!gyldigNavn){
                System.out.println("Hva er legens navn?: ");
                navn = legeObjekt.nextLine();
                if (!navn.isEmpty()){
                    gyldigNavn = true;
                }
                else{
                    System.out.println("Navn kan ikke vaere tom\n");
                }
            }

            int kontrollIdInt = -1;

            while(kontrollIdInt < 0){
                System.out.println("Hva er legens kontrollId? Hvis legen ikke har kontrollId, tast inn 0: ");
                kontrollId = legeObjekt.nextLine();

                try {
                    kontrollIdInt = Integer.parseInt(kontrollId);

                }
                catch(NumberFormatException e){
                    System.out.println("Ugyldig input");
                }
                if (kontrollIdInt < 0){
                    System.out.println("Tallet oppgitt maa vaere stoerre eller lik 0");
                }
            }

            if (!kontrollId.equals("0")){
                Spesialist spesialist = new Spesialist(navn, kontrollId);
                sortertLegenavn.leggTil(navn);
                leger.leggTil(spesialist);
                return spesialist;
            }
            else {
                Lege lege = new Lege(navn);
                sortertLegenavn.leggTil(navn);
                leger.leggTil(lege);
                return lege;
            }
        }

        private Legemiddel leggTilLegemiddel(){
            Legemiddel legemiddel = null;

            Scanner legemiddelObjekt = new Scanner(System.in);
            boolean gyldigInput = false;
            boolean gyldigPris = false;
            boolean gyldigVirkestoff = false;
            boolean gyldigStyrke = false;
            boolean gyldigNavn = false;

            int prisInt = 0;
            double virkestoffDouble = 0;
            int styrkeInt = 0;
            String navn = "";

            System.out.println("\n--- OPPRETTELSE AV LEGEMIDDEL ---\n");

            while (!gyldigInput){
                System.out.println("Hva slags legemiddel vil du opprette? V - Vanlig, VD - Vanedannende, N - Narkotisk: ");
                String type = legemiddelObjekt.nextLine().toUpperCase();

                if (type.equals("V") || type.equals("VD") || type.equals("N")){
                    while(!gyldigNavn){
                        System.out.println("Hva heter legemiddelet?: ");
                        navn = legemiddelObjekt.nextLine();
                        if (!navn.isEmpty()){
                            gyldigNavn = true;
                        }
                        else{
                            System.out.println("Navn kan ikke vaere tom\n");
                        }
                    }

                    while(!gyldigPris){
                        System.out.println("Hvor mye koster legemiddelet?: ");
                        String pris = legemiddelObjekt.nextLine();
                        try {
                            prisInt = Integer.parseInt(pris);
                            if (prisInt < 0){
                                System.out.println("Prisen kan ikke vaere mindre enn 0");
                            }
                            else{
                                gyldigPris = true;
                            }
                        }
                        catch(NumberFormatException e){
                            System.out.println("Ugyldig input");
                        }
                    }

                    while(!gyldigVirkestoff){
                        System.out.println("Hvor mye virkestoff har legemiddelet?: ");
                        String virkestoff = legemiddelObjekt.nextLine();
                        try {
                            virkestoffDouble = Double.parseDouble(virkestoff);
                            if (virkestoffDouble < 0){
                                System.out.println("Virkestoff maa vaere et tall stoerre enn 0");
                            }
                            else{
                                gyldigVirkestoff = true;
                            }
                        }
                        catch(NumberFormatException e){
                            System.out.println("Ugyldig input");
                        }
                    }
                    if (type.equals("V")){

                        legemiddel = new Vanlige(navn, prisInt, virkestoffDouble);
                        legemidler.leggTil(legemiddel);
                        System.out.println("Legemiddelet ble opprettet med foelgende informasjon\n");
                        System.out.println(legemiddel);
                        gyldigInput = true;
                    }
                    else if (type.equals("VD")){
                        while(!gyldigStyrke){
                            System.out.println("Hva er styrken paa det vanedannende legemiddelet?: ");
                            String styrke = legemiddelObjekt.nextLine();
                            try{
                                styrkeInt = Integer.parseInt(styrke);
                                if (styrkeInt < 0){
                                    System.out.println("Styrken kan ikke vaere mindre enn 0");
                                }
                                else{
                                    gyldigStyrke = true;
                                }
                            }
                            catch(NumberFormatException e){
                                System.out.println("Ugyldig input");
                            }
                        }
                        legemiddel = new Vanedannende(navn, prisInt, virkestoffDouble, styrkeInt);
                        legemidler.leggTil(legemiddel);
                        System.out.println("Legemiddelet ble opprettet med foelgende informasjon\n");
                        System.out.println(legemiddel);
                        gyldigInput = true;
                    }
                    else if (type.equals("N")){
                        while(!gyldigStyrke){
                            System.out.println("Hva er styrken paa det narkotiske legemiddelet?: ");
                            String styrke = legemiddelObjekt.nextLine();
                            try{
                                styrkeInt = Integer.parseInt(styrke);
                                if (styrkeInt < 0){
                                    System.out.println("Styrken kan ikke vaere mindre enn 0");
                                }
                                else{
                                    gyldigStyrke = true;
                                }
                            }
                            catch(NumberFormatException e){
                                System.out.println("Ugyldig input");
                            }
                        }
                        legemiddel = new Narkotisk(navn, prisInt, virkestoffDouble, styrkeInt);
                        legemidler.leggTil(legemiddel);
                        System.out.println("Legemiddelet ble opprettet med foelgende informasjon\n");
                        System.out.println(legemiddel);
                        gyldigInput = true;
                    }
                }
                else{
                    System.out.println("Ugyldig valg av legemiddel, proev paa nytt.");
                }
            }
            return legemiddel;
        }

        private void leggTilResept(){
            System.out.println("\n--- OPPRETTELSE AV RESEPT ---\n");
            Legemiddel legemiddel = null;
            String type = "";
            Resept resept = null;
            String reit = "";
            int reitInt = 0;
            Scanner reseptObjekt = new Scanner(System.in);
            Scanner reseptReit = new Scanner(System.in);

            boolean gyldigType = false;
            boolean gyldigLegemiddel = false;
            boolean gyldigPasient = false;
            boolean gyldigLege = false;
            boolean gyldigReit = false;

            while(!gyldigType){
                System.out.println("Oppgi type resept: P - P-Resept, H - Hvitt Resept, M - Militaer Resept, B - Blaatt Resept");
                type = reseptObjekt.nextLine().toUpperCase();

                if (type.equals("P") || type.equals("H") || type.equals("M") || type.equals("B")){
                    gyldigType = true;
                }
                else{
                    System.out.println("Ugyldig input paa resept type, vennligst proev paa nytt\n");
                }
            }

            System.out.println("Oppgi navnet til legemiddelet som resepten skal inneholde: ");
            String legemiddelNavn = reseptObjekt.nextLine();
            legemiddel = sjekkLegemiddel(legemiddelNavn);

            System.out.println("Oppgi navnet til pasienten: ");
            String pasientNavn = reseptObjekt.nextLine();
            Pasient pasient = sjekkPasientNavn(pasientNavn);

            System.out.println("Oppgi navnet til utskrivende lege: ");
            String legeNavn = reseptObjekt.nextLine();
            Lege lege = sjekkLegenavn(legeNavn);

            if(type.equals("P")){
                try{
                    resept = lege.skrivPResept(legemiddel, pasient);
                } catch(UlovligUtskrift e){System.out.println("Reseptet ble ikke opprettet fordi kontroll-ID er ugyldig");}
            }
            else{
                while (!gyldigReit){
                    System.out.println("Oppgi reit: ");
                    reit = reseptReit.nextLine();
                    try{
                        reitInt = Integer.parseInt(reit);
                        if(reitInt > 0){
                            gyldigReit = true;
                        } else{System.out.println("Reit maa vaere ett tall stoerre enn 0");}
                    }
                    catch(NumberFormatException e){
                        System.out.println("Ugyldig input");
                    }
                }
                if (type.equals("B")){
                    try{
                        resept = lege.skrivBlaaResept(legemiddel, pasient, reitInt);

                    } catch(UlovligUtskrift e){System.out.println("Reseptet ble ikke opprettet fordi kontroll-ID er ugyldig\n");}
                }
                else if(type.equals("M")){
                    try{
                        resept = lege.skrivMilitaerResept(legemiddel, pasient, reitInt);

                    } catch(UlovligUtskrift e){System.out.println("Reseptet ble ikke opprettet fordi kontroll-ID er ugyldig\n");}
                }
                else if(type.equals("H")){
                    try{
                        resept = lege.skrivHvitResept(legemiddel, pasient, reitInt);

                    } catch(UlovligUtskrift e){System.out.println("Reseptet ble ikke opprettet fordi kontroll-ID er ugyldig\n");}
                }
            }
            if (resept != null){
                resepter.leggTil(resept);
                pasient.resepter.leggTil(resept);
                System.out.println("\nResepten ble lagt til med foelgende informasjon\n");
                System.out.println(resept);
            }
        }

        private void opprettFil(){
            Scanner input = new Scanner(System.in);
            boolean gyldigFil = false;
            String filnavn = "";

            while(!gyldigFil){
                System.out.println("Hva vil du kalle filen");
                filnavn = input.nextLine();
                try{
                    File fil = new File(filnavn + ".txt");
                    if (fil.createNewFile()){
                        System.out.println("Fil opprettet: " + fil.getName());
                        gyldigFil = true;
                    }
                    else{
                        System.out.println("Fil eksisterer allerede");
                    }
                }
                catch (IOException e){
                    System.out.println("En feil har oppstaatt");
                    e.printStackTrace();
                }
            }
            try {
                FileWriter skrivTilFil = new FileWriter(filnavn + ".txt");
                skrivTilFil.write("# Pasienter (navn, fnr)");
                for (Pasient pasient : pasienter){
                    skrivTilFil.write("\n" + pasient.navn + "," + pasient.fodselsnummer);
                }
                skrivTilFil.write("\n# Legemidler (navn,type,pris,virkestoff,[styrke])");
                for (Legemiddel legemiddel : legemidler){
                    if (legemiddel instanceof Vanlige){
                        skrivTilFil.write("\n" + legemiddel.hentNavn() + "," + legemiddel.hentType() + "," + legemiddel.hentPris() + "," + legemiddel.hentVirkestoff());
                    }
                    else{
                        skrivTilFil.write("\n" + legemiddel.hentNavn() + "," + legemiddel.hentType() + "," + legemiddel.hentPris() + "," + legemiddel.hentVirkestoff() + "," + legemiddel.hentStyrke());
                    }
                }
                skrivTilFil.write("\n# Leger (navn,kontrollid / 0 hvis vanlig lege)");
                for (Lege lege : leger){
                    skrivTilFil.write("\n" + lege.hentNavn() + "," + lege.hentKontrollID());
                }
                skrivTilFil.write("\n# Resepter (legemiddelNummer,legeNavn,pasientID,type,[reit])");
                for (Resept resept : resepter){
                    if (resept instanceof HvitResept){
                        if (resept instanceof PResept){
                            skrivTilFil.write("\n" + resept.legemiddel.hentId() + "," + resept.utskrivendeLege.hentNavn() + "," + resept.pasient.pasientId + ",p");
                        }
                        else if (resept instanceof MilitaerResept){
                            skrivTilFil.write("\n" + resept.legemiddel.hentId() + "," + resept.utskrivendeLege.hentNavn() + "," + resept.pasient.pasientId + ",militaer," + resept.reit);
                        }
                        else{
                            skrivTilFil.write("\n" + resept.legemiddel.hentId() + "," + resept.utskrivendeLege.hentNavn() + "," + resept.pasient.pasientId + ",hvit," + resept.reit);
                        }
                    }
                    else if (resept instanceof BlaaResept){
                        skrivTilFil.write(resept.legemiddel.hentId() + "," + resept.utskrivendeLege.hentNavn() + "," + resept.pasient.pasientId + ",blaa," + resept.reit);
                    }
                }
                skrivTilFil.close();            }
            catch(IOException e){
                System.out.println("En feil har oppstaatt");
                e.printStackTrace();
            }
        }

        private void info(){
            System.out.println("Dataene som finnes i systemet er som foelger: \n Antall Pasienter:" + pasienter.stoerrelse() + 
            "\n Antall Legemidler: " + legemidler.stoerrelse() + "\n Antall leger: " + leger.stoerrelse() + "\n Antall resepter: " + resepter.stoerrelse());
        }
    }
