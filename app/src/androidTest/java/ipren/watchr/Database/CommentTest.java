package ipren.watchr.Database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Movie;

public class CommentTest {
    private Movie m = new Movie(1, "testMovie", new int[]{1});

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CommentDao commentDao;
    private MovieDao movieDao;
    private MovieDB db;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        commentDao = db.commentDao();
        movieDao = db.movieDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() throws Exception {
        movieDao.insertMovies(m);
        commentDao.insertComments(new Comment(1, "username", "comment", "profilePicLink"));
        List<Comment> comments = LiveDataTestUtil.getValue(commentDao.getCommentsFromMovie(1));
        Assert.assertEquals("username", comments.get(0).getUsername());
    }

    @Test
    public void deleteTest() throws Exception {
        movieDao.insertMovies(m);
        commentDao.insertComments(new Comment(1, "username", "comment", "profilePicLink"));
        movieDao.deleteMovies(m);
        List<Comment> comments = LiveDataTestUtil.getValue(commentDao.getCommentsFromMovie(1));
        Assert.assertEquals(0, comments.size());
    }
}
