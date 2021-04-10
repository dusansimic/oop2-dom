package domaci02;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class Osoba {

	public enum Pol {
		MUSKI, ZENSKI;
	}

	private final String ime;
	private final String prezime;
	private final Pol pol;
	private final LocalDate datumRodjenja;
	private final String mestoRodjenja;
	private final String mestoStanovanja;
	private final int primanja;
	private final List<Osoba> deca;

	public Osoba(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String mestoRodjenja, String mestoStanovanja, int primanja, Osoba... deca) {
		if (ime == null) {
			throw new IllegalArgumentException();
		}
		this.ime = ime;
		if (prezime == null) {
			throw new IllegalArgumentException();
		}
		this.prezime = prezime;
		if (pol == null) {
			throw new IllegalArgumentException();
		}
		this.pol = pol;
		if (datumRodjenja == null) {
			throw new IllegalArgumentException();
		}
		this.datumRodjenja = datumRodjenja;
		if (mestoRodjenja == null) {
			throw new IllegalArgumentException();
		}
		this.mestoRodjenja = mestoRodjenja;
		if (mestoStanovanja == null) {
			throw new IllegalArgumentException();
		}
		this.mestoStanovanja = mestoStanovanja;
		this.primanja = primanja;
		if (deca == null) {
			throw new IllegalArgumentException();
		}
		List<Osoba> d = new ArrayList<>();
		for (Osoba dete : deca) {
			d.add(dete);
		}
		this.deca = Collections.unmodifiableList(d);
	}

	public String getIme() {
		return ime;
	}
	
	public String getPrezime() {
		return prezime;
	}
	
	public Pol getPol() {
		return pol;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}
	
	public String getMestoRodjenja() {
		return mestoRodjenja;
	}

	public String getMestoStanovanja() {
		return mestoStanovanja;
	}

	public int getPrimanja() {
		return primanja;
	}

	public List<Osoba> getDeca() {
		return deca;
	}

	@Override
	public String toString() {
		return String.format("%08X ", System.identityHashCode(this)) + ime + " " + prezime;
	}

	public static String toString(Osoba osoba) {
		StringJoiner joiner = new StringJoiner("|");
		joiner.add(osoba.ime);
		joiner.add(osoba.prezime);
		joiner.add(osoba.pol.toString());
		joiner.add(osoba.datumRodjenja.toString());
		joiner.add(osoba.mestoRodjenja);
		joiner.add(osoba.mestoStanovanja);
		joiner.add(String.valueOf(osoba.primanja));
		joiner.add(String.valueOf(osoba.deca.size()));
		for (Osoba dete : osoba.deca) {
			joiner.add(dete.toString());
		}
		return joiner.toString();
	}

	public static Osoba fromString(String string) {
		// TODO Implementirati
		return null;
	}
}
