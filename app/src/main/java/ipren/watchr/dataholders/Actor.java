package ipren.watchr.dataholders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

// This tables entries is linked to columns in the movies table
@Entity(tableName = "actors", foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE),
        indices = {@Index("movie_id")})
public class Actor {
    @PrimaryKey
    @NonNull
    @SerializedName("credit_id")
    private String ID;
    @NonNull
    @ColumnInfo(name = "movie_id")
    private int movieID;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;
    @ColumnInfo(name = "character")
    @SerializedName("character")
    private String character; // The character the actor plays
    @ColumnInfo(name = "picture_link")
    @SerializedName("profile_path")
    private String pictureLink;
    @ColumnInfo(name = "order")
    @SerializedName("order")
    private int order; // Some actors play more important roles than others

    public Actor(String ID, int movieID, String name) {
        this.ID = ID;
        this.movieID = movieID;
        this.name = name;
    }

    public Actor(int movieID, Actor a) {
        this.movieID = movieID;
        this.ID = a.getID();
        name = a.getName();
        character = a.getCharacter();
        pictureLink = a.getPictureLink();
        order = a.getOrder();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = new StringBuilder().append("https://image.tmdb.org/t/p/original").append(pictureLink).toString();
    }
}
