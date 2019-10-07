package ipren.watchr.activities.fragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.dataHolders.Actor;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private ArrayList<Actor> actors;
    private Context mContext;

    public CastAdapter(Context mContext, ArrayList<Actor> actors) {
        this.actors = actors;
        this.mContext = mContext;
    }

    // Inflate our RecyclerView with the items it shall contain?
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_listitem, parent, false);
        return new ViewHolder(view);
    }

    // Where data is being attached
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(actors.get(position).getName());
        Util.loadImage(holder.image, actors.get(position).getPictureLink(), Util.getProgressDrawable(holder.image.getContext()));
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    // ViewHolder object
    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.actorImage);
            name = itemView.findViewById(R.id.actorName);
        }
    }
}
