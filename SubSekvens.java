class SubSekvens {
private String subSekvens;
private int antallSubSekvens;

    public SubSekvens(String sS, int antallSS) {
        subSekvens = sS;
        antallSubSekvens = antallSS;
    }

    public void settSubSekvens() {
        antallSubSekvens++;
    }

    public String hentsubSekvens() {
        return subSekvens;
    }

    public int hentantallSubSekvens() {
        return antallSubSekvens;
    }

    public void oekAntall(int antSubsekvens){
        antallSubSekvens += antSubsekvens;
    }
}

