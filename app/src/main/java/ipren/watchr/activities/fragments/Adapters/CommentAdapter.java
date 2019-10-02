package ipren.watchr.activities.fragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.R;
import ipren.watchr.dataHolders.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> commments;
    private Context mContext;

    public CommentAdapter(Context mContext, ArrayList<Comment> commments) {
        this.commments = commments;
        this.mContext = mContext;
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
        if (!commments.get(position).getProfilePicLink().isEmpty())
            Glide.with(mContext)
                    .load(commments.get(position).getProfilePicLink())
                    .into(holder.profilePic);
        else {
            int resourceId = mContext.getResources().getIdentifier("profile_picture_mock", "drawable", "ipren.watchr");
            holder.profilePic.setImageDrawable(ContextCompat.getDrawable(mContext, resourceId));
        }
        holder.username.setText(commments.get(position).getUsername());
        holder.comment.setText(commments.get(position).getComment());

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
