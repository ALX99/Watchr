package ipren.watchr.activities.fragments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ipren.watchr.R;
import ipren.watchr.dataHolders.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private List<Genre> genres;

    public GenreAdapter() {
        genres = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    // Method to update the data
    public void setData(List<Genre> genres) {
        if (genres.size() == 0)
            this.genres = dummyGenre();
        else
            this.genres = genres;
        notifyDataSetChanged();
    }

    // TODO, remove when API fully implemented.
    private ArrayList<Genre> dummyGenre() {
        ArrayList x = new ArrayList<Genre>();
        x.add(new Genre(27, "Horror"));
        x.add(new Genre(53, "Thriller"));
        x.add(new Genre(9648, "Mystery"));
        return x;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.genreName);
        }
    }
}


