package ipren.watchr.Database;

import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.repository.Database.MovieDao;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MovieTest {
    // Will throw IllegalSTateException: Cannot invoke observeForever on a background thread
    // if this rule isn't included
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieDao movieDao;
    private MovieDB db;
    private Movie m = new Movie(1, "testMovie");
    private Movie m2 = new Movie(2, "testMovie");


    @Before
    public void createDb() {
        // using an in-memory database because the information stored
        // here disappears when the process is killed
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        movieDao = db.movieDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    private void insertDummyData() {
        movieDao.insert(m);
        movieDao.insert(m2);
    }

    @Test
    public void testEmpty() throws Exception {
        Assert.assertTrue(LiveDataTestUtil.getValue(movieDao.getAllMovies()).isEmpty());
    }


    @Test
    public void movieInsertTest() throws Exception {
        movieDao.insert(m);
        List<Movie> movies = LiveDataTestUtil.getValue(movieDao.getAllMovies());
        Assert.assertEquals(movies.get(0).title, m.title);
    }

    @Test
    public void movieFind() throws Exception {
        insertDummyData();
        Movie gMovie = LiveDataTestUtil.getValue(movieDao.getMovieByID(2));
        Assert.assertEquals(gMovie.id, m2.id);
    }

    @Test
    public void movieDelete() throws Exception {
        insertDummyData();
        movieDao.delete(m);
        movieDao.deleteMoviesByID(2);

        Assert.assertEquals(0, LiveDataTestUtil.getValue(movieDao.getAllMovies()).size());
    }

    @Test
    public void updateTest() throws Exception {
        movieDao.insert(m);
        Movie newM = new Movie(1, "title");
        movieDao.update(newM);
        Assert.assertEquals(newM.title, LiveDataTestUtil.getValue(movieDao.getMovieByID(1)).title);
    }
}
