package ipren.watchr.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.viewModels.AccountViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class AccountFragment extends Fragment {

    AccountViewModel accountViewModel;

    @BindView(R.id.movies_watched_count)
    TextView movies_watched_count;
    @BindView(R.id.favorite_count)
    TextView favorite_count;
    @BindView(R.id.movies_rated_count)
    TextView movies_rated_count;
    @BindView(R.id.avrage_rating)
    TextView averageRating;
    @BindView(R.id.watch_later_count)
    TextView watch_later_count;
    @BindView(R.id.comments_made_count)
    TextView commentsMadeCount;
    @BindView(R.id.logout_btn)
    Button logoutBtn;
    @BindView(R.id.user_not_verified_txt)
    TextView userNotVerifiedTxt;
    @BindView(R.id.configure_acc_btn)
    ImageView configAccBtn;
    @BindView(R.id.username_text_field_acc)
    TextView usernameTxtField;
    @BindView(R.id.email_text_field_acc)
    TextView emailTxtField;
    @BindView(R.id.profile_img_acc)
    ImageView profilePicImg;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View fragmentView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);


        //Signs the user out and vibrates the phone
        logoutBtn.setOnClickListener(e -> {
            ((Vibrator) getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            Navigation.findNavController(fragmentView).popBackStack();
            Toast.makeText(getContext(), "You've been logged out", Toast.LENGTH_SHORT).show();
            accountViewModel.signOut();
        });

        configAccBtn.setOnClickListener(e ->
                Navigation.findNavController(fragmentView).navigate(R.id.action_global_account_settings));

        accountViewModel.getUser().observe(this, e -> {
            if(e == null) {
                Navigation.findNavController(getView()).popBackStack();
                return;
            }

            String uID = e.getUID();
            usernameTxtField.setText(e.getUserName());
            emailTxtField.setText(e.getEmail());
            Util.loadImage((profilePicImg),
                    e.getUserProfilePictureUri().toString(),
                    Util.getProgressDrawable(getContext()));
            if(e.isVerified())
                userNotVerifiedTxt.setVisibility(View.GONE);

            postUserStatistics(uID);

        });
    }

    //SetData
    private void postUserStatistics(String uID) {
        accountViewModel.getCommentsByUserId(uID).observe(this, res -> postListSize(commentsMadeCount, res));
        accountViewModel.getRatingByUserId(uID).observe(this, res -> {
            postListSize(movies_rated_count, res);
            setAverageScore(averageRating, calculateAverageRating(res));
        });
        accountViewModel.getFavoritesList(uID).observe(this, res -> postListSize(favorite_count, res));
        accountViewModel.getWatchedList(uID).observe(this, res -> postListSize(movies_watched_count, res));
        accountViewModel.getWatchLaterList(uID).observe(this, res -> postListSize(watch_later_count, res));
    }



    //Helpers
    private void setAverageScore(TextView textView, Double value) {
        if (value == null || value.isNaN()) {
            textView.setText("n/a");
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setText(new DecimalFormat("#.#").format(value));
            if (value >= 7)
                textView.setTextColor(Color.GREEN);
            else if (value <= 3)
                textView.setTextColor(Color.RED);
            else
                textView.setTextColor(Color.parseColor("#FFA500"));
        }
    }

    private Double calculateAverageRating(FireRating[] list) {
        double total = 0;
        if (list == null)
            return null;
        for (FireRating rating : list) {
            total += rating.getScore();
        }
        return (total / list.length);
    }


    private void postListSize(TextView view, Object[] list) {
        if (list == null)
            view.setText("0");
        else
            view.setText("" + list.length);
    }

}
