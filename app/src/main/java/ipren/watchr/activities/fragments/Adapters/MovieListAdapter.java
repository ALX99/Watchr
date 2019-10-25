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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ipren.watchr.R;
import ipren.watchr.activities.Util.Util;
import ipren.watchr.activities.fragments.MovieListFragment;
import ipren.watchr.activities.fragments.MovieListFragmentDirections;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.ListViewModel;

/**
 * Class for handling the creation and updating of movie cards in the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> implements Filterable {

    private ListViewModel listViewModel;
    private MovieListFragment fragment;
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
    public MovieListAdapter(ListViewModel listViewModel, MovieListFragment fragment) {
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

    public void updateFilteredMovieList(List<Movie> filteredMovieList) {
        movieList.clear();
        movieList.addAll(filteredMovieList);
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
        int movieIdInt = movieList.get(position).getId();
        String movieIdStr = String.valueOf(movieIdInt);

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
        listViewModel.getGenres(movieIdInt).observe(fragment.getActivity(), genres -> {
            if (genres != null) {
                String genresString = "";
                for (Genre genre : genres) {
                    genresString += genre.getName() + " ";
                }
                genresView.setText("Genres: " + genresString);
                listViewModel.getGenres(movieIdInt).removeObservers(fragment.getActivity());
            }
        });

        // Set UI components
        title.setText(movieList.get(position).title);
        overview.setText(movieList.get(position).overview);
        rating.setText("Rating: " + movieList.get(position).getVoteAverage());

        Util.loadImage(image, "https://image.tmdb.org/t/p//w154" + movieList.get(position).posterPath, Util.getProgressDrawable(image.getContext()));






        // TODO: @johan Refactor this
        // Color watched button
        listViewModel.getUser().observe(fragment, user -> {
            if (user != null) {
                listViewModel.initMovieIdLists();
                listViewModel.getWatchedIds().observe(fragment, ids -> {
                    if (ids != null) {
                        if (Arrays.asList(ids).contains(movieIdStr)) {
                            changeButtonColor(watchedButton, R.color.colorAccent);
                        } else {
                            changeButtonColor(watchedButton, R.color.text);
                        }
                    } else {
                        changeButtonColor(watchedButton, R.color.text);
                    }
                });
            } else {
                changeButtonColor(watchedButton, R.color.text);
            }
        });

        // Color watch later button
        listViewModel.getUser().observe(fragment, user -> {
            if (user != null) {
                listViewModel.initMovieIdLists();
                listViewModel.getWatchLaterIds().observe(fragment, ids -> {
                    if (ids != null) {
                        if (Arrays.asList(ids).contains(movieIdStr)) {
                            changeButtonColor(watchLaterButton, R.color.colorAccent);
                        } else {
                            changeButtonColor(watchLaterButton, R.color.text);
                        }
                    } else {
                        changeButtonColor(watchLaterButton, R.color.text);
                    }
                });
            } else {
                changeButtonColor(watchLaterButton, R.color.text);
            }
        });

        // Color favorites button
        listViewModel.getUser().observe(fragment, user -> {
            if (user != null) {
                listViewModel.initMovieIdLists();
                listViewModel.getFavoritesIds().observe(fragment, ids -> {
                    if (ids != null) {
                        if (Arrays.asList(ids).contains(movieIdStr)) {
                            changeButtonColor(favoriteButton, R.color.colorAccent);
                        } else {
                            changeButtonColor(favoriteButton, R.color.text);
                        }
                    } else {
                        changeButtonColor(favoriteButton, R.color.text);
                    }
                });
            } else {
                changeButtonColor(favoriteButton, R.color.text);
            }
        });






        // Set up on click listeners
        layout.setOnClickListener(v -> {
            MovieListFragmentDirections.ActionDetail action = MovieListFragmentDirections.actionDetail();
            // Pass the movie id
            action.setMovieId(movieList.get(position).id);
            Navigation.findNavController(layout).navigate(action);
        });







        // TODO: @johan Refactor this
        watchedButton.setOnClickListener(v -> {
            if (listViewModel.getUser().getValue() != null) {
                listViewModel.getWatchedIds().observe(fragment, ids -> {
                    if (ids != null) {
                        List<String> idList = Arrays.asList(ids);
                        if (idList.contains(movieIdStr)) {
                            listViewModel.removeMovieFromList(movieIdStr, IUserDataRepository.WATCHED_LIST);
                            changeButtonColor(watchedButton, R.color.text);
                        } else {
                            listViewModel.addMovieToList(movieIdStr, IUserDataRepository.WATCHED_LIST);
                            changeButtonColor(watchedButton, R.color.colorAccent);
                        }
                        listViewModel.getWatchedIds().removeObservers(fragment);
//                        // TODO: @johan Add or remove live if on the user list
                    }
                });
            } else {
                Toast.makeText(fragment.getContext(), "Please log in to use this feature", Toast.LENGTH_SHORT).show();
            }
        });

        watchLaterButton.setOnClickListener(v -> {
            if (listViewModel.getUser().getValue() != null) {
                listViewModel.getWatchLaterIds().observe(fragment, ids -> {
                    if (ids != null) {
                        List<String> idList = Arrays.asList(ids);
                        if (idList.contains(movieIdStr)) {
                            listViewModel.removeMovieFromList(movieIdStr, IUserDataRepository.WATCH_LATER_LIST);
                            changeButtonColor(watchLaterButton, R.color.text);
                        } else {
                            listViewModel.addMovieToList(movieIdStr, IUserDataRepository.WATCH_LATER_LIST);
                            changeButtonColor(watchLaterButton, R.color.colorAccent);
                        }
                        listViewModel.getWatchLaterIds().removeObservers(fragment);
//                        // TODO: @johan Add or remove live if on the user list
                    }
                });
            } else {
                Toast.makeText(fragment.getContext(), "Please log in to use this feature", Toast.LENGTH_SHORT).show();
            }
        });

        favoriteButton.setOnClickListener(v -> {
            if (listViewModel.getUser().getValue() != null) {
                listViewModel.getFavoritesIds().observe(fragment, ids -> {
                    if (ids != null) {
                        List<String> idList = Arrays.asList(ids);
                        if (idList.contains(movieIdStr)) {
                            listViewModel.removeMovieFromList(movieIdStr, IUserDataRepository.FAVORITES_LIST);
                            changeButtonColor(favoriteButton, R.color.text);
                        } else {
                            listViewModel.addMovieToList(movieIdStr, IUserDataRepository.FAVORITES_LIST);
                            changeButtonColor(favoriteButton, R.color.colorAccent);
                        }
                        listViewModel.getFavoritesIds().removeObservers(fragment);
//                        // TODO: @johan Add or remove live if on the user list
                    }
                });
            } else {
                Toast.makeText(fragment.getContext(), "Please log in to use this feature", Toast.LENGTH_SHORT).show();
            }
        });
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

    /**
     * Return the movie list
     */
    public List<Movie> getMovieListFull() {
        return movieListFull;
    }
}
