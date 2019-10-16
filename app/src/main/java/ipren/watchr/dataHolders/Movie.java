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
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    public String posterPath; // TODO
    @ColumnInfo(name = "title")
    public String title; // TODO
    @SerializedName("overview")
    public String overview; // TODO
    @ColumnInfo(name = "popularity")
    private double popularity;
    @Ignore
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("video")
    @ColumnInfo(name = "video")
    private boolean video;
    @SerializedName("status")
    @ColumnInfo(name = "Status")
    private String status;
    private boolean adult;
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;
    @ColumnInfo(name = "language")
    @SerializedName("original_language")
    private String originalLanguage;
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private double voteAverage;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;
    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    private String runtime;
    @Ignore
    @SerializedName("genres")
    private Genre[] genres;
    @Ignore
    @SerializedName("credits")
    private ActorList actorList;

    public Movie(int id, String posterPath, String title, String overview, double popularity, int voteCount, boolean video, String status, boolean adult, String backdropPath, String originalLanguage, String originalTitle, double voteAverage, String releaseDate, String runtime, Genre[] genres) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.status = status;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.genres = genres;
    }

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public ActorList getActorList() {
        return actorList;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
