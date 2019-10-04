package ipren.watchr.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ipren.watchr.dataHolders.Movie;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DBTest {
    private MovieDao movieDao;
    private MovieDB db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieDB.class).build();
        movieDao = db.movieDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void movieInsertTest() throws Exception {
        Movie movie = new Movie(1, "testMovie");
        movieDao.insert(movie);
        List<Movie> movies = movieDao.getAllMovies();

        Assert.assertEquals(movie.title, movies.get(0).title);
    }

}
