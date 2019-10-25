package ipren.watchr.repository.movierepository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

import ipren.watchr.dataholders.Genre;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.MovieGenreJoin;

@Dao
public interface MovieGenreJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieGenreJoin... movieGenreJoin);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movies " +
            "INNER JOIN movie_genre_join " +
            "ON movies.id=movie_genre_join.movieID " +
            "WHERE movie_genre_join.genreID IN(:genreIDs)")
    LiveData<List<Movie>> getMoviesByGenre(final int[] genreIDs);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM genres " +
            "INNER JOIN movie_genre_join " +
            "ON genres.id =movie_genre_join.genreID " +
            "WHERE movie_genre_join.movieID=:movieID")
    LiveData<List<Genre>> getGenresFromMovie(final int movieID);


    // No update method since both genreID and movieID
    // are primary keys, so there would be nothing that could
    // be updated

    @Delete
    void delete(MovieGenreJoin... movieGenreJoin);

    @Query("DELETE FROM movie_genre_join")
    void NUKE();
}
