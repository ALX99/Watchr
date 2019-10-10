package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class Movie {
    @Ignore
    @ColumnInfo(name = "popularity")
    public double popularity;
    @Ignore
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    public int voteCount;
    @Ignore
    @ColumnInfo(name = "video")
    public boolean video;
    @Ignore
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public int id;
    @Ignore
    public boolean adult;
    @Ignore
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    public String backdropPath;
    @Ignore
    @ColumnInfo(name = "language")
    @SerializedName("original_language")
    public String originalLanguage;
    @Ignore
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    public String originalTitle;
    @Ignore
    @SerializedName("genre_ids")
    public int[] genreIds;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Ignore
    public double voteAverage;
    @Ignore
    public String overview;
    @Ignore
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    public String releaseDate;

    // Will insert these things into the Db
    // when discussed more with group.
    // Ignore for now
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
