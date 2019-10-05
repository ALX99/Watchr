package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

// This tables entries is linked to columns in the movies table
@Entity(tableName = "comments", foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "movie_id",
        childColumns = "movie_id",
        onDelete = CASCADE))
public class Comment {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private final int movieID;
    @ColumnInfo(name = "picture_link")
    private String profilePicLink;
    @SerializedName("author")
    @ColumnInfo(name = "username")
    private String username;
    @SerializedName("content")
    @ColumnInfo(name = "comment")
    private String comment;

    public Comment(int movieID, String username, String comment, String profilePicLink) {
        this.movieID = movieID;
        this.profilePicLink = profilePicLink;
        this.username = username;
        this.comment = comment;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
