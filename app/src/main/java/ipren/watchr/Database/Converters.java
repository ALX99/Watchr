package ipren.watchr.Database;

import androidx.room.TypeConverter;

import java.util.Arrays;

public class Converters {

    @TypeConverter
    public static String genresToString(int[] g) {
        return Arrays.toString(g);
    }

    @TypeConverter
    public static int[] stringToGenres(String s) {
        String[] split = s.substring(1, s.length() - 1).split(", ");

        int[] genres = new int[split.length];

        for (int i = 0; i < genres.length; i++)
            genres[i] = Integer.parseInt(split[i]);

        return genres;
    }
}
