package ipren.watchr.Database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.repository.Database.GenreDao;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.repository.Database.MovieDao;

public class GenreTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private Genre g = new Genre(1, "Horror");
    private GenreDao genreDao;
    private MovieDao movieDao;
    private MovieDB db;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        genreDao = db.genreDao();
        movieDao = db.movieDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() throws Exception {
        genreDao.insertGenres(g);
        Genre genre = LiveDataTestUtil.getValue(genreDao.getGenreByID(1));
        Assert.assertEquals(g.getName(), genre.getName());
    }

    @Test
    public void deleteTest() throws Exception {
        genreDao.insertGenres(g);
        int deletesRows = genreDao.deleteGenreByID(new int[]{1});
        Assert.assertEquals(1, deletesRows);
        genreDao.insertGenres(g);
        genreDao.insertGenres(new Genre(2, "Action"));
        deletesRows = genreDao.deleteGenreByID(new int[]{1, 2});
        Assert.assertEquals(2, deletesRows);
        genreDao.insertGenres(g);
        genreDao.deleteGenres(g);
        Assert.assertEquals(0, LiveDataTestUtil.getValue(genreDao.getAllGenres()).size());
    }

    @Test
    public void updateTest() throws Exception {
        genreDao.insertGenres(g);
        Genre newG = new Genre(1, "Action");
        genreDao.updateGenres(newG);
        Assert.assertEquals(newG.getName(), LiveDataTestUtil.getValue(genreDao.getGenreByID(1)).getName());
    }

}
