package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.MovieListAdapter;
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

        connectFilterButton();
        connectSearchView();

        // This creates a new instance every time because of how bottom navigation works
//        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel = ListViewModel.getInstance(this);
        movieListAdapter = new MovieListAdapter(new ArrayList<>(), listViewModel);
        String listType = this.getArguments().getString("listType");

        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        movieList.setAdapter(movieListAdapter);

        // Navigate based on list and login status
        if (listType.equals("Browse") || listType.equals("Recommended") || listViewModel.getUser().getValue() != null) {
            listViewModel.refresh(listType);
            observeViewModel();
        } else {
            listError.setVisibility(View.GONE);
            movieList.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            notLoggedInError.setVisibility(View.VISIBLE);
        }

        // Fetch fresh data from API on refresh
        refreshLayout.setOnRefreshListener(() -> {
            movieList.setVisibility(View.GONE);
            listError.setVisibility(View.GONE);
            listViewModel.refresh(listType);
            refreshLayout.setRefreshing(false);
        });
    }

    /**
     * Makes the fragment listen to the live data in the view model
     */
    private void observeViewModel() {
        listViewModel.getMovies().observe(this, movies -> {
            if (movies != null && movies instanceof List) {
                movieList.setVisibility(View.VISIBLE);
                movieListAdapter.updateMovieList(movies);
            }
        });

        listViewModel.getMovieLoadError().observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        listViewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    movieList.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.GONE);
                }
            }
        });

        listViewModel.getEmptyListStatus().observe(this, isEmptyList -> {
            if (isEmptyList != null && isEmptyList instanceof Boolean) {
                emptyListView.setVisibility(isEmptyList ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Find and setup the search bar
     */
    private void connectSearchView() {
        // Get the search view from toolbar and show
        SearchView searchView = getActivity().findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.VISIBLE);

        // Clear text and focus
        searchView.setQuery("", false);
        searchView.clearFocus();

        // Get rid of magnifying glass on keyboard.
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Filter on input
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

    /**
     * Find and setup the filter button
     */
    private void connectFilterButton() {
        // Get the filter button from toolbar and show
        ImageButton filterBtn = getActivity().findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.VISIBLE);

        filterBtn.setOnClickListener(v -> {
            Log.d("TEST", "Clicked the filter button");
        });
    }
}
