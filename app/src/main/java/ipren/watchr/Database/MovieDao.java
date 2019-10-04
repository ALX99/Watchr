package ipren.watchr.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ipren.watchr.dataHolders.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE movie_id LIKE :movieID")
    LiveData<Movie> getMovieByID(int movieID);

    /*
    This probably won't work, just testing it out in case.
    If it doesn't work I will design a many to many relationship
    between a genre and a movie to be able to query both movies by genre
    and genres by movies.
     */
//    @Query("SELECT * FROM movies WHERE genre_ids IN(:genres)")
//    LiveData<List<Movie>> getMoviesByGenre(int[] genres);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... movies);

    @Update
    void updateMovies(Movie... movies);


}
