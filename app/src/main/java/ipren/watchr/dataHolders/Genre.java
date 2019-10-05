package ipren.watchr.dataHolders;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Many to many relationship
@Entity(tableName = "genres")
public class Genre {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "genre_id")
    private final int genreID;
    private final String name;

    public Genre(int genreID, String name) {
        this.genreID = genreID;
        this.name = name;
    }

    public int getGenreID() {
        return genreID;
    }

    public String getName() {
        return name;
    }
}
