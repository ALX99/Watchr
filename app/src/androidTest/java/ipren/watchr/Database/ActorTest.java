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
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.Database.ActorDao;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.repository.Database.MovieDao;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ActorTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private Actor a = new Actor("1", 1, "Chris Pratt", "Joker", 2, "pictureLink");
    private Movie m = new Movie(1, "testMovie");
    private ActorDao actorDao;
    private MovieDao movieDao;
    private MovieDB db;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), MovieDB.class).allowMainThreadQueries().build();
        actorDao = db.actorDao();
        movieDao = db.movieDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() throws Exception {
        movieDao.insert(m);
        // Insert the actor after the movie since the actor table is a foreign key
        // referencing entries in the movies table. The movie id must already exist in movies
        // before we try to insert an actor associated with that movie
        actorDao.insert(a);
        List<Actor> actors = LiveDataTestUtil.getValue(actorDao.getActorsFromMovie(1));
        Assert.assertEquals(a.getCharacter(), actors.get(0).getCharacter());
        Actor newA = new Actor("2", 1, "Chris lol", "Joker", 2, "pictureLink");
        actorDao.insert(newA);
        Assert.assertEquals(2, LiveDataTestUtil.getValue(actorDao.getActorsFromMovie(m.getId())).size());

    }

}
