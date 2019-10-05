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

@RunWith(AndroidJUnit4ClassRunner.class)
public class ActorTest {
    private Actor a = new Actor(1, "Chris Pratt", "Joker", 2, "pictureLink");
    private Movie m = new Movie(1, "testMovie", new int[]{1});

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

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
        movieDao.insertMovies(m);
        // Insert the actor after the movie since the actor table is a foreign key
        // referencing entries in the movies table. The movie id must already exist in movies
        // before we try to insert an actor associated with that movie
        actorDao.insertActors(a);
        List<Actor> actors = LiveDataTestUtil.getValue(actorDao.getActorsFromMovie(1));
        Assert.assertEquals(a.getCharacter(), actors.get(0).getCharacter());
    }

    @Test
    public void deleteTest() throws Exception {
        movieDao.insertMovies(m);
        actorDao.insertActors(a);
        actorDao.insertActors(new Actor(1,"blabla", "blabla",2,"picturelinkk"));
        movieDao.deleteMovies(m);
        List<Actor> actors = LiveDataTestUtil.getValue(actorDao.getActorsFromMovie(m.id));
        // If movie is deleted all actors associated with
        // the movie should be deleted as well
        Assert.assertEquals(0, actors.size());
    }
}
