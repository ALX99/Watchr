package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;

import java.util.List;

import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;

public interface IMovieViewModel {
    LiveData<Movie> getMovie();

    void setMovieID(int movieID);

    void commentOnMovie(int movieID, String UID, String text);

    LiveData<User> getUser();

    LiveData<FireComment[]> getComments();

    LiveData<List<Genre>> getGenres();
}
