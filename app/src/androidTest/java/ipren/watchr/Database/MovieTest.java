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
        Movie movie = new Movie(1, "testMovie", new int[]{1});
        movieDao.insertMovies(movie);
        List<Movie> movies = LiveDataTestUtil.getValue(movieDao.getAllMovies());
        Assert.assertEquals(movies.get(0).title, movie.title);
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
        Movie movie = new Movie(1, "testMovie1", new int[]{1});
        movieDao.insertMovies(movie);
        movieDao.insertMovies(new Movie(2, "testMovie2", new int[]{1}));
        movieDao.deleteMovies(movie);
        movieDao.deleteMoviesByID(2);

        Assert.assertEquals(0, LiveDataTestUtil.getValue(movieDao.getAllMovies()).size());
    }

    @Test
    public void getMoviesByIDs() throws Exception {
        Movie movie = new Movie(1, "testMovie1", new int[]{
                1, 23, 67
        });

        // Yep, not working. Will fix later with M:N relationship between movies table and genre table
        List<Movie> movies = LiveDataTestUtil.getValue(movieDao.getMoviesByGenre(new int[]{67}));
        Assert.assertEquals(1, movies.size());
    }

}
