public class Summary {
    private String nazivPredmeta;
    private String imePredavaca;
    private Cas.Tip tipCasa;
    private String ucionica;

    public Summary(String nazivPredmeta, String imePredavaca, Cas.Tip tipCasa, String ucionica) {
        this.nazivPredmeta = nazivPredmeta;
        this.imePredavaca = imePredavaca;
        this.tipCasa = tipCasa;
        this.ucionica = ucionica;
    }

    public String getNazivPredmeta() {
        return nazivPredmeta;
    }

    public String getImePredavaca() {
        return imePredavaca;
    }

    public Cas.Tip getTipCasa() {
    	return tipCasa;
    }

    public String getUcionica() {
        return ucionica;
    }

    @Override
    public String toString() {
        return nazivPredmeta + ", " + imePredavaca + ", " + ucionica;
    }
}
