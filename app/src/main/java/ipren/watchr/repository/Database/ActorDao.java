package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ipren.watchr.dataHolders.Actor;

@Dao
public interface ActorDao extends BaseDao<Actor> {
    @Query("SELECT * FROM actors WHERE movie_id LIKE :movieID")
    LiveData<List<Actor>> getActorsFromMovie(int movieID);

    @Query("DELETE FROM actors")
    void NUKE();
    
}
