package ipren.watchr.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.MovieRepository;

public class MovieViewModel extends AndroidViewModel implements IMovieViewModel {
    IMovieRepository movieRepository;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application.getApplicationContext());
    }

    public LiveData<Movie> getMovie(int id) {
        return movieRepository.getMovieByID(id);
    }

    public LiveData<List<Genre>> getGenres(int id) {
        return movieRepository.getGenresFromMovie(id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
