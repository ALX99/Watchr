package ipren.watchr.activities.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.Constants;
import ipren.watchr.R;
import ipren.watchr.activities.Util.ItemOffsetDecoration;
import ipren.watchr.activities.Util.Util;
import ipren.watchr.activities.fragments.Adapters.CastAdapter;
import ipren.watchr.activities.fragments.Adapters.CommentAdapter;
import ipren.watchr.activities.fragments.Adapters.GenreAdapter;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IUserDataRepository;
import ipren.watchr.viewModels.IMovieViewModel;
import ipren.watchr.viewModels.MovieViewModel;

/**
 * The type Movie details.
 */
public class MovieDetails extends Fragment {
    private int movieID;
    private IMovieViewModel viewModel;
    private User user;
    private NavController navController;


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
    @BindView(R.id.adult)
    TextView adult;
    @BindView(R.id.language)
    TextView language;
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

        // Gets viewModel and sets the movieID
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.setMovieID(movieID);

        // Inits stuff
        initUser();
        initMovie();
        initOurRating();
        initCheckBoxes();
        initCast();
        initGenres();
        initComments();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupScrolling();
        hideSearchAndFilter();

        // Set up navigation
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }

    private void hideSearchAndFilter() {
        // Get the search view from toolbar and hide
        SearchView searchView = getActivity().findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.GONE);

        // Get the filter button from toolbar and show
        ImageButton filterBtn = getActivity().findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.GONE);
    }

    private void initOurRating() {
        // Create a alert dialog builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.create();

        ourRating.setOnClickListener(v -> {
            // Get the rating view
            View ratingView = getLayoutInflater().inflate(R.layout.rating_layout, null);
            // Get the RatingBar
            AppCompatRatingBar rating = ratingView.findViewById(R.id.setRating);
            // Set rating view in the builder
            builder.setView(ratingView);
            builder.show();

            // RatingBar change listener; send request to rate movie to FireBase
            rating.setOnRatingBarChangeListener((ratingBar, rating1, fromUser) -> {
                if (checkLoggedIn() && checkVerified() && fromUser) {
                    viewModel.rateMovie(Math.round(rating1 * 2), user.getUID()); // Rate the movie
                    Toast.makeText(getContext(), "You rated the movie " + rating1 + " stars!", Toast.LENGTH_SHORT).show();
                }
            });

            // Display the average of our ratings from FireBase in the rating
            // AppCompactRatingBar layout
            viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
                if (ratings != null) {
                    for (Rating f : ratings) {
                        Log.d("RATING", f.getUser_id());
                        if (user != null && f.getUser_id().equals(user.getUID()))
                            rating.setRating((float) (f.getScore() / 2));
                    }
                }
            });
        });

        // Display the average of our ratings in the movieDetails layout
        viewModel.getRatings().observe(getViewLifecycleOwner(), fireRatings -> {
            double avg = 0;
            double num = 0;
            if (fireRatings != null) {
                for (Rating f : fireRatings)
                    avg += f.getScore();
                if (fireRatings.length > 0)
                    num = avg / fireRatings.length;
                ourRatingText.setText(new DecimalFormat("#.#").format(num));
                ourRating.setProgress((int) Math.round(num));
            }
        });
    }

    /**
     * Starts observing the user object and sets stuff according according to the user object
     */
    private void initUser() {
        // Observer user
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            if (user != null) {
                // Load pic
                Glide.with(getContext())
                        .load(user.getUserProfilePictureUri().toString())
                        .into(profilePicture);
                // Set checkboxes to match data from firebase
                viewModel.getUserList(IUserDataRepository.FAVORITES_LIST, user.getUID()).observe(getViewLifecycleOwner(), result -> {
                    if (Arrays.asList(result).contains(Integer.toString(movieID)))
                        favoriteCheckbox.setChecked(true);
                });
                viewModel.getUserList(IUserDataRepository.WATCHED_LIST, user.getUID()).observe(getViewLifecycleOwner(), result -> {
                    if (Arrays.asList(result).contains(Integer.toString(movieID)))
                        watchedCheckbox.setChecked(true);
                });
                viewModel.getUserList(IUserDataRepository.WATCH_LATER_LIST, user.getUID()).observe(getViewLifecycleOwner(), result -> {
                    if (Arrays.asList(result).contains(Integer.toString(movieID)))
                        watchLaterCheckbox.setChecked(true);
                });

            } else {
                profilePicture.setImageDrawable(ContextCompat.getDrawable(getContext(), getContext().getResources().getIdentifier("default_profile_photo", "drawable", "ipren.watchr")));
                favoriteCheckbox.setChecked(false);
                watchedCheckbox.setChecked(false);
                watchLaterCheckbox.setChecked(false);
            }
        });
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

    /**
     * Observes the movie and displays all the information
     */
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
            // Adult
            adult.setText(String.format(getResources().getString(R.string.adult), Movie.isAdult()));
            // Language
            language.setText(String.format(getResources().getString(R.string.language), Movie.getOriginalLanguage()));
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

    /**
     * Sets onCheckedChangeListeners to the checkboxes
     */
    private void initCheckBoxes() {
        favoriteCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn() && checkVerified()) {
                if (isChecked)
                    viewModel.addMovieToList(IUserDataRepository.FAVORITES_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(IUserDataRepository.FAVORITES_LIST, user.getUID());
            } else
                favoriteCheckbox.setChecked(false);
        });
        watchedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn() && checkVerified()) {
                if (isChecked)
                    viewModel.addMovieToList(IUserDataRepository.WATCHED_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(IUserDataRepository.WATCHED_LIST, user.getUID());
            } else
                watchedCheckbox.setChecked(false);
        });
        watchLaterCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkLoggedIn() && checkVerified()) {
                if (isChecked)
                    viewModel.addMovieToList(IUserDataRepository.WATCH_LATER_LIST, user.getUID());
                else
                    viewModel.removeMovieFromList(IUserDataRepository.WATCH_LATER_LIST, user.getUID());
            } else
                watchLaterCheckbox.setChecked(false);
        });
    }

    /**
     * Sets up the recyclerView with some style and its adapter, then start observing the actors
     */
    private void initCast() {
        cast.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CastAdapter adapter = new CastAdapter();
        cast.setAdapter(adapter);

        viewModel.getActors().observe(getViewLifecycleOwner(), actors -> {
            adapter.setData(actors);
        });
    }

    /**
     * Sets up the recyclerView with some style and its adapter, then start observing the comments
     */
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
            if (checkLoggedIn() && checkVerified()) {
                viewModel.commentOnMovie(user.getUID(), comment.getText().toString());
                comment.setText("");
                comment.clearFocus();
            }
        });
    }

    /**
     * Sets up the recyclerView with some style and its adapter, then start observing the genres
     */
    private void initGenres() {
        genres.setLayoutManager(new GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        genres.addItemDecoration(new ItemOffsetDecoration(genres.getContext(), R.dimen.genre_list_margin));
        GenreAdapter adapter = new GenreAdapter();
        genres.setAdapter(adapter);


        viewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            adapter.setData(genres);
        });
    }

    /**
     * checks if a user is logged in
     *
     * @return true if a user is logged in, false otherwise
     */
    private boolean checkLoggedIn() {
        if (user != null) return true;
        Toast.makeText(getContext(), "You need to be logged in to use this feature!", Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * Checks if a user is verified
     *
     * @return true if a user is verified, false otherwise
     */
    private boolean checkVerified() {
        if (!user.isVerified()) {
            Toast.makeText(getContext(), "You need to verify your account to use this feature!", Toast.LENGTH_LONG).show();
            navController.navigate(R.id.accountSettingsFragment);
            return false;
        }
        return true;

    }
}
