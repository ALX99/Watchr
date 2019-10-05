package ipren.watchr.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

// table that binds together a many to many relationship
// in an SQL database. A movie can have many genres
// and a genre can have many movies.
// This is used since we don't want to create
// new entries for the same genre because it is
// in a different movie
@Entity(tableName = "movie_genre_join",
        primaryKeys = {"movieID", "genreID"},
        foreignKeys = {
                @ForeignKey(entity = Movie.class,
                        parentColumns = "movie_id",
                        childColumns = "movieID"),
                @ForeignKey(entity = Genre.class,
                        parentColumns = "genre_id",
                        childColumns = "genreID")
        })
public class MovieGenreJoin {
    public int movieID;
    public int genreID;
}
