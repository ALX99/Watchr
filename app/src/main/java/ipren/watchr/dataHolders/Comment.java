package ipren.watchr.dataHolders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

// This tables entries is linked to columns in the movies table
@Entity(tableName = "actors", foreignKeys = @ForeignKey(entity = Movie.class,
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
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "comment")
    private String comment;

    public Comment(int movieID, String profilePicLink, String username, String comment) {
        this.movieID = movieID;
        this.profilePicLink = profilePicLink;
        this.username = username;
        this.comment = comment;
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
