package ipren.watchr;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// https://developer.android.com/reference/androidx/room/Entity.html
@Entity
public class Movie {
    @PrimaryKey
    private final long id;

    private final String title;
    private final String description;
    @ColumnInfo(name = "genres")
    private final int[] genres;

    public Movie(long id, String title, String description, int[] genres) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int[] getGenres() {
        return genres;
    }

    public String getDescription() {
        return description;
    }
}