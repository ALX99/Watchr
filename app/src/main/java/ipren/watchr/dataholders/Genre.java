package ipren.watchr.dataholders;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

// Many to many relationship
@Entity(tableName = "genres")
public class Genre {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private final int genreID;
    @ColumnInfo(name = "name")
    @SerializedName("name")
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
