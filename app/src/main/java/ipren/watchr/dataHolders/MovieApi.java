package ipren.watchr.dataHolders;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MovieApi {
    @GET
    Single<MovieList> getMovies(@Url String url);
}
