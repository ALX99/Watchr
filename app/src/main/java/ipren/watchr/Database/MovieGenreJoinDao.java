package ipren.watchr.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

@Dao
public interface MovieGenreJoinDao {
    @Insert
    void insert(MovieGenreJoin movieGenreJoin);

    @Query("SELECT * FROM movies " +
            "INNER JOIN movie_genre_join " +
            "ON movies.movie_id=movie_genre_join.movieID " +
            "WHERE movie_genre_join.genreID IN(:genreIDs)")
    List<Movie> getMoviesByGenre(final int[] genreIDs);

    @Query("SELECT * FROM genres " +
            "INNER JOIN movie_genre_join " +
            "ON genres.genre_id =movie_genre_join.genreID " +
            "WHERE movie_genre_join.movieID=:movieID")
    List<Genre> getGenresByMovie(final int movieID);

}
