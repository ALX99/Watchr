package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.MovieListAdapter;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.MovieFilter;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewmodels.ListViewModel;

public class MovieListFragment extends Fragment {

    @BindView(R.id.movieList)
    RecyclerView movieList;
    @BindView(R.id.listError)
    TextView listError;
    @BindView(R.id.notLoggedInError)
    TextView notLoggedInError;
    @BindView(R.id.loadingView)
    ProgressBar loadingView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.emptyListView)
    TextView emptyListView;

    private int currentPage = 1;
    private ListViewModel listViewModel;
    private MovieListAdapter movieListAdapter;
    // Observer to update the movieList
    private Observer<List<Movie>> movieObserver = new Observer<List<Movie>>() {
        @Override
        public void onChanged(List<Movie> movies) {
            if (movies != null) {
                if (currentPage == 1)
                    movieListAdapter.updateMovieList(movies);
                else // If not on page 1 we want to load more movies
                    movieListAdapter.addMoreMovies(movies);
                loadingView.setVisibility(View.GONE);
            }
        }
    };

    public MovieListFragment() {
        // Required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        // Bind the layout variables
        ButterKnife.bind(this, view);

        return view;
    }

    /**
     * Sets up the list, decides which list to display.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPage = 1; // Set currentPage to 1

        // Get view model and set correct data
        listViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        String listType = this.getArguments().getString("listType");

        if (listViewModel.getUser().getValue() != null) {
            listViewModel.initMovieIdLists();
        }

        // Set list layout and adapter
        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Scroll listener for loading more movies
        movieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                // When reached bottom of the recyclerView
                if (!recyclerView.canScrollVertically(1) && (listType.equals(IMovieRepository.RECOMMENDED_LIST) || listType.equals(IMovieRepository.BROWSE_LIST))) {
                    Toast.makeText(getContext(), "Loading more movies...", Toast.LENGTH_SHORT).show();
                    currentPage++;
                    loadMovies(listType);
                }
            }
        });
        movieListAdapter = new MovieListAdapter(listViewModel, this);
        movieList.setAdapter(movieListAdapter);

        connectFilterButton();
        connectSearchView(listType);

        loadMovies(listType);

        // Fetch fresh data from API on refresh
        refreshLayout.setOnRefreshListener(() -> {
            loadMovies(listType);
            refreshLayout.setRefreshing(false);
        });
    }

    /**
     * Loads the correct movies according to list type
     */
    private void loadMovies(String listType) {
        switch (listType) {
            case IMovieRepository.BROWSE_LIST:
                listViewModel.initBrowse(currentPage);
                break;
            case IMovieRepository.RECOMMENDED_LIST:
                listViewModel.initRecommended(currentPage);
                break;
            default:
                handleUserList(listType);
                listViewModel.getUser().observe(this, user -> handleUserList(listType));
        }
        listViewModel.getMovies().observe(getActivity(), movieObserver);
    }

    /**
     * Sets up the correct user list based on list type
     */
    private void handleUserList(String listType) {
        // Check if user is logged in
        if (listViewModel.getUser().getValue() != null) {
            // Get the movie ids from user repo
            switch (listType) {
                case IUserDataRepository.WATCHED_LIST:
                    listViewModel.getWatchedIds().observe(this, this::observeIds);
                    break;
                case IUserDataRepository.WATCH_LATER_LIST:
                    listViewModel.getWatchLaterIds().observe(this, this::observeIds);
                    break;
                case IUserDataRepository.FAVORITES_LIST:
                    listViewModel.getFavoritesIds().observe(this, this::observeIds);
                    break;
            }
        } else {
            // Not logged in
            notLoggedInError.setVisibility(View.VISIBLE);
            movieList.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.GONE);
        }
    }

    /**
     * Observe the movie ids returned from user repo
     *
     * @param ids Ids to be observed
     */
    public void observeIds(String[] ids) {
        if (ids != null) {
            listViewModel.initUserMovieList(ids);
            listViewModel.getMovies().observe(this, movies -> {
                if (movies != null) {
                    movieListAdapter.updateMovieList(movies);
                    if (movies.size() == 0) {
                        emptyListView.setVisibility(View.VISIBLE);
                    } else {
                        movieList.setVisibility(View.VISIBLE);
                    }
                    notLoggedInError.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * Find and setup the search bar
     */
    private void connectSearchView(String listType) {
        // Get the search view from toolbar and show
        SearchView searchView = getActivity().findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.VISIBLE);

        // Clear text and focus
        searchView.setQuery("", false);
        searchView.clearFocus();

        // Get rid of magnifying glass on keyboard.
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listType.equals(IMovieRepository.BROWSE_LIST)) {
                    // Remove the current observer
                    listViewModel.getMovies().removeObserver(movieObserver);
                    // Load new movies from a query
                    listViewModel.getMoviesFromQuery(query, currentPage);
                    // Observe them
                    listViewModel.getMovies().observe(getActivity(), movieObserver);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!listType.equals(IMovieRepository.BROWSE_LIST)) {
                    movieListAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    /**
     * Find and setup the filter button
     */
    private void connectFilterButton() {
        // Get the filter button from toolbar and show
        ImageButton filterBtn = getActivity().findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.VISIBLE);
        DrawerLayout filterDrawer = getActivity().findViewById(R.id.filter_drawer);

        filterBtn.setOnClickListener(v -> {
            if (filterDrawer.isDrawerOpen(GravityCompat.END)) {
                // Perform filtering
                MovieFilter movieFilter = MovieFilter.getInstance();
                List<Movie> movies = movieListAdapter.getMovieListFull();
                List<Movie> filteredMovies = new ArrayList<>(movies);

                for (Movie movie : movies) {
                    // Check rating
                    if (!(movie.getVoteAverage() >= movieFilter.getRating())) {
                        filteredMovies.remove(movie);
                    }
                }

                // Order by
                switch (movieFilter.getOrderBy()) {
                    case "Popularity":
                        Collections.sort(filteredMovies, (a, b) -> {
                            double result = a.getPopularity() - b.getPopularity();
                            return orderByDesc(result);
                        });
                        break;
                    case "Rating":
                        Collections.sort(filteredMovies, (a, b) -> {
                            double result = a.getVoteAverage() - b.getVoteAverage();
                            return orderByDesc(result);
                        });
                        break;
                    case "Title":
                        Collections.sort(filteredMovies, (a, b) -> {
                            double result = a.getTitle().charAt(0) - b.getTitle().charAt(0);
                            return orderByAsc(result);
                        });
                        break;
                }

                movieListAdapter.updateFilteredMovieList(filteredMovies);

                filterDrawer.closeDrawer(GravityCompat.END);
                filterDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                filterDrawer.openDrawer(GravityCompat.END);
                filterDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }
        });
    }

    private int orderByDesc(double result) {
        if (result < 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    private int orderByAsc(double result) {
        if (result < 0) {
            return -1;
        } else if (result == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
