package ipren.watchr.activities.fragments.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.R;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.viewModels.IMovieViewModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commments;
    private Context mContext;
    private IMovieViewModel viewModel;
    private LifecycleOwner owner;

    public CommentAdapter(Context mContext, IMovieViewModel viewModel, LifecycleOwner owner) {
        commments = new ArrayList<>();
        this.mContext = mContext;
        this.viewModel = viewModel;
        this.owner = owner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commet_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Make sure there is a URL in there, otherwise set default image
        viewModel.getPublicProfile(commments.get(position).getUser_id()).observe(owner, profile -> {
            if (profile != null) {
                Glide.with(mContext)
                        .load(profile.getProfilePhotoUri().toString())
                        .error(ContextCompat.getDrawable(mContext, mContext.getResources().getIdentifier("default_profile_photo", "drawable", "ipren.watchr")))
                        .into(holder.profilePic);
                holder.username.setText(profile.getUsername());
                Log.d("COMMENT", "URI for " + profile.getUsername() + " is " + profile.getProfilePhotoUri().toString());
            }
        });


        holder.comment.setText(commments.get(position).getText());

    }

    // Method to update the data
    public void setData(List<Comment> comments) {
        if (comments == null) return;
        this.commments = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView comment;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.comment_profile);
            username = itemView.findViewById(R.id.comment_username);
            comment = itemView.findViewById(R.id.commentText);
        }
    }
}
