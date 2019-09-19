package ipren.watchr.Database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.Movie;
// https://developer.android.com/reference/androidx/room/Dao

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE genre = :g")
    public abstract List<Movie> findMovieByGenre(Genre g);
}
