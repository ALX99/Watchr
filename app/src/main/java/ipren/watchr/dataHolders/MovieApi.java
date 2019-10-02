package ipren.watchr.dataHolders;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MovieApi {
    @GET("movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=1")
    Single<List<Movie>> getMovies();
}
