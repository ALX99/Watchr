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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.MovieListAdapter;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.viewModels.ListViewModel;

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
    private ListViewModel listViewModel;
    private MovieListAdapter movieListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MovieListFragment() {
        // Required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Get view model and set correct data
        listViewModel = ListViewModel.getInstance(this);
        String listType = this.getArguments().getString("listType");
        listViewModel.setListType(listType);
        listViewModel.refresh();

        // Set list layout and adapter
        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListAdapter = MovieListAdapter.getInstance(listViewModel);
        movieList.setAdapter(movieListAdapter);

        // Connect toolbar search and filter
        connectFilterButton();
        connectSearchView(listType);

        observeViewModel();

        // Fetch fresh data from API on refresh
        refreshLayout.setOnRefreshListener(() -> {
//            movieList.setVisibility(View.GONE);
//            listError.setVisibility(View.GONE);
//            listViewModel.refresh(listType);
            refreshLayout.setRefreshing(false);
        });
    }

    /**
     * Makes the fragment listen to the live data in the view model
     */
    private void observeViewModel() {
        listViewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && movies instanceof List) {
                movieListAdapter.updateMovieList(movies);
                movieList.setVisibility(View.VISIBLE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                emptyListView.setVisibility(View.GONE);
                notLoggedInError.setVisibility(View.GONE);
            }
            listViewModel.getMovies().removeObservers(getViewLifecycleOwner());
        });

        listViewModel.getLoggedInStatus().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn instanceof Boolean) {
                if (!isLoggedIn) {
                    movieList.setVisibility(View.GONE);
                    listError.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.GONE);
                    notLoggedInError.setVisibility(View.VISIBLE);
                }
            }
            listViewModel.getLoggedInStatus().removeObservers(getViewLifecycleOwner());
        });

        listViewModel.getEmptyListStatus().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty != null && isEmpty instanceof Boolean) {
                if (isEmpty) {
                    movieList.setVisibility(View.GONE);
                    listError.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                    notLoggedInError.setVisibility(View.GONE);
                }
            }
            listViewModel.getEmptyListStatus().removeObservers(getViewLifecycleOwner());
        });

        listViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                if (isLoading) {
                    movieList.setVisibility(View.GONE);
                    listError.setVisibility(View.GONE);
                    loadingView.setVisibility(View.VISIBLE);
                    emptyListView.setVisibility(View.GONE);
                    notLoggedInError.setVisibility(View.GONE);
                }
            }
            listViewModel.getLoadingStatus().removeObservers(getViewLifecycleOwner());
        });
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

        if (listType.equals(IMovieRepository.BROWSE_LIST)) {
            // If browse, update list directly from API data
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Get live data from query, observe it to make it async and then stop observing it
                    LiveData<List<Movie>> moviesLiveData = listViewModel.getMoviesFromQuery(query);
                    moviesLiveData.observe(getViewLifecycleOwner(), movies -> {
                        if (movies != null) {
                            movieListAdapter.updateMovieList(movies);
                        }
                        moviesLiveData.removeObservers(getViewLifecycleOwner());
                    });
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        } else {
            // Filter current list
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    movieListAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }

    /**
     * Find and setup the filter button
     */
    private void connectFilterButton() {
        // Get the filter button from toolbar and show
        ImageButton filterBtn = getActivity().findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.VISIBLE);

        filterBtn.setOnClickListener(v -> {
            Toast.makeText(this.getContext(), "Clicked the filter button", Toast.LENGTH_SHORT).show();
        });
    }
}
