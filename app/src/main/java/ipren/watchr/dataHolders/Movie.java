package ipren.watchr.dataHolders;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing a movie object
 */
public class Movie {
    public double popularity;
    @SerializedName("vote_count")
    public int voteCount;
    public boolean video;
    @SerializedName("poster_path")
    public String posterPath;
    public int id;
    public boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("genre_ids")
    public int[] genreIds;
    public String title;
    @SerializedName("vote_average")
    public double voteAverage;
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;

    public Movie(double popularity, int voteCount, boolean video, String posterPath, int id, boolean adult, String backdropPath, String originalLanguage, String originalTitle, int[] genreIds, String title, double voteAverage, String overview, String releaseDate) {
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.posterPath = posterPath;
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }
}
