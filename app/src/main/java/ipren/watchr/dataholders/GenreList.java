package ipren.watchr.dataholders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// I can't for the love of got figure out how to use
// gson deserializers
public class GenreList {
    @SerializedName("genres")
    List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }
}
