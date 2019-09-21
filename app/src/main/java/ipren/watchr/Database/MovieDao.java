package ipren.watchr.Database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.Movie;
// https://developer.android.com/reference/androidx/room/Dao

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE genres IN (:g)")
    public abstract List<Movie> findMovieByGenre(int[] g);
}
