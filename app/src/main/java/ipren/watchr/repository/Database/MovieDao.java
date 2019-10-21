package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.dataHolders.Movie;

@Dao
public interface MovieDao extends BaseDao<Movie> {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id LIKE :movieID")
    Movie getMovieByIDNonLiveObject(int movieID);

    @Query("SELECT * FROM movies WHERE id LIKE :movieID")
    LiveData<Movie> getMovieByID(int movieID);


    @Query("SELECT * FROM movies WHERE id IN(:movieID)")
    LiveData<List<Movie>> getMoviesByID(int... movieID);

    @Query("DELETE FROM movies WHERE id = :movieID")
    void deleteMoviesByID(int movieID);

    @Query("DELETE FROM movies")
    void NUKE();

}
