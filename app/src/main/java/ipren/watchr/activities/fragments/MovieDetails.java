package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.fragments.Adapters.ProductionAdapter;
import ipren.watchr.dataHolders.Actor;

public class MovieDetails extends Fragment {

    @BindView(R.id.castList)
    RecyclerView cast;

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
    }

    private void initCast(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentFragment().getContext(), LinearLayoutManager.HORIZONTAL, false);
        cast.setLayoutManager(layoutManager);
        ProductionAdapter adapter = new ProductionAdapter(getParentFragment().getContext(), dummyData());
        cast.setAdapter(adapter);
    }

    private ArrayList<Actor> dummyData() {
        ArrayList x = new ArrayList<Actor>();
        x.add(new Actor(5, "Patrick Wilson", "/djhTpbOvrfdDsWZFFintj2Uv47a.jpg"));
        x.add(new Actor(6, "Vera Farmiga", "/oWZfxv4cK0h8Jcyz1MvvT2osoAP.jpg"));
        x.add(new Actor(9, "Mckenna Grace", "/dX6QFwpAzAcXGgxSINwvDxujEgj.jpg"));
        x.add(new Actor(10, "Madison Iseman", "/qkPW0nHQUlckRj3MRveVTzRpNR2.jpg"));
        x.add(new Actor(17, "Katie Sarife", "/oQLQZ58uvGgpdtCUpOcoiF5zYJW.jpg"));
        return x;
    }

}
