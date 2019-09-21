package ipren.watchr.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ipren.watchr.Movie;

// https://developer.android.com/reference/androidx/room/Database
@Database(version = 1, entities = {Movie.class})
@TypeConverters({Converters.class})
public abstract class MovieDB extends RoomDatabase {

    abstract MovieDao getMovies();
}
