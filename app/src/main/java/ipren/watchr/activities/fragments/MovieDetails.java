package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.Helpers.ItemOffsetDecoration;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.CastAdapter;
import ipren.watchr.activities.fragments.Adapters.CommentAdapter;
import ipren.watchr.activities.fragments.Adapters.GenreAdapter;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Genre;

public class MovieDetails extends Fragment {

    // Butter knife <3
    @BindView(R.id.castList)
    RecyclerView cast;
    @BindView(R.id.genreList)
    RecyclerView genres;
    @BindView(R.id.commentList)
    RecyclerView comments;

    public MovieDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Our view for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        // Bind stuff with ButterKnife
        ButterKnife.bind(this, view);
        init(view);

        return view;

    }

    private void init(View v) {
        initCast(v);
        initGenres(v);
        initComments(v);

    }

    private void initCast(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext(), LinearLayoutManager.HORIZONTAL, false);
        cast.setLayoutManager(layoutManager);
        CastAdapter adapter = new CastAdapter(getParentFragment().getContext(), dummyData());
        cast.setAdapter(adapter);
    }

    private void initComments(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext(), LinearLayoutManager.VERTICAL, false);
        ItemOffsetDecoration dividerItemDecoration = new ItemOffsetDecoration(genres.getContext(), R.dimen.comment_list_margin);
        comments.addItemDecoration(dividerItemDecoration);
        comments.setLayoutManager(layoutManager);
        CommentAdapter adapter = new CommentAdapter(getParentFragment().getContext(), dummyComment());
        comments.setAdapter(adapter);
    }

    private void initGenres(View v) {
        GridLayoutManager layoutManager = new GridLayoutManager(getParentFragment().getContext(), 2, LinearLayoutManager.HORIZONTAL, false);

        ItemOffsetDecoration dividerItemDecoration = new ItemOffsetDecoration(genres.getContext(), R.dimen.genre_list_margin);
        genres.addItemDecoration(dividerItemDecoration);
        genres.setLayoutManager(layoutManager);
        GenreAdapter adapter = new GenreAdapter(getParentFragment().getContext(), dummyGenre());
        genres.setAdapter(adapter);
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

    private ArrayList<Genre> dummyGenre() {
        ArrayList x = new ArrayList<Genre>();
        x.add(new Genre(27, "Horror"));
        x.add(new Genre(53, "Thriller"));
        x.add(new Genre(9648, "Mystery"));
        return x;
    }

}
