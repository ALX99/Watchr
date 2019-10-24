package ipren.watchr.activities.fragments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ipren.watchr.R;
import ipren.watchr.activities.Util.Util;
import ipren.watchr.activities.fragments.MovieListFragmentDirections;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.viewModels.ListViewModel;

/**
 * Class for handling the creation and updating of movie cards in the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> implements Filterable {

    private ListViewModel listViewModel;
    private Fragment fragment;
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
                String filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim();

                for (Movie movie : movieListFull) {
                    if (movie.title.toLowerCase(Locale.getDefault()).contains(filterPattern)) {
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
     * Creates a movie adapter with a list of movies
     */
    public MovieListAdapter(ListViewModel listViewModel, Fragment fragment) {
        this.movieList = new ArrayList<>();
        this.listViewModel = listViewModel;
        this.fragment = fragment;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Gets called automatically to update a view holder when scrolling
     */
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        int movieId = movieList.get(position).getId();

        // Get all UI components from movie card
        ImageView image = holder.itemView.findViewById(R.id.movieImage);
        TextView title = holder.itemView.findViewById(R.id.movieTitle);
        TextView overview = holder.itemView.findViewById(R.id.movieOverview);
        TextView rating = holder.itemView.findViewById(R.id.movieRating);
        TextView genresView = holder.itemView.findViewById(R.id.movieGenres);
        ConstraintLayout layout = holder.itemView.findViewById(R.id.movieLayout);
        ImageButton watchedButton = holder.itemView.findViewById(R.id.watchedButton);
        ImageButton watchLaterButton = holder.itemView.findViewById(R.id.watchLaterButton);
        ImageButton favoriteButton = holder.itemView.findViewById(R.id.favoriteButton);

        // Fetch genres and set to view
        listViewModel.getGenres(movieId).observe(fragment.getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                String genresString = "";
                for (Genre genre : genres) {
                    genresString += genre.getName() + " ";
                }
                genresView.setText("Genres: " + genresString);
                listViewModel.getGenres(movieId).removeObservers(fragment.getViewLifecycleOwner());
            }
        });

        // Set UI components
        title.setText(movieList.get(position).title);
        overview.setText(movieList.get(position).overview);
        rating.setText("Rating: " + movieList.get(position).getVoteAverage());

        // TODO: @johan Set genres
//        listViewModel.getGenres(movieId).observe(holder, genres -> {
//
//        });

        Util.loadImage(image, "https://image.tmdb.org/t/p//w154" + movieList.get(position).posterPath, Util.getProgressDrawable(image.getContext()));
        // TODO: @johan Color the buttons correctly

        // Set up on click listeners
        layout.setOnClickListener(v -> {
            MovieListFragmentDirections.ActionDetail action = MovieListFragmentDirections.actionDetail();
            // Pass the movie id
            action.setMovieId(movieList.get(position).id);
            Navigation.findNavController(layout).navigate(action);
        });

        watchedButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Added " + movieId + " to watched", Toast.LENGTH_SHORT).show();
        });

        watchLaterButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Added " + movieId + " to watch later", Toast.LENGTH_SHORT).show();
        });

        favoriteButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Added " + movieId + " to favorites", Toast.LENGTH_SHORT).show();
        });
    }

//
//    /**
//     * Adds/removes the movie to the favorite list
//     */
//    public void onFavoriteClicked(View v) {
//        ConstraintLayout parent = (ConstraintLayout) v.getParent();
//        int id = getMovieId(parent);
//        // TODO: @johan Fix multiple buttons being highlighted because ViewHolders is being reused
//        buttonHandler(v, id, "Favorites");
//    }
//
//    /**
//     * Adds/removes the movie to the watch later list
//     */
//    public void onWatchLaterClicked(View v) {
//        ConstraintLayout parent = (ConstraintLayout) v.getParent();
//        int id = getMovieId(parent);
//        buttonHandler(v, id, "Watch_Later");
//    }
//
//    /**
//     * Adds/removes the movie to the watched list
//     */
//    public void onWatchedClicked(View v) {
//        ConstraintLayout parent = (ConstraintLayout) v.getParent();
//        int id = getMovieId(parent);
//        buttonHandler(v, id, "Watched");
//    }
//
//    /**
//     * Returns the current movie id from context
//     */
//    private int getMovieId(View v) {
//        String idString = ((TextView) v.findViewById(R.id.movieId)).getText().toString();
//        return Integer.valueOf(idString);
//    }

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
