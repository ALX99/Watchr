package ipren.watchr.repository.movierepository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.dataholders.Genre;

@Dao
public interface GenreDao extends BaseDao<Genre> {
    @Query("DELETE FROM genres WHERE id IN(:genreIDs)")
    int deleteGenreByID(int[] genreIDs);

    @Query("SELECT * FROM genres WHERE id=:genreID")
    LiveData<Genre> getGenreByID(int genreID);

    @Query("SELECT * FROM genres")
    LiveData<List<Genre>> getAllGenres();

    @Query("DELETE FROM genres")
    void NUKE();

}
