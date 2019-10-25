package ipren.watchr.dataholders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Used by Gson
 */
public class GenreList {
    @SerializedName("genres")
    List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }
}
