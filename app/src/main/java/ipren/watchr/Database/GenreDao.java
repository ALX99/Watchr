package ipren.watchr.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ipren.watchr.dataHolders.Genre;

@Dao
public interface GenreDao {
    @Query("DELETE FROM genres WHERE id IN(:genreIDs)")
    int deleteGenreByID(int[] genreIDs);

    @Query("SELECT * FROM genres WHERE id=:genreID")
    LiveData<Genre> getGenreByID(int genreID);

    @Query("SELECT * FROM genres")
    LiveData<List<Genre>> getAllGenres();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGenres(Genre... genres);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGenres(Genre... genres);

    @Delete
    void deleteGenres(Genre... genres);

}
