public enum Stanje {
    CALENDAR("BEGIN:VCALENDAR"),
    CALENDAR_END("END:VCALENDAR"),
    EVENT("BEGIN:VEVENT"),
    EVENT_END("END:VEVENT");

    private String kw;

    Stanje(String kw) {
        this.kw = kw;
    }

    public String getKw() {
        return kw;
    }


    @Override
    public String toString() {
        return kw;
    }
}
