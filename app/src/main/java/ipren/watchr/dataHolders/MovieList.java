package ipren.watchr.dataHolders;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

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
    @ColumnInfo(name = "list_id")
    private int listID;

    public MovieList(int movieID, int listID) {
        this.movieID = movieID;
        this.listID = listID;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getMovieID() {
        return movieID;
    }

    public int getListID() {
        return listID;
    }
}
