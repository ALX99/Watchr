package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import ipren.watchr.Helpers.Converters;

@Entity(tableName = "movies")
@TypeConverters({Converters.class})
public class Movie {
    @ColumnInfo(name = "popularity")
    public double popularity;
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    public int voteCount;
    @ColumnInfo(name = "video")
    public boolean video;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movie_id")
    public int id;
    public boolean adult;
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    public String backdropPath;
    @ColumnInfo(name = "language")
    @SerializedName("original_language")
    public String originalLanguage;
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("genre_ids")
    @Ignore
    public int[] genreIds;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    public double voteAverage;
    public String overview;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    public String releaseDate;

    @Ignore
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

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
