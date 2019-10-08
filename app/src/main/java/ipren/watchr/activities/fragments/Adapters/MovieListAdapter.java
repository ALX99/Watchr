package ipren.watchr.activities.fragments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.BrowseFragmentDirections;
import ipren.watchr.dataHolders.Movie;

/**
 * Class for handling the creation and updating of movie cards in the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;

    /**
     * Creates a movie adapter with a list of movies
     */
    public MovieListAdapter(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    /**
     * Clears and updates the list and layouts with new content
     */
    public void updateMovieList(List<Movie> newMovieList) {
        movieList.clear();
        movieList.addAll(newMovieList);
        notifyDataSetChanged();
    }

    /**
     * Inflates a movie card layout and returns it in a view holder
     */
    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Gets called automatically to update a view holder when scrolling
     */
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        ImageView image = holder.itemView.findViewById(R.id.movieImage);
        TextView title = holder.itemView.findViewById(R.id.movieTitle);
        TextView overview = holder.itemView.findViewById(R.id.movieOverview);
        TextView rating = holder.itemView.findViewById(R.id.movieRating);
        TextView genres = holder.itemView.findViewById(R.id.movieGenres);
        ConstraintLayout layout = holder.itemView.findViewById(R.id.movieLayout);

        title.setText(movieList.get(position).title);
        overview.setText(movieList.get(position).overview);
        rating.setText("Rating: " + movieList.get(position).voteAverage);
        Util.loadImage(image, "https://image.tmdb.org/t/p//w92" + movieList.get(position).posterPath, Util.getProgressDrawable(image.getContext()));
        // Tap to go to movie details
        layout.setOnClickListener(v -> {
            BrowseFragmentDirections.ActionDetail action = BrowseFragmentDirections.actionDetail();
            // Pass the movie id
            action.setMovieId(movieList.get(position).id);
            Navigation.findNavController(layout).navigate(action);
        });
    }

    /**
     * Returns the size of the movie array
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * Class for holding a movie view layout
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
