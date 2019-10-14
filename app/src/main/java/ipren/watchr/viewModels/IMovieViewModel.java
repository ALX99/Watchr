package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;

public interface IMovieViewModel {
    LiveData<Movie> getMovie(int id);

    LiveData<List<Genre>> getGenres(int id);
}
