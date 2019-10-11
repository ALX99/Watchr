package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public final int id; // TODO?
    @Ignore
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath; // TODO
    @ColumnInfo(name = "title")
    public String title; // TODO
    @Ignore
    public String overview; // TODO
    @Ignore
    @ColumnInfo(name = "popularity")
    private double popularity;
    @Ignore
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private int voteCount;
    @Ignore
    @ColumnInfo(name = "video")
    private boolean video;
    @Ignore
    private boolean adult;
    @Ignore
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;
    @Ignore
    @ColumnInfo(name = "language")
    @SerializedName("original_language")
    private String originalLanguage;
    @Ignore
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Ignore
    private double voteAverage;
    @Ignore
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    // Will insert these things into the Db
    // when discussed more with group.
    // Ignore for now
    @Ignore
    public Movie(double popularity, int voteCount, boolean video, String posterPath, int id, boolean adult, String backdropPath, String originalLanguage, String originalTitle, String title, double voteAverage, String overview, String releaseDate) {
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.posterPath = posterPath;
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
