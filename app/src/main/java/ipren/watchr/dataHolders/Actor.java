package ipren.watchr.dataHolders;

public class Actor {
    private final int id; // will probably be needed when saving into DB
    private String name;
    private String pictureLink;

    public Actor(int id, String name, String pictureLink) {
        this.id = id;
        this.name = name;
        this.pictureLink = new StringBuilder().append("https://image.tmdb.org/t/p/original").append(pictureLink).toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureLink() {
        return pictureLink;
    }
}
