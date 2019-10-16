package ipren.watchr.repository.API;

import io.reactivex.Single;
import ipren.watchr.dataHolders.GenreList;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieList;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi implements IMovieApi {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private IMovieApi api;

    public MovieApi() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(IMovieApi.class);
    }

    public Single<MovieList> getMovies(String url) {
        return api.getMovies(url);
    }

    @Override
    public Call<MovieList> getTrending() {
        return api.getTrending();
    }

    @Override
    public Call<Movie> getMovie(int movieID) {
        return api.getMovie(movieID);
    }

    @Override
    public Call<GenreList> getAllGenres() {
        return api.getAllGenres();
    }
}
