package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Class for getting list of movies from API
 * and for storing different types of lists of movie
 * in the database
 */
@Entity(tableName = "movie_lists",
        primaryKeys = {"movie_id", "list_id"},
        foreignKeys = @ForeignKey(entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id",
                onDelete = CASCADE))
public class MovieList {
    @Ignore
    @SerializedName("results")
    private List<Movie> movies;
    @ColumnInfo(name = "movie_id")
    private int movieID;
    @NonNull
    @ColumnInfo(name = "list_id")
    private String listName;
    @ColumnInfo(name = "page")
    private int page;
    @ColumnInfo(name = "update_date")
    private Date updateDate;


    public MovieList(int movieID, @NonNull String listName, int page, Date updateDate) {
        this.movieID = movieID;
        this.listName = listName;
        this.page = page;
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    @NonNull
    public String getListName() {
        return listName;
    }

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getMovieID() {
        return movieID;
    }

}
