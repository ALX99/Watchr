package ipren.watchr.activities.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.MovieListAdapter;
import ipren.watchr.viewModels.ListViewModel;

public class BrowseFragment extends Fragment {

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


    public BrowseFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        // Bind the layout variables
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        movieList.setLayoutManager(new LinearLayoutManager(getContext()));
        movieList.setAdapter(movieListAdapter);

        observeViewModel();
    }

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
