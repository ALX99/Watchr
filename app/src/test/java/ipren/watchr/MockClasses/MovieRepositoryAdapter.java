package ipren.watchr.MockClasses;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataholders.Actor;
import ipren.watchr.dataholders.Genre;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.repository.IMovieRepository;

public class MovieRepositoryAdapter implements IMovieRepository {
    @Override
    public LiveData<Movie> getMovieByID(int ID) {
        return null;
    }

    @Override
    public LiveData<List<Movie>> getMoviesByID(int[] IDs) {
        return null;
    }

    @Override
    public LiveData<List<Genre>> getGenresFromMovie(int ID) {
        return null;
    }

    @Override
    public LiveData<List<Movie>> getMovieList(String listID, int page, boolean forceFetch) {
        return null;
    }

    @Override
    public LiveData<List<Actor>> getActorsFromMovie(int movieID) {
        return null;
    }

    @Override
    public LiveData<List<Movie>> Search(String text, int page, boolean forceFetch) {
        return null;
    }

    @Override
    public LiveData<List<Movie>> getDiscoverList(int[] genres, int page, boolean forceFetch) {
        return null;
    }
}
