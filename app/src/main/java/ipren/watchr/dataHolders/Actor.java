package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

// This tables entries is linked to columns in the movies table
@Entity(tableName = "actors", foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE))
public class Actor {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    @NonNull
    @ColumnInfo(name = "movie_id")
    private int movieID;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "character")
    private String character; // The character the actor plays
    @ColumnInfo(name = "picture_link")
    private String pictureLink;
    @ColumnInfo(name = "order")
    private int order; // Some actors play more important roles than others

    public Actor(int movieID, String name, String character, int order, String pictureLink) {
        this.movieID = movieID;
        this.name = name;
        this.character = character;
        this.order = order;
        this.pictureLink = new StringBuilder().append("https://image.tmdb.org/t/p/original").append(pictureLink).toString();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public int getOrder() {
        return order;
    }

    public String getPictureLink() {
        return pictureLink;
    }
}
