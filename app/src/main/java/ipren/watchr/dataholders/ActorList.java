package ipren.watchr.dataholders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Used by Gson
 */
public class ActorList {
    @SerializedName("cast")
    List<Actor> actors;

    public List<Actor> getActors() {
        return actors;
    }
}
