/*
 * U ulaznom fajlu 'raspored.ics' je dat raspored casova druge godine IT smera
 * u iCalendar formatu.
 *
 * Kalendar pocinje linijom 'BEGIN:VCALENDAR' a zavrsava se linijom
 * 'END:VCALENDAR'. Izmedju ove dve linije se nalaze podaci o svim dogadjajima
 * ukljucujuci i podatke o casovima predavanja i vezbi.
 *
 * Svaki cas predavanja i vezbi je predstavljen zasebnim dogadjajem. Svaki
 * dogadjaj pocinje linijom 'BEGIN:VEVENT' a zavrsava sa 'END:VEVENT'. Izmedju
 * ovih linija se nalaze podaci za konkretan dogadjaj, odnosno cas.
 *
 * Podaci o casovima se nalaze u osobinama odgovarajucih dogadjaja i zapisani
 * su na sledeci nacin, svaki u svojoj liniji:
 * OSOBINA;ATRIBUT1=Vrednost1;ATRIBUT2=Vrednost2:VrednostOsobine
 * Dugacke osobine se mogu razdvojiti u vise linija. Pri tome prva linija
 * pocinje normalno dok je svaka sledeca uvucena jednom prazninom. Cela osobina
 * se dobija konkatenacijom linija, naravno ne ukljucujuci vodecu prazninu.
 *
 * Osobine od znacaja za raspored casova su:
 *
 * 1) Vreme pocetka i kraja casa (DTSTART i DTEND) cija vrednost sadrzi datum i
 *    vreme u formatu YYYYMMDDTHHMMSS. Prvih 8 znakova predstavlja datum i moze
 *    se slobodno ignorisati. Poslednjih 6 znakova predstavlja vreme i sadrzi
 *    po dve cifre za sat, minut i sekindu pocetka, odnosno kraja casa. Takodje,
 *    osobina moze sadrzati i atribut sa podatkom o vremenskoj zoni koji se
 *    moze slobodno ignorisati.
 *
 * 2) Pravilo ponavljanja (RRULE) sadrzi, izmedju ostalih podataka, dan u
 *    nedelji kada se cas odrzava. Iako je ovaj podatak moguce izracunati na
 *    osnovu datuma u osobini (DTSTART) lakse je koristiti ovu osobinu. Dan u
 *    nedelji je zapisan kao engleska dvoslovna srkacenica (MO, TU, WE...) kao
 *    vrednost komponente 'BYDAY'.
 *
 * 3) Predmet, nastavnik, tip, sala (SUMMARY) se nalaze zajedno odvojeni
 *    zarezom ispred kojeg se nalazi obrnuta kosa crta.
 *
 * Vise podataka o iCalendar formatu se moze naci na sledecem linku:
 * https://en.wikipedia.org/wiki/ICalendar
 *
 * Napisati aplikaciju koja ucitava podatke iz fajla, i najpre proverava da li
 * su svi podaci u fajlu dobro zadati. Ukoliko za neki od casova ne postoji
 * potrebna osobina ili format osobine nije zapisan na gore naveden nacin,
 * program na ekran ispisuje koja osobina je u pitanju i u kom redu fajla se
 * nalazi.
 *
 * Zatim, program od korisnika u ucitava naziv predmeta i ispisuje na ekran dan
 * u nedelji i vremena pocetka i kraja casa predavanja zadatog predmeta (tip
 * casa predavanja je 'P') kao i salu u kojoj se odrzava (ili online ako se
 * odrzava online), odnosno odgovarajucu poruku ukoliko predmet ne postoji u fajlu.
 *
 * Program ne treba da razlikuje mala i velika slova ni u kom delu svoje
 * funkcionalnosti.
 *
 * Jedan primer casa zapisanog kao dogadjaj u kalendaru:
 *
 * ...
 * BEGIN:VEVENT
 * ...
 * DTSTART;TZID=Europe/Belgrade:20210225T090000
 * DTEND;TZID=Europe/Belgrade:20210225T110000
 * ...
 * RRULE:FREQ=WEEKLY;BYDAY=TH
 * ...
 * SUMMARY:Objektno - orijentisano programiranje 2\, M. Radovanović\, (P)\, online
 * ...
 * END:VEVENT
 * ...
 *
 * Ovaj dogadjaj predstavlja predavanja iz predmeta Objektno-orijentisano
 * programiranje 2, koji se odrzava cetvrtkom od 9 do 11h online.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RasporedProgram {
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Izaberite godinu:");
            for (int i = 0; i < Godina.values().length; i++) {
                System.out.println(String.format("\t%d. %s", i + 1, Godina.values()[i]));
            }
            String izbor = in.readLine();
            Godina godina = Godina.values()[Integer.parseInt(izbor.strip()) - 1];

            // Ucitaj podatke u niz linija
            String[] lines = readGodina(godina);
            if (lines == null) {
                return;
            }


            // Parsiraju se podaci i vraca se raspored
            Raspored raspored = parse(lines);

            System.out.println("Unesite zeljeni dan u nedelji:");
            for (int i = 0; i < Dan.values().length; i++) {
                System.out.println(String.format("\t%d. %s", i + 1, Dan.values()[i]));
            }
            String izabranDan = in.readLine();
            Dan dan = Dan.values()[Integer.parseInt(izabranDan.strip()) - 1];

            for (Cas c : raspored.getDan(dan)) {
                System.out.println("===");
                System.out.println(c);
            }
        } catch (IOException exc) {
            System.err.println("Нешто тргна наопаку!");
            System.err.println(exc.getMessage());
            return;
        }
   }

    private static String[] readFile() {
        return readStream(new InputStreamReader(RasporedProgram.class.getResourceAsStream("raspored.ics")));
    }

    private static String[] readGodina(Godina god) {
        try {
            return readURL(
                    new URL(String.format("https://calendar.google.com/calendar/ical/%s%%40group.calendar.google.com/public/basic.ics", god.getId()))
            );
        } catch (MalformedURLException exc) {
            System.err.println("Нешто тргна наопаку!");
            System.err.println(exc.getMessage());
            return null;
        }
    }

    private static String[] readURL(URL url) {
        try {
            return readStream(new InputStreamReader(url.openStream()));
        } catch (IOException exc) {
            System.err.println("Нешто тргна наопаку!");
            System.err.println(exc.getMessage());
            return null;
        }
    }

    private static String[] readStream(InputStreamReader s) {
        try (BufferedReader in = new BufferedReader(s)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[lines.size()]);
        } catch (IOException exc) {
            System.err.println("Нешто тргна наопаку!");
            System.err.println(exc.getMessage());
            return null;
        }
    }

    private static Raspored parse(String[] lines) {
        Raspored raspored = new Raspored(new HashMap<Dan, SortedSet<Cas>>());
//        Map<Dan, SortedSet<Cas>> raspored = new HashMap<>();
        FancyInteger cursor = new FancyInteger(0);

        for (; cursor.get() < lines.length; cursor.increment()) {
            if (lines[cursor.get()].trim().equals(Stanje.CALENDAR.getKw())) {
                parseCalendar(raspored, lines, cursor);
            }
        }

        return raspored;
    }

    private static void parseCalendar(Raspored raspored, String[] lines, FancyInteger cursor) {
        String line;
        for (; cursor.get() < lines.length && !lines[cursor.get()].trim().equals(Stanje.CALENDAR_END.getKw()); cursor.increment()) {
            line = lines[cursor.get()];

            if (line.trim().equals(Stanje.EVENT.getKw())) {
                parseEvent(raspored, lines, cursor);
            }
        }
    }

    private static void parseEvent(Raspored raspored, String[] lines, FancyInteger cursor) {
        LocalTime start = null;
        LocalTime end = null;
        Summary summary = null;
        Dan dan = null;

        for (; cursor.get() < lines.length && !lines[cursor.get()].trim().equals(Stanje.EVENT_END.getKw()); cursor.increment()) {
            if (lines[cursor.get()].startsWith("DTSTART")) {
                start = parseDateTime(lines, cursor, "DTSTART");
            }

            if (lines[cursor.get()].startsWith("DTEND")) {
                end = parseDateTime(lines, cursor, "DTEND");
            }

            if (lines[cursor.get()].startsWith("RRULE:")) {
                dan = parseDan(lines, cursor);
            }

            if (lines[cursor.get()].startsWith("SUMMARY:")) {
                summary = parseSummary(lines, cursor);
            }
        }

        if (isOneNull(start, end, summary, dan)) {
            System.err.println("Nisu svi podaci ucitani!");
            if (summary != null && summary.getNazivPredmeta() != null) {
                System.err.println("Postoji naziv!");
            }
            System.out.println(String.format("Cursor: %d", cursor.get()));
        }

        Cas c = new Cas(
			summary.getNazivPredmeta(),
			summary.getImePredavaca(),
			summary.getTipCasa(),
			start,
			end,
			summary.getUcionica()
        );

        if (dan != null) {
            raspored.addCasToDan(dan, c);
        }
    }

    private static boolean isOneNull(Object ...data) {
        for (Object d : data) {
            if (d == null) {
                return true;
            }
        }

        return false;
    }

    private static LocalTime parseDateTime(String[] lines, FancyInteger cursor, String prefix) {
        StringBuilder data = new StringBuilder();

        for (;
             cursor.get() < lines.length && (lines[cursor.get()].startsWith(prefix) || lines[cursor.get()].startsWith(" "));
             cursor.increment()) {
            data.append(lines[cursor.get()]);
        }

        Pattern r = Pattern.compile("T(?<sati>\\d{2})(?<minuti>\\d{2})(?<sekunde>\\d{2})");
        Matcher m = r.matcher(data.toString());
        if (m.find()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
            return LocalTime.parse(m.group("sati") + m.group("minuti"), formatter);
        }


        return null;
    }

    private static Dan parseDan(String[] lines, FancyInteger cursor) {
        StringBuilder data = new StringBuilder();

        for (;
             cursor.get() < lines.length && (lines[cursor.get()].startsWith("RRULE:") || lines[cursor.get()].startsWith(" "));
             cursor.increment()) {
            data.append(lines[cursor.get()]);
        }

        Pattern r = Pattern.compile("BYDAY=(?<dan>.*)");
        Matcher m = r.matcher(data.toString());
        if (m.find()) {
            String day = m.group("dan");
            return switch (day) {
                case "MO" -> Dan.MON;
                case "TU" -> Dan.TUE;
                case "WE" -> Dan.WEN;
                case "TH" -> Dan.THU;
                case "FR" -> Dan.FRI;
                case "SA" -> Dan.SAT;
                default -> Dan.SUN;
            };
        }

        return null;
    }

    private static Summary parseSummary(String[] lines, FancyInteger cursor) {
        StringBuilder data = new StringBuilder();

        for (;
             cursor.get() < lines.length && (lines[cursor.get()].startsWith("SUMMARY:") || lines[cursor.get()].startsWith(" "));
             cursor.increment()) {
            data.append(lines[cursor.get()]);
        }

        Pattern r = Pattern.compile(":(?<nazivPredmeta>.*?)\\\\,( )?(?<imePredavaca>.*?)(\\\\,( )?\\((?<tipNastave>.*?)\\))?\\\\,( )?(?<ucionica>.*)");
        Matcher m = r.matcher(data.toString());
        if (m.find()) {
            String nazivPredmeta = m.group("nazivPredmeta").strip();
            String imePredavaca = m.group("imePredavaca");
            String tipNastave = m.group("tipNastave");

            Cas.Tip dobarTipCasa;

            if (tipNastave == null) {
                dobarTipCasa = Cas.Tip.NEPOZNAT;
            } else {
                dobarTipCasa = switch (tipNastave) {
                    case "V" -> Cas.Tip.V;
                    case "PV" -> Cas.Tip.PV;
                    default -> Cas.Tip.P;
                };
            }

            String ucionica = m.group("ucionica");

            return new Summary(nazivPredmeta, imePredavaca, dobarTipCasa, ucionica);
        }

        String nazivPredmeta = data.toString().split(":")[1];
        return new Summary(nazivPredmeta, null, null, null);
    }
}
