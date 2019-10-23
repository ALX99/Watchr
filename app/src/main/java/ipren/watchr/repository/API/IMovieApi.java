package ipren.watchr.repository.API;

import io.reactivex.Single;
import ipren.watchr.Constants;
import ipren.watchr.dataHolders.GenreList;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IMovieApi {
    @GET
    Single<MovieList> getMovies(@Url String url);

    @GET("trending/movie/week?api_key=" + Constants.API_KEY)
    Call<MovieList> getTrending(@Query("page") int page);

    @GET("movie/{id}?api_key=" + Constants.API_KEY + "&language=en-US&append_to_response=credits")
    Call<Movie> getMovie(@Path("id") int movieID);

    @GET("genre/movie/list?api_key=" + Constants.API_KEY + "&language=en-US")
    Call<GenreList> getAllGenres();

    @GET("discover/movie?api_key=" + Constants.API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=true")
    Call<MovieList> getDiscover(@Query("with_genres") int[] genres, @Query("page") int page);

    @GET("search/movie?api_key=" + Constants.API_KEY)
    Call<MovieList> Search(@Query("query") String text, @Query("page") int page);
}
