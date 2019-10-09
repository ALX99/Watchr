package ipren.watchr.repository.API;

import io.reactivex.Single;
import ipren.watchr.dataHolders.MovieList;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MovieApi {
    @GET
    Single<MovieList> getMovies(@Url String url);
}
