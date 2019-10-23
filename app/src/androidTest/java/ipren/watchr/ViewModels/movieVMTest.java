package ipren.watchr.ViewModels;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import ipren.watchr.TestUtil.LiveDataTestUtil;
import ipren.watchr.dataHolders.ActorList;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.repository.Database.MovieDao;
import ipren.watchr.viewModels.MovieViewModel;

public class movieVMTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private Movie m = new Movie(3, "testMovie");
    private MovieDao movieDao;
    private MovieDB db;
    MovieViewModel t;

    @Before
    public void createDb() throws Exception {
        Application a = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(a, MovieDB.class).allowMainThreadQueries().build();
        t = new MovieViewModel(a);
        m.setActorList(new ActorList());
        m.setUpdateDate(new Date());

        movieDao = db.movieDao();
        movieDao.insert(m);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void getTest() throws Exception {
        t.setMovieID(m.id);

        Movie x = LiveDataTestUtil.getValue(t.getMovie());
        Thread.sleep(1000000);
        Assert.assertEquals(m.title, x.title);
    }

}