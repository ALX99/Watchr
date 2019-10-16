package ipren.watchr.repository.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ipren.watchr.Helpers.DateConverter;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieGenreJoin;
import ipren.watchr.dataHolders.MovieList;

@Database(entities = {Movie.class, Actor.class, Comment.class, Genre.class, MovieGenreJoin.class, MovieList.class}, version = 11)
@TypeConverters(DateConverter.class)
public abstract class MovieDB extends RoomDatabase {
    private static MovieDB INSTANCE; // Database instance
    private static String DB_NAME = "Ipren-Database";

    public abstract ActorDao actorDao();

    public abstract MovieDao movieDao();

    public abstract CommentDao commentDao();

    public abstract GenreDao genreDao();

    public abstract MovieGenreJoinDao movieGenreJoinDao();

    private static Callback dbCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // Singleton
    public static synchronized MovieDB getInstance(Context context) {
        if (INSTANCE != null)
            return INSTANCE;

        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                MovieDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                // USED FOR TESTING PURPOSES. REMOVE IN PRODUCTION CODE. TODO
                // .addCallback(dbCallback)
                .build();
        return INSTANCE;

    }

    public abstract MovieListDao movieListDao();

    // TESTING PURPOSES
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDao movieDao;
        private final GenreDao genreDao;
        private final MovieGenreJoinDao movieGenreJoinDao;
        private final CommentDao commentDao;

        PopulateDbAsync(MovieDB db) {
            movieDao = db.movieDao();
            genreDao = db.genreDao();
            movieGenreJoinDao = db.movieGenreJoinDao();
            commentDao = db.commentDao();
            NUKE();
        }

        private void NUKE() {
            movieDao.NUKE();
            genreDao.NUKE();
            movieGenreJoinDao.NUKE();
            commentDao.NUKE();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            movieDao.insert(new Movie(475557, "ayy lmao"));
            genreDao.insert(new Genre(1, "Action"));
            genreDao.insert(new Genre(2, "Horror"));
            movieGenreJoinDao.insert(new MovieGenreJoin(475557, 1));
            movieGenreJoinDao.insert(new MovieGenreJoin(475557, 2));
            return null;
        }
    }
}