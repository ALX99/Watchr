package ipren.watchr.Helpers;

import androidx.room.TypeConverter;

import java.util.Arrays;

// Class to convert advanced types to more primitive
// stuff to be able to store it in the database.
public class Converters {

    // Convert int[] to String
    @TypeConverter
    public static String genresToString(int[] gennres) {
        return Arrays.toString(gennres);
    }

    // convert int[] to String
    @TypeConverter
    public static int[] stringToGenres(String s) {
        String[] split = s.substring(1, s.length() - 1).split(", ");

        int[] genres = new int[split.length];

        for (int i = 0; i < genres.length; i++)
            genres[i] = Integer.parseInt(split[i]);

        return genres;
    }
}
