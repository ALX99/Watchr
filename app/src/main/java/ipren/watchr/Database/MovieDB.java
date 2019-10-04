package ipren.watchr.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ipren.watchr.Helpers.Converters;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Movie;

@Database(entities = {Movie.class, Actor.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MovieDB extends RoomDatabase {
    private static MovieDB INSTANCE; // Database instance
    private static String DB_NAME = "Ipren-Database";

    // Singleton
    public static MovieDB getInstance(Context context) {
        if (INSTANCE != null)
            return INSTANCE;

        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                MovieDB.class, DB_NAME)
                .build();
        return INSTANCE;

    }

    public abstract ActorDao actorDao();

    public abstract MovieDao movieDao();

}
