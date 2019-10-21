package ipren.watchr.activities.fragments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ipren.watchr.R;
import ipren.watchr.activities.fragments.MovieListFragmentDirections;
import ipren.watchr.activities.fragments.listeners.MovieClickListener;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.databinding.ItemMovieBinding;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.ListViewModel;

/**
 * Class for handling the creation and updating of movie cards in the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> implements MovieClickListener, Filterable {

    // Singleton instance
    private static MovieListAdapter instance;

    private ListViewModel listViewModel;
    private List<Movie> movieList;
    private List<Movie> movieListFull;

    /**
     * Search and filter handling
     */
    private Filter filter = new Filter() {
        /**
         * Filters all the movies in the current list according to the filter constraint.
         * This is done in a background thread.
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filteredMovieList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredMovieList.addAll(movieListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Movie movie : movieListFull) {
                    if (movie.title.toLowerCase().contains(filterPattern)) {
                        filteredMovieList.add(movie);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredMovieList;

            return results;
        }

        /**
         * Displays the filter results on the main thread.
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movieList.clear();
            movieList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Class for holding a movie view layout
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        public ItemMovieBinding itemView;

        public MovieViewHolder(@NonNull ItemMovieBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

    /**
     * Creates a movie adapter with a list of movies
     */
    private MovieListAdapter(ListViewModel listViewModel) {
        this.movieList = new ArrayList<>();
        this.listViewModel = listViewModel;
    }

    /**
     * @return Singleton instance
     */
    public static MovieListAdapter getInstance(ListViewModel listViewModel) {
        if (instance == null) {
            instance = new MovieListAdapter(listViewModel);
        }
        return instance;
    }

    /**
     * Clears and updates the list and layouts with new content
     */
    public void updateMovieList(List<Movie> newMovieList) {
        movieList.clear();
        movieList.addAll(newMovieList);
        // Create a copy of the full list so we can filter the other
        movieListFull = new ArrayList<>(movieList);
        notifyDataSetChanged();
    }

    /**
     * Inflates a movie card layout and returns it in a view holder
     */
    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding view = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Gets called automatically to update a view holder when scrolling
     */
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        holder.itemView.setMovie(movieList.get(position));
        holder.itemView.setListener(this);

        // TODO: @johan Refactor and fix this (gives error)
//        if (checkMovieInList(getMovieId(holder.itemView.getRoot()), IUserDataRepository.FAVORITES_LIST)) {
//            changeButtonColor(holder.itemView.favoriteButton, R.color.colorAccent);
//        } else {
//            changeButtonColor(holder.itemView.favoriteButton, R.color.text);
//        }
//
//        if (checkMovieInList(getMovieId(holder.itemView.getRoot()), IUserDataRepository.WATCH_LATER_LIST)) {
//            changeButtonColor(holder.itemView.addButton, R.color.colorAccent);
//        } else {
//            changeButtonColor(holder.itemView.addButton, R.color.text);
//        }
//
//        if (checkMovieInList(getMovieId(holder.itemView.getRoot()), IUserDataRepository.WATCHED_LIST)) {
//            changeButtonColor(holder.itemView.watchedButton, R.color.colorAccent);
//        } else {
//            changeButtonColor(holder.itemView.watchedButton, R.color.text);
//        }
    }

    /**
     * Navigates to the detail screen when card is clicked
     */
    @Override
    public void onMovieClicked(View v) {
        int id = getMovieId(v);

        MovieListFragmentDirections.ActionDetail action = MovieListFragmentDirections.actionDetail();
        action.setMovieId(id);
        Navigation.findNavController(v).navigate(action);
    }

    /**
     * Adds/removes the movie to the favorite list
     */
    @Override
    public void onFavoriteClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);
        // TODO: @johan Fix multiple buttons being highlighted because ViewHolders is being reused
        buttonHandler(v, id, "Favorites");
    }

    /**
     * Adds/removes the movie to the watch later list
     */
    @Override
    public void onWatchLaterClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);
        buttonHandler(v, id, "Watch_Later");
    }

    /**
     * Adds/removes the movie to the watched list
     */
    @Override
    public void onWatchedClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);
        buttonHandler(v, id, "Watched");
    }

    /**
     * Returns the current movie id from context
     */
    private int getMovieId(View v) {
        String idString = ((TextView) v.findViewById(R.id.movieId)).getText().toString();
        return Integer.valueOf(idString);
    }

    /**
     * Handles the updating of the buttons
     */
    private void buttonHandler(View v, int id, String listType) {
        int status = listViewModel.updateMovieInList(id, listType);
        if (status == 1) {
            // Added movie to list
            changeButtonColor((ImageButton) v, R.color.colorAccent);
        } else if (status == -1) {
            // Removed movie from list
            changeButtonColor((ImageButton) v, R.color.text);
        } else {
            // Please log in
            Toast.makeText(v.getContext(), "Please log in to use this feature", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkMovieInList(int id, String listType) {
        return listViewModel.checkMovieInList(id, listType);
    }

    /**
     * Changes the color of the button
     */
    private void changeButtonColor(ImageButton v, int color) {
        ImageButton btn = v;
        btn.setColorFilter(btn.getContext().getResources().getColor(color));
    }

    /**
     * Returns the size of the movie array
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * Returns the filter
     */
    @Override
    public Filter getFilter() {
        return filter;
    }
}
