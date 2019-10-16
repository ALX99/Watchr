package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.Constants;
import ipren.watchr.Helpers.ItemOffsetDecoration;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.CastAdapter;
import ipren.watchr.activities.fragments.Adapters.CommentAdapter;
import ipren.watchr.activities.fragments.Adapters.GenreAdapter;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMainRepository;
import ipren.watchr.viewModels.IMovieViewModel;
import ipren.watchr.viewModels.MovieViewModel;

public class MovieDetails extends Fragment {
    @BindView(R.id.castList)
    RecyclerView cast;
    @BindView(R.id.genreList)
    RecyclerView genres;
    @BindView(R.id.commentList)
    RecyclerView comments;
    @BindView(R.id.send)
    ImageView send;
    @BindView(R.id.posterCardView)
    View posterCardView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.commentEdit)
    EditText comment;
    @BindView(R.id.descriptionScrollView)
    ScrollView descriptionScrollView;
    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.cover)
    ImageView cover;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.movieTitle)
    TextView title;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    @BindView(R.id.ratingText)
    TextView ratingText;
    @BindView(R.id.rating)
    ProgressBar rating;
    @BindView(R.id.popularityText)
    TextView popularityText;
    @BindView(R.id.popularity)
    ProgressBar popularity;
    @BindView(R.id.runtime)
    TextView runtime;
    private int movieID;
    private IMovieViewModel viewModel;
    private IMainRepository mainRepository;
    private User user;


    public MovieDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get movieID argument
        this.movieID = getArguments().getInt("movieId");

        // Our view for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        // Bind stuff with ButterKnife
        ButterKnife.bind(this, view);
        Toast.makeText(getContext(), Integer.toString(movieID), Toast.LENGTH_SHORT).show(); // Debug
        init(view);
        hideSearchAndFilter();
        return view;
    }

    private void hideSearchAndFilter() {
        // Get the search view from toolbar and hide
        SearchView searchView = getActivity().findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.GONE);

        // Get the filter button from toolbar and show
        ImageButton filterBtn = getActivity().findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.GONE);
    }

    private void init(View v) {
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mainRepository = IMainRepository.getMainRepository();
        mainRepository.getUserLiveData().observe(this, user -> {
            this.user = user;
        });

        setupScrolling();
        initMovie();
        // init methods below work the same way
        // They add a LinearLayoutManager, then optionally an
        // ItemDecoration and then inits the adapter and associated it with the RecycleView.
        initCast();
        initGenres();
        initComments();


    }

    // Don't ask me why this boilerplate code is needed to setup a scrollable
    // TextView inside a nested scrollview. But if this isn't added scrolling
    // inside descriptionScrollView is near impossible
    private void setupScrolling() {
        descriptionScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                descriptionScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }

    private void initMovie() {
        viewModel.getMovie(movieID).observe(this, Movie -> {
            // Don't try to set anything if the object is null.
            // Will result in fatal crash
            if (Movie == null || Movie.overview == null)
                return;

            // Load images
            Util.loadImage(poster, new StringBuilder(Constants.MOVIE_DB_GET_IMAGE).append(Movie.posterPath).toString(), Util.getProgressDrawable(requireContext())); // Poster
            Util.loadImage(cover, new StringBuilder(Constants.MOVIE_DB_GET_IMAGE).append(Movie.getBackdropPath()).toString(), Util.getProgressDrawable(requireContext())); // Cover

            // Title
            title.setText(Movie.title);
            // Overview
            description.setText(Movie.overview);
            // Status
            status.setText(String.format(getResources().getString(R.string.status), Movie.getStatus()));
            // Runtime
            runtime.setText(String.format(getResources().getString(R.string.runtime), Movie.getRuntime()));
            // Release date
            releaseDate.setText(Movie.getReleaseDate());
            // Vote score
            ratingText.setText(Double.toString(Movie.getVoteAverage()));
            rating.setProgress((int) Math.round(Movie.getVoteAverage()));
            // Popularity
            int pop = (int) Math.round(Movie.getPopularity());
            popularityText.setText(Integer.toString(pop));
            popularity.setProgress(pop);


        });

    }

    private void initCast() {
        cast.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CastAdapter adapter = new CastAdapter(getParentFragment().getContext(), dummyData());
        cast.setAdapter(adapter);
    }

    private void initComments() {
        comments.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        comments.addItemDecoration(new ItemOffsetDecoration(genres.getContext(), R.dimen.comment_list_margin));
        CommentAdapter adapter = new CommentAdapter(requireContext());
        comments.setAdapter(adapter);

        mainRepository.getComments(Integer.toString(movieID), IMainRepository.SEARCH_METHOD_MOVIE_ID).observe(this, comments -> {
            adapter.setData(comments);
        });
        // OnClickListener to send comments
        send.setOnClickListener((View v) -> {
            // TODO
            String text = comment.getText().toString();
            comment.setText("");
            comment.clearFocus();
            if (user != null)
                mainRepository.commentMovie(text, Integer.toString(movieID), user.getUID(), null);
            else
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        });
    }

    private void initGenres() {
        genres.setLayoutManager(new GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        genres.addItemDecoration(new ItemOffsetDecoration(genres.getContext(), R.dimen.genre_list_margin));
        GenreAdapter adapter = new GenreAdapter();
        genres.setAdapter(adapter);

        // Observe LiveData
        // If the Lifecycle object is not in an active state, then the observer isn't called even if the value changes.
        // After the Lifecycle object is destroyed, the observer is automatically removed.
        // So no need to unsub?
        viewModel.getGenres(movieID).observe(this, genres -> {
            adapter.setData(genres);
        });
    }

    private ArrayList<Actor> dummyData() {
        ArrayList x = new ArrayList<Actor>();
        x.add(new Actor(0, "Patrick Wilson", "example", 1, "/djhTpbOvrfdDsWZFFintj2Uv47a.jpg"));
        x.add(new Actor(0, "Vera Farmiga", "example", 1, "/oWZfxv4cK0h8Jcyz1MvvT2osoAP.jpg"));
        x.add(new Actor(0, "Mckenna Grace", "example", 1, "/dX6QFwpAzAcXGgxSINwvDxujEgj.jpg"));
        x.add(new Actor(0, "Madison Iseman", "example", 1, "/qkPW0nHQUlckRj3MRveVTzRpNR2.jpg"));
        x.add(new Actor(0, "Katie Sarife", "example", 1, "/oQLQZ58uvGgpdtCUpOcoiF5zYJW.jpg"));
        return x;
    }

}
