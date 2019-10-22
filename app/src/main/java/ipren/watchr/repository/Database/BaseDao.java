package ipren.watchr.repository.Database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> items);

    void update(T... items);

    void update(List<T> items);

    @Delete
    void delete(T... items);
}
