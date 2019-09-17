package com.example.watchr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MovieApi {
    @GET
    Call<MovieList> getMovies(@Url String url);
}
