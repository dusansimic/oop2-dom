import java.time.LocalTime;

public class Cas implements Comparable<Cas> {
	public static enum Tip {
		P("Predavanje"),
		V("Vezbe"),
		PV("Prakticne vezbe"),
		NEPOZNAT("Nepoznat");

		private String v;

		Tip(String v) {
			this.v = v;
		}

		public String getV() {
			return v;
		}
	}
	
	private String naziv;
	private String imePredavaca;
	private Tip tip;
	private LocalTime pocetak;
	private LocalTime kraj;
	private String ucionica;
	
	public Cas(String naziv, String imePredavaca, Cas.Tip tip, LocalTime pocetak, LocalTime kraj, String ucionica) {
		super();
		this.naziv = naziv;
		this.imePredavaca = imePredavaca;
		this.tip = tip;
		this.pocetak = pocetak;
		this.kraj = kraj;
		this.ucionica = ucionica;
	}

	@Override
	public int compareTo(Cas o) {
		return this.pocetak.compareTo(o.pocetak);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imePredavaca == null) ? 0 : imePredavaca.hashCode());
		result = prime * result + ((kraj == null) ? 0 : kraj.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((pocetak == null) ? 0 : pocetak.hashCode());
		result = prime * result + ((tip == null) ? 0 : tip.hashCode());
		result = prime * result + ((ucionica == null) ? 0 : ucionica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cas other = (Cas) obj;
		if (imePredavaca == null) {
			if (other.imePredavaca != null)
				return false;
		} else if (!imePredavaca.equals(other.imePredavaca))
			return false;
		if (kraj == null) {
			if (other.kraj != null)
				return false;
		} else if (!kraj.equals(other.kraj))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (pocetak == null) {
			if (other.pocetak != null)
				return false;
		} else if (!pocetak.equals(other.pocetak))
			return false;
		if (tip != other.tip)
			return false;
		if (ucionica == null) {
			if (other.ucionica != null)
				return false;
		} else if (!ucionica.equals(other.ucionica))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Naziv: " + naziv + "\nPredaje: " + imePredavaca + "\nTip casa: " + tip + "\tPocetak: " + pocetak
				+ "\tKraj: " + kraj + "\tUcionica: " + ucionica;
	}
	
	
}
