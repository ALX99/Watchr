package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieList;

@Dao
public interface MovieListDao extends BaseDao<MovieList> {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movies " +
            "INNER JOIN movie_lists " +
            " ON movies.id =movie_lists.movie_id " +
            "WHERE movie_lists.list_id = :listID")
    LiveData<List<Movie>> getAllMoviesFromList(String listID);

    @Query("SELECT * FROM movie_lists WHERE list_id =:listID AND page =:page")
    List<MovieList> getMovieListsNonLivedata(String listID, int page);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movies " +
            "INNER JOIN movie_lists " +
            " ON movies.id =movie_lists.movie_id " +
            "WHERE movie_lists.list_id = :listID " +
            "AND movie_lists.page =:page")
    LiveData<List<Movie>> getMoviesFromList(String listID, int page);

    @Query("DELETE FROM movie_lists WHERE movie_id = :movieID AND list_id = :listID")
    void removeMovieFromList(int movieID, String listID);

    @Query("DELETE FROM movie_lists")
    void NUKE();
}
