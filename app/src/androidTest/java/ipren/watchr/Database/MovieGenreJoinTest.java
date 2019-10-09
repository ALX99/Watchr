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
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.Database.GenreDao;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.repository.Database.MovieDao;
import ipren.watchr.dataHolders.MovieGenreJoin;
import ipren.watchr.repository.Database.MovieGenreJoinDao;

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
        movieDao.insertMovies(m);
        genreDao.insertGenres(g);
        genreDao.insertGenres(g1);
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
        Assert.assertEquals(m.title, movies.get(0).title);

        movieGenreJoinDao.deleteMovieGenreJoin(new MovieGenreJoin(1, 2));
        movies = LiveDataTestUtil.getValue(movieGenreJoinDao.getMoviesByGenre(new int[]{2, 3}));
        Assert.assertEquals(m.title, movies.get(0).title);

    }

    @Test
    public void deleteTest() throws Exception {
        insertDummyData();
        movieGenreJoinDao.deleteMovieGenreJoin(new MovieGenreJoin(1, 2));
        List<Genre> genres = LiveDataTestUtil.getValue(movieGenreJoinDao.getGenresFromMovie(1));
        Assert.assertEquals(1, genres.size());

    }

}
