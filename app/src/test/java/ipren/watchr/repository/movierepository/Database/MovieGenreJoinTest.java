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

import java.util.List;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataholders.Genre;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.MovieGenreJoin;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MovieGenreJoinTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private Movie m = new Movie(1, "testMovie");
    private Genre g = new Genre(2, "Action");
    private Genre g1 = new Genre(3, "Horror");
    private GenreDao genreDao;
    private MovieDao movieDao;
    private MovieGenreJoinDao movieGenreJoinDao;
    private MovieDB db;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        genreDao = db.genreDao();
        movieDao = db.movieDao();
        movieGenreJoinDao = db.movieGenreJoinDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    private void insertDummyData() {
        movieDao.insert(m);
        genreDao.insert(g);
        genreDao.insert(g1);
        movieGenreJoinDao.insert(new MovieGenreJoin(1, 2));
        movieGenreJoinDao.insert(new MovieGenreJoin(1, 3));

    }

    @Test
    public void getGenresForMovieTest() throws Exception {
        insertDummyData();

        List<Genre> genres = LiveDataTestUtil.getValue(movieGenreJoinDao.getGenresFromMovie(1));
        Assert.assertEquals(2, genres.size());
    }

    @Test
    public void getMoviesByGenreTest() throws Exception {
        insertDummyData();
        List<Movie> movies = LiveDataTestUtil.getValue(movieGenreJoinDao.getMoviesByGenre(new int[]{2, 3}));
        Assert.assertEquals(m.getTitle(), movies.get(0).getTitle());

        movieGenreJoinDao.delete(new MovieGenreJoin(1, 2));
        movies = LiveDataTestUtil.getValue(movieGenreJoinDao.getMoviesByGenre(new int[]{2, 3}));
        Assert.assertEquals(m.getTitle(), movies.get(0).getTitle());

    }

    @Test
    public void deleteTest() throws Exception {
        insertDummyData();
        movieGenreJoinDao.delete(new MovieGenreJoin(1, 2));
        List<Genre> genres = LiveDataTestUtil.getValue(movieGenreJoinDao.getGenresFromMovie(1));
        Assert.assertEquals(1, genres.size());

    }

}
