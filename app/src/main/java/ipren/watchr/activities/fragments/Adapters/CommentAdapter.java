package ipren.watchr.activities.fragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.R;
import ipren.watchr.dataHolders.FireComment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<FireComment> commments;
    private Context mContext;

    public CommentAdapter(Context mContext) {
        commments = new ArrayList<>();
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
//        if (!commments.get(position).getProfilePicLink().isEmpty())
//            Glide.with(mContext)
//                    .load(commments.get(position).getProfilePicLink())
//                    .into(holder.profilePic);

        int resourceId = mContext.getResources().getIdentifier("default_profile_photo", "drawable", "ipren.watchr");
            holder.profilePic.setImageDrawable(ContextCompat.getDrawable(mContext, resourceId));

        holder.username.setText(commments.get(position).getUser_id());
        holder.comment.setText(commments.get(position).getText());

    }

    // Method to update the data
    public void setData(FireComment[] comments) {
        if (comments == null) return;
            this.commments = Arrays.asList(comments);
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
