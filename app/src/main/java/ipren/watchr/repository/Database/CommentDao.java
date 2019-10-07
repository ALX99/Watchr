package ipren.watchr.repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ipren.watchr.dataHolders.Comment;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments WHERE movie_id LIKE :movieID")
    LiveData<List<Comment>> getCommentsFromMovie(int movieID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(Comment... comments);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateComments(Comment... comments);

    @Delete
    void deleteComments(Comment... comments);
}
