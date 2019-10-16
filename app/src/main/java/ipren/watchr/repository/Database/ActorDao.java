package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.dataHolders.Actor;

@Dao
public interface ActorDao {
    @Query("SELECT * FROM actors WHERE movie_id LIKE :movieID")
    LiveData<List<Actor>> getActorsFromMovie(int movieID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Actor... actor);

    @Query("DELETE FROM actors")
    void NUKE();
    
}
