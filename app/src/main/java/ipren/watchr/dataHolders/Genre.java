package ipren.watchr.dataHolders;

// Will be deleted when database is set up properly
public class Genre {
    private final int id;
    private final String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
