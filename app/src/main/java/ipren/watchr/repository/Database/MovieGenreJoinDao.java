package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

@Dao
public interface MovieGenreJoinDao {
    @Insert
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
    void deleteMovieGenreJoin(MovieGenreJoin... movieGenreJoin);

}
