package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.Constants;
import ipren.watchr.Helpers.ItemOffsetDecoration;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.CastAdapter;
import ipren.watchr.activities.fragments.Adapters.CommentAdapter;
import ipren.watchr.activities.fragments.Adapters.GenreAdapter;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.IMovieViewModel;
import ipren.watchr.viewModels.MovieViewModel;

public class MovieDetails extends Fragment {
    private int movieID;
    private IMovieViewModel viewModel;

    private IUserDataRepository mainRepository;
    private User user;

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
    @BindView(R.id.watched)
    AppCompatCheckBox watchedCheckbox;
    @BindView(R.id.watchLater)
    AppCompatCheckBox watchLaterCheckbox;
    @BindView(R.id.favorite)
    AppCompatCheckBox favoriteCheckbox;
    @BindView(R.id.ourRatingText)
    TextView ourRatingText;
    @BindView(R.id.ourRating)
    ProgressBar ourRating;
    @BindView(R.id.profile_picture)
    CircleImageView profilePicture;

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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupScrolling();
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        viewModel.setMovieID(movieID);


        // Observer user
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {

            this.user = user;
            if (checkLoggedIn()) {
                String pic = user.getUserProfilePictureUri().toString();
                Glide.with(getContext())
                        .load(pic)
                        .error(ContextCompat.getDrawable(getContext(), getView().getResources().getIdentifier("default_profile_photo", "drawable", "ipren.watchr")))
                        .into(profilePicture);


                viewModel.getUserList(IUserDataRepository.FAVORITES_LIST, user.getUID()).observe(getViewLifecycleOwner(), result -> {
                    if (result != null)
                        Log.d("INFO", Arrays.toString(result));
                });
            } else
                profilePicture.setImageDrawable(ContextCompat.getDrawable(getContext(), getContext().getResources().getIdentifier("default_profile_photo", "drawable", "ipren.watchr")));
        });

        initMovie();
        initCheckBoxes();
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
        viewModel.getMovie().observe(getViewLifecycleOwner(), Movie -> {
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
            // Our Rating
        });
    }

    private void initCheckBoxes() {
        favoriteCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn()) {
                if (isChecked)
                    viewModel.addMovieToList(movieID, IUserDataRepository.FAVORITES_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(movieID, IUserDataRepository.FAVORITES_LIST, user.getUID());
            } else
                favoriteCheckbox.setChecked(!isChecked);
        });
        watchedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn()) {
                if (isChecked)
                    viewModel.addMovieToList(movieID, IUserDataRepository.WATCHED_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(movieID, IUserDataRepository.WATCHED_LIST, user.getUID());
            } else
                watchedCheckbox.setChecked(!isChecked);
        });
        watchLaterCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn()) {
                if (isChecked)
                    viewModel.addMovieToList(movieID, IUserDataRepository.WATCH_LATER_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(movieID, IUserDataRepository.WATCH_LATER_LIST, user.getUID());
            } else
                watchLaterCheckbox.setChecked(!isChecked);
        });
    }
    private void initCast() {

        cast.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CastAdapter adapter = new CastAdapter();
        cast.setAdapter(adapter);

        viewModel.getActors().observe(getViewLifecycleOwner(), actors -> {
            adapter.setData(actors);
        });
    }

    private void initComments() {
        comments.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        comments.addItemDecoration(new ItemOffsetDecoration(genres.getContext(), R.dimen.comment_list_margin));
        CommentAdapter adapter = new CommentAdapter(requireContext(), viewModel, getViewLifecycleOwner());
        comments.setAdapter(adapter);


        viewModel.getComments().observe(getViewLifecycleOwner(), comments -> {

            adapter.setData(comments);
        });

        // OnClickListener to send comments
        send.setOnClickListener((View v) -> {
            if (checkLoggedIn()) {
            viewModel.commentOnMovie(movieID, user.getUID(), comment.getText().toString());
            comment.setText("");
                comment.clearFocus();
            }
        });
    }

    private void initGenres() {
        genres.setLayoutManager(new GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        genres.addItemDecoration(new ItemOffsetDecoration(genres.getContext(), R.dimen.genre_list_margin));
        GenreAdapter adapter = new GenreAdapter();
        genres.setAdapter(adapter);


        viewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            adapter.setData(genres);
        });
    }

    private boolean checkLoggedIn() {
        if (user != null) return true;
        Toast.makeText(getContext(), "You need to be logged in to use this feature!", Toast.LENGTH_LONG).show(); // Debug
        return false;
    }
}
