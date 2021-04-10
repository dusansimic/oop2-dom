import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Raspored {
    private Map<Dan, SortedSet<Cas>> raspored;

    public Raspored(Map<Dan, SortedSet<Cas>> raspored) {
        this.raspored = raspored;
    }

    public SortedSet<Cas> getDan(Dan dan) {
        return raspored.get(dan);
    }

    public void addCasToDan(Dan dan, Cas cas) {
        if (raspored.get(dan) == null) {
            raspored.put(dan, new TreeSet<>());
        }
        raspored.get(dan).add(cas);
    }
}
