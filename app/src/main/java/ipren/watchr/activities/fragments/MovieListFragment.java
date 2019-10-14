package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private ListViewModel viewModel;
    private MovieListAdapter movieListAdapter = new MovieListAdapter(new ArrayList<>());

    @BindView(R.id.movieList)
    RecyclerView movieList;

    @BindView(R.id.listError)
    TextView listError;

    @BindView(R.id.loadingView)
    ProgressBar loadingView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    public MovieListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        // Bind the layout variables
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getContext(), this.getArguments().getString("listType"), Toast.LENGTH_SHORT).show();

        String url = "";

        // For testing purposes
        switch (this.getArguments().getString("listType")) {
            case "browse": url = "movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=1"; break;
            case "recommended": url = "movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=2"; break;
            case "watchLater": url = "movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=3"; break;
            case "watched": url = "movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=4"; break;
            case "favorites": url = "movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=5"; break;
        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh(url);

        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        movieList.setAdapter(movieListAdapter);

        // Fetch fresh data from API on refresh
        refreshLayout.setOnRefreshListener(() -> {
            movieList.setVisibility(View.GONE);
            listError.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            viewModel.refresh("movie/top_rated?api_key=75e28ea896c86e2d5ef78b91e8500e22&language=en-US&page=1");
            refreshLayout.setRefreshing(false);
        });

        observeViewModel();
    }

    /**
     * Makes the fragment listen to the live data in the view model
     */
    private void observeViewModel() {
        viewModel.movies.observe(this, movies -> {
            if (movies != null && movies instanceof List) {
                movieList.setVisibility(View.VISIBLE);
                movieListAdapter.updateMovieList(movies);
            }
        });

        viewModel.movieLoadError.observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.loading.observe(this, isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    movieList.setVisibility(View.GONE);
                }
            }
        });
    }
}
