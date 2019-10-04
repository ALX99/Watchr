package ipren.watchr.dataHolders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class for getting list of movies from API
 */
public class MovieList {
        @SerializedName("results")
        public List<Movie> movies;
}
