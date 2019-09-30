package ipren.watchr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ipren.watchr.model.Movie;

/**
 *
 */
public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    public MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    // We use AndroidViewModel to get access to a context for storing data
    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        Movie movie1 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Schindler's List", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie2 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Godfather", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie3 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Saving Private Ryan", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie4 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Schindler's List", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie5 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Godfather", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie6 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Saving Private Ryan", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie7 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Schindler's List", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie8 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Godfather", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        Movie movie9 = new Movie(0.0, 0, false, "", 1, false, "", "", "", null, "Saving Private Ryan", 9.0, "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II.", "");
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
        movieList.add(movie4);
        movieList.add(movie5);
        movieList.add(movie6);
        movieList.add(movie7);
        movieList.add(movie8);
        movieList.add(movie9);
        movies.setValue(movieList);
        movieLoadError.setValue(false);
        loading.setValue(false);
    }
}
