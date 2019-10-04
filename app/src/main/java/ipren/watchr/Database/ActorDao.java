package ipren.watchr.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ipren.watchr.dataHolders.Actor;

@Dao
public interface ActorDao {
    @Query("SELECT * FROM actors WHERE movie_id LIKE :movieID")
    LiveData<List<Actor>> getActorsFromMovie(int movieID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Actor... actor);

    @Update
    void updateActors(Actor... actors);

    @Delete
    void deleteActors(Actor... actors);

}
