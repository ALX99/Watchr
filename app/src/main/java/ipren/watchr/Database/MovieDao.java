package ipren.watchr.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ipren.watchr.dataHolders.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id LIKE :movieID")
    LiveData<Movie> getMovieByID(int movieID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(Movie... movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovies(Movie... movies);

    @Delete
    void deleteMovies(Movie... movies);

    @Query("DELETE FROM movies WHERE id = :movieID")
    void deleteMoviesByID(int movieID);

}
