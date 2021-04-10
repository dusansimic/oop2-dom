package domaci02;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
 * Dat je tok osoba u vidu metoda Osobe.osobeStream(). Pomocu datog metoda,
 * lambda izraza i operacija nad tokovima podataka implementirati sledece metode:
 * 
 * svi() - Stampa id, ime i prezime za sve osobe
 * 
 * punoDece() - Stampa ime i prezime svih osoba sa vise od dvoje dece
 *
 * istoMesto() - Stampa, sortirano po imenu mesta, podatke za sve osobe koje zive
 *      u istom mestu u kojem su rodjeni, na sledeci nacin:
 *      Ime Prezime (Mesto)
 * 
 * bogateBezDece() - Stampa podatke o zenama sa platom preko 75.000 koje nemaju
 *      dece, sortirano opadajuce po plati, na sledeci nacin:
 *      Ime Prezime (Primanja)
 * 
 * rodjendani() - Stampa podatke o osobama koje slave rodjendan ovog meseca.
 *      Stampati dan i mesec, ime i prezime, kao i koliko godina osoba puni, na
 *      sledeci nacin:
 *      DD. MM. Ime Prezime (Puni)
 * 
 * odrasli(String prezime) - Stampa ukupan broj osoba sa datim prezimenom.
 * 
 * ukupnoDece() - Stampa ukupan broj dece svih osoba.
 * 
 * zaPaketice() - Stampa imena, prezimena i starost dece koja su dobila paketice
 *      2010. godine. Uslov za dobijanje paketica je da dete ne bude starije od
 *      10 godina.
 * 
 * imenaPenzionera() - Stampa sva razlicita imena penzionera, sortirana abecedno.
 *      Penzioneri su osobe koje imaju preko 65 godina.
 * 
 * procenat() - Stampa procenat muskih osoba, ukljucujuci i decu.
 * 
 * trecaPoRedu() - Stampa osobu koja je treca po broju muske dece.
 * 
 * najbogatiji(String grad) - Stampa ime (bez prezimena), visinu primanja i mesto
 *      rodjenja osobe sa najvecim primanjima za zadato mesto stanovanja.
 * 
 * josBogatiji(String grad) - Stampa podatke osoba koje imaju veca primanja od
 *      najbogatije osobe iz zadatog mesta, na sledeci nacin:
 *      Ime (primanja) Mesto stanovanja
 * 
 */
public class OsobeProgram {

	public static void main(String[] args) {
//		svi();
//		punoDece();
//        istoMesto();
//		bogateBezDece();
//		rodjendani();
//        odrasli("Goranović");
//		ukupnoDece();
//        zaPaketice();
//		imenaPenzionera();
//		procenat();
//		procenatJednostavnije();
//		procenatJosJednostavnije();
//		trecaPoRedu();
//		najbogatiji("Novi Sad");
//		josBogatiji("Novi Sad");
	}

	private static void svi() {
		Osobe.osobeStream(5000)
			.forEach(System.out::println);
	}

	private static void punoDece() {
		Osobe.osobeStream(5000)
			.filter(o -> o.getDeca().size() > 2)
			.map(o -> o.getIme() + " " + o.getPrezime())
			.forEach(System.out::println);
	}

	private static void istoMesto() {
		Osobe.osobeStream(5000)
			.filter(o -> o.getMestoRodjenja().equals(o.getMestoStanovanja()))
			.sorted((o1, o2) -> o1.getMestoRodjenja().compareTo(o2.getMestoRodjenja()))
			.map(o -> String.format("%s %s (%s)", o.getIme(), o.getPrezime(), o.getMestoStanovanja()))
			.forEach(System.out::println);
	}

	private static void bogateBezDece() {
		Osobe.osobeStream(5000)
			.filter(o -> o.getPol() == Osoba.Pol.ZENSKI && o.getDeca().size() == 0 && o.getPrimanja() > 75000)
			.sorted((o1, o2) -> o2.getPrimanja() - o1.getPrimanja())
			.map(o -> String.format("%s %s (%d)", o.getIme(), o.getPrezime(), o.getPrimanja()))
			.forEach(System.out::println);
	}

	private static void rodjendani() {
		Osobe.osobeStream(5000)
			.filter(o -> o.getDatumRodjenja().getMonth() == java.time.LocalDate.now().getMonth())
			.map(o -> String.format(
				"%d. %d. %s %s (%d)",
				o.getDatumRodjenja().getDayOfMonth(),
				o.getDatumRodjenja().getMonthValue(),
				o.getIme(),
				o.getPrezime(),
				LocalDate.now().getYear() - o.getDatumRodjenja().getYear()
			))
			.forEach(System.out::println);
	}

	private static void odrasli(String prezime) {
		System.out.println(
			Osobe.osobeStream(5000)
				.filter(o -> o.getPrezime().equals(prezime))
				.count()
		);
	}

	private static void ukupnoDece() {
		System.out.println(
			Osobe.osobeStream(5000)
				.map(o -> o.getDeca().size())
				.reduce((acum, br) -> acum + br)
				.get()
		);
	}

	private static void zaPaketice() {
		Osobe.osobeStream(5000)
			.flatMap(o -> o.getDeca().stream())
			.filter(o -> 2010 - o.getDatumRodjenja().getYear() <= 10)
			.forEach(System.out::println);
	}

	private static void imenaPenzionera() {
		Osobe.osobeStream(5000)
			.filter(o -> LocalDate.now().getYear() - o.getDatumRodjenja().getYear() > 65)
			.map(o -> o.getIme())
			.distinct()
			.sorted()
			.forEach(System.out::println);
	}

	static class Pair<T, U> {
		T first;
		U second;

		public Pair(T first, U second) {
			this.first = first;
			this.second = second;
		}

		public T getFirst() {
			return first;
		}

		public U getSecond() {
			return second;
		}

		@Override
		public String toString() {
			return "Pair{" +
					"first=" + first +
					", second=" + second +
					'}';
		}
	}

	/**
	 * procenat ce racunati na malo komplikovaniji nacin. Koristeci klasu pair, ova funkcija skladisti broj muskaraca
	 * i ukupan broj osoba za svaku osobu (deca + ta osoba). Pomocu reduce operacije se akumuliraju ti brojevi
	 * (muskaraca i ukupan broj osoba) i na kraju se racuna procenat muskaraca. Ovime se ne akumulira greska kao u
	 * drugom primeru i time je mnogo tacniji procenat.
	 */
	private static void procenat() {
		Pair<Integer, Integer> par = Osobe.osobeStream(5000)
			.map(o -> new Pair<Integer, Integer>(
					(int) o.getDeca().stream().filter(
						d -> d.getPol() == Osoba.Pol.MUSKI
					).count() + (o.getPol() == Osoba.Pol.MUSKI ? 1 : 0),
					o.getDeca().size() + 1
			))
			.reduce((accum, p) -> new Pair<Integer, Integer>(accum.first + p.first, accum.second + p.second))
			.get();

		System.out.println(String.format("%f%%", par.first.floatValue() / par.second.floatValue() * 100));
	}

	/**
	 * procenatJednostavnije ce odmah sracunati procenat muskaraca kod svake osobe (deca + ta osoba) i na kraju ce
	 * koristeci reduce da izracuna ukupan procenat muskaraca. Ovo je manje precizno jer se gube podaci prilikom
	 * naknadnog racunanja proseka jer. Akumuliranjem te male greske izracunati procenat se pomera od stvarnog procenta
	 * za 1-2%.
	 */
	private static void procenatJednostavnije() {
		System.out.println(
			String.format("%f%%", Osobe.osobeStream(5000)
				.map(o ->
					((int) o.getDeca().stream().filter(d -> d.getPol() == Osoba.Pol.MUSKI).count() + (o.getPol() == Osoba.Pol.MUSKI ? 1 : 0)) * 1.0 / (o.getDeca().size() + 1)
				)
				.reduce((accum, p) -> (accum + p) / 2.0)
				.get() * 100)
		);
	}

	/**
	 * procenatJosJednostavnije je kao sto i samo ime kaze jos jednostaviji nacin da se izracuna procenat. Ovde se
	 * koristi flatMap da bi se deca poredjala pored roditleja u streamu i da bi se mogli sracunati zajedno. Posle toga
	 * se mapiraju podaci u celobrojne vrednosti 1 ili 0 u zavisnosti da li je osoba musko ili zensko. Tada se koristi
	 * getAverage metoda koja ce izvuci procenat jedinica, tj. muskaraca.
	 */
	private static void procenatJosJednostavnije() {
		System.out.println(
			String.format("%f%%", Osobe.osobeStream(5000)
				.flatMap(o -> Stream.concat(Stream.of(o), o.getDeca().stream()))
				.mapToInt(o -> o.getPol() == Osoba.Pol.MUSKI ? 1 : 0)
				.summaryStatistics()
				.getAverage()  * 100
			)
		);
	}

	private static int brMuskeDece(Osoba o) {
		return (int) o.getDeca().stream().filter(d -> d.getPol() == Osoba.Pol.MUSKI).count();
	}

	private static void trecaPoRedu() {
		System.out.println(
			Osobe.osobeStream(5000)
				.sorted((o1, o2) -> brMuskeDece(o1) - brMuskeDece(o2))
				.skip(2)
				.findFirst()
				.get()
		);
	}

	private static void najbogatiji(String grad) {
		Osoba osoba = Osobe.osobeStream(5000)
			.filter(o -> o.getMestoStanovanja().equals(grad))
			.max((o1, o2) -> o1.getPrimanja() - o2.getPrimanja())
			.get();

		System.out.println(String.format("%s %d %s", osoba.getIme(), osoba.getPrimanja(), osoba.getMestoRodjenja()));
	}

	private static void josBogatiji(String grad) {
	    Supplier<Stream<Osoba>> osobe = () -> Osobe.osobeStream(5000);
		Osoba max = osobe.get().filter(o -> o.getMestoStanovanja().equals(grad)).max((o1, o2) -> o1.getPrimanja() - o2.getPrimanja()).get();
	    osobe.get().filter(o -> o.getPrimanja() > max.getPrimanja()).map(o -> String.format("%s (%d) %s", o.getIme(), o.getPrimanja(), o.getMestoStanovanja())).forEach(System.out::println);
	}
}
