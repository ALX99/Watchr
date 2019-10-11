package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.Helpers.ItemOffsetDecoration;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.CastAdapter;
import ipren.watchr.activities.fragments.Adapters.CommentAdapter;
import ipren.watchr.activities.fragments.Adapters.GenreAdapter;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.viewModels.IMovieViewModel;
import ipren.watchr.viewModels.MovieViewModel;

public class MovieDetails extends Fragment {
    private int movieID;
    private IMovieViewModel viewModel;
    // Butter knife <3
    @BindView(R.id.castList)
    RecyclerView cast;
    @BindView(R.id.genreList)
    RecyclerView genres;
    @BindView(R.id.commentList)
    RecyclerView comments;
    @BindView(R.id.send)
    ImageView send;

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
        return view;
    }

    private void init(View v) {
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);


        initMovie();
        // init methods below work the same way
        // They add a LinearLayoutManager, then optionally an
        // ItemDecoration and then inits the adapter and associated it with the RecycleView.
        initCast();
        initGenres();
        initComments();

    }

    private void initMovie() {
        viewModel.getMovie(movieID).observe(this, Movie -> {
            // Don't try to set anything if the object is null.
            // Will result in fatal crash
            if (Movie == null)
                return;

            // Load images
            Util.loadImage((getView().findViewById(R.id.poster)), new StringBuilder("https://image.tmdb.org/t/p/w500").append(Movie.posterPath).toString(), Util.getProgressDrawable(requireContext())); // Poster
            Util.loadImage((getView().findViewById(R.id.cover)), new StringBuilder("https://image.tmdb.org/t/p/w500").append(Movie.getBackdropPath()).toString(), Util.getProgressDrawable(requireContext())); // Poster

            // Title
            ((TextView) getView().findViewById(R.id.movieTitle)).setText(Movie.title);
            // Overview
            ((TextView) getView().findViewById(R.id.description)).setText(Movie.overview);
            // Release date
            ((TextView) getView().findViewById(R.id.releaseDate)).setText(Movie.getReleaseDate());
            // Vote score
            ((TextView) getView().findViewById(R.id.ratingText)).setText(Double.toString(Movie.getVoteAverage()));
            ((ProgressBar) getView().findViewById(R.id.rating)).setProgress((int) Math.round(Movie.getVoteAverage()));
            // Popularity
            int pop = (int) Math.round(Movie.getPopularity());
            ((TextView) getView().findViewById(R.id.popularityText)).setText(Integer.toString(pop));
            ((ProgressBar) getView().findViewById(R.id.popularity)).setProgress(pop);


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
        CommentAdapter adapter = new CommentAdapter(requireContext(), dummyComment());
        comments.setAdapter(adapter);

        // OnClickListener to send comments
        send.setOnClickListener((View v) -> {
            String text = ((EditText) getView().findViewById(R.id.commentEdit)).getText().toString();
            // TODO
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        });
    }

    private void initGenres() {
        genres.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false));
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


    private ArrayList<Comment> dummyComment() {
        ArrayList x = new ArrayList<Comment>();
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));
        x.add(new Comment(0, 0, "User", "Example comment", ""));

        return x;
    }
}
