package ipren.watchr.dataholders;

import android.util.Log;

/**
 * Filter model containing current filter options.
 * Singleton.
 */
public class Filter {
    private static Filter instance;
    private String rating;
    private String genre;
    private String orderBy;
    private boolean includeWatched;
    private boolean includeWatchLater;
    private boolean includeFavorites;

    private Filter() {
        rating = "1+";
        genre = "Horror";
        orderBy = "Popularity";
        includeWatched = true;
        includeWatchLater = true;
        includeFavorites = true;
    }

    public static Filter getInstance() {
        if (instance == null) {
            instance = new Filter();
        }
        return instance;
    }

    public String getRating() {
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
        Log.d("TEST", tag + " " + status);
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
        Log.d("TEST", tag + " " + value);
        switch (tag) {
            case "rating_spinner":
                rating = value;
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
