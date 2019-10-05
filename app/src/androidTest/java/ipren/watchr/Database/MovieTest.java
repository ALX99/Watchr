package ipren.watchr.Database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataHolders.Movie;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MovieTest {
    // Will throw IllegalSTateException: Cannot invoke observeForever on a background thread
    // if this rule isn't included
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieDao movieDao;
    private MovieDB db;
    private Movie m = new Movie(1, "testMovie", new int[]{1});

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

    @Test
    public void testEmpty() throws Exception {
        Assert.assertTrue(LiveDataTestUtil.getValue(movieDao.getAllMovies()).isEmpty());
    }

    @Test
    public void movieInsertTest() throws Exception {
        movieDao.insertMovies(m);
        List<Movie> movies = LiveDataTestUtil.getValue(movieDao.getAllMovies());
        Assert.assertEquals(movies.get(0).title, m.title);
    }

    @Test
    public void movieFind() throws Exception {
        Movie movie = new Movie(2, "testMovie", new int[]{1});
        movieDao.insertMovies(movie);
        Movie gMovie = LiveDataTestUtil.getValue(movieDao.getMovieByID(2));

        Assert.assertEquals(gMovie.id, movie.id);
    }

    @Test
    public void movieDelete() throws Exception {
        movieDao.insertMovies(m);
        movieDao.insertMovies(new Movie(2, "testMovie2", new int[]{1}));
        movieDao.deleteMovies(m);
        movieDao.deleteMoviesByID(2);

        Assert.assertEquals(0, LiveDataTestUtil.getValue(movieDao.getAllMovies()).size());
    }

    @Test
    public void updateTest() throws Exception {
        movieDao.insertMovies(m);
        Movie newM = new Movie(1, "title", new int[]{1});
        movieDao.updateMovies(newM);
        Assert.assertEquals(newM.title, LiveDataTestUtil.getValue(movieDao.getMovieByID(1)).title);
    }
}
