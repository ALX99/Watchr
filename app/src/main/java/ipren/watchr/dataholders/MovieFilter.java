package ipren.watchr.dataholders;

/**
 * MovieFilter model containing current filter options.
 * Singleton.
 */
public class MovieFilter {
    private static MovieFilter instance;
    private double rating;
    private String genre;
    private String orderBy;
    private boolean includeWatched;
    private boolean includeWatchLater;
    private boolean includeFavorites;

    private MovieFilter() {
        rating = 1.0;
        genre = "All";
        orderBy = "Popularity";
        includeWatched = true;
        includeWatchLater = true;
        includeFavorites = true;
    }

    public static MovieFilter getInstance() {
        if (instance == null) {
            instance = new MovieFilter();
        }
        return instance;
    }

    public double getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean getIncludeWatched() {
        return includeWatched;
    }

    public boolean getIncludeWatchLater() {
        return includeWatchLater;
    }

    public boolean getIncludeFavorites() {
        return includeFavorites;
    }

    public void setSwitch(String tag, boolean status) {
        switch (tag) {
            case "watched_switch":
                includeWatched = status;
                break;
            case "watch_later_switch":
                includeWatchLater = status;
                break;
            case "favorites_switch":
                includeFavorites = status;
                break;
        }
    }

    public void setSpinner(String tag, String value) {
        switch (tag) {
            case "rating_spinner":
                rating = Double.parseDouble(value);
                break;
            case "genre_spinner":
                genre = value;
                break;
            case "order_by_spinner":
                orderBy = value;
                break;
        }
    }
}
