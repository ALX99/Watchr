package ipren.watchr.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

@Database(entities = {Movie.class, Actor.class, Comment.class, Genre.class, MovieGenreJoin.class}, version = 1)
public abstract class MovieDB extends RoomDatabase {
    private static MovieDB INSTANCE; // Database instance
    private static String DB_NAME = "Ipren-Database";

    public abstract ActorDao actorDao();

    public abstract MovieDao movieDao();

    public abstract CommentDao commentDao();

    public abstract GenreDao genreDao();

    public abstract MovieGenreJoinDao movieGenreJoinDao();

    // Singleton
    public static MovieDB getInstance(Context context) {
        if (INSTANCE != null)
            return INSTANCE;

        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                MovieDB.class, DB_NAME)
                .build();
        return INSTANCE;

    }
}
