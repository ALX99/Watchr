package ipren.watchr.repository.movierepository.Database;

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

import java.util.Date;
import java.util.List;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.MovieList;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MovieListTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MovieDao movieDao;
    private MovieDB db;
    private Movie m = new Movie(1, "testMovie");
    MovieList movieList = new MovieList(m.getId(), "name", 1, new Date());
    private MovieListDao movieListDao;


    @Before
    public void createDb() {
        // using an in-memory database because the information stored
        // here disappears when the process is killed
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        movieDao = db.movieDao();
        movieDao.insert(m);
        movieListDao = db.movieListDao();
        movieListDao.insert(movieList);
        MovieList a = new MovieList(m.getId(), "naame", 1, new Date());
        movieListDao.insert(a);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndDeleteTest() throws Exception {
        List<MovieList> list = movieListDao.getMovieListsNonLivedata(movieList.getListName(), 1);
        Assert.assertEquals(movieList.getListName(), list.get(0).getListName());
    }

    @Test
    public void getMoviesTest() throws Exception {
        MovieList movieList = new MovieList(m.getId(), "name", 1, new Date());
        movieListDao.insert(movieList);
        List<Movie> list = LiveDataTestUtil.getValue(movieListDao.getAllMoviesFromList(movieList.getListName()));
        Assert.assertEquals(m.getTitle(), list.get(0).getTitle());
    }

    @Test
    public void removeMovieFromListTest() throws Exception {
        Movie m = new Movie(2, "testMovie");
        movieDao.insert(m);
        movieListDao.removeMovieFromList(m.getId(), movieList.getListName());
        List<Movie> mList = LiveDataTestUtil.getValue(movieListDao.getMoviesFromList(movieList.getListName(), 1));
        Assert.assertEquals(1, mList.size());
        mList = LiveDataTestUtil.getValue(movieListDao.getMoviesFromList(movieList.getListName(), 2));
        Assert.assertEquals(0, mList.size());
    }

}