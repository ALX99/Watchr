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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.activities.Util.Util;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.viewModels.AccountViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

//This Fragment only consists of one layout;
// Elements for input are marked INPUT
// Elements for initiating actions are marked ACTION
// Elements that just display data are marked DISPLAY
public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;

    @BindView(R.id.configure_acc_btn)
    ImageView configAccBtn; //Navigation: Navigates to account settings.
    @BindView(R.id.logout_btn)
    Button logoutBtn; //Action: Logs the user out and pops fragment
    @BindView(R.id.movies_watched_count)
    TextView movies_watched_count; // Display: Shows the total amount of watched movies
    @BindView(R.id.favorite_count)
    TextView favorite_count; // Displays the total amount of favorites
    @BindView(R.id.movies_rated_count)
    TextView movies_rated_count; //Display: Shows the total amount of movies rated
    @BindView(R.id.avrage_rating)
    TextView averageRating; // Display: Shows the average rating score left by the user
    @BindView(R.id.time_watched_count)
    TextView time_watched_count; //Display: Shows the total watch time of a user
    @BindView(R.id.watch_later_count)
    TextView watch_later_count; // Display:Shows how many movies the user has added to watch later
    @BindView(R.id.comments_made_count)
    TextView commentsMadeCount; //Display:Shows the total amount of comments a user has made
    @BindView(R.id.user_not_verified_txt)
    TextView userNotVerifiedTxt; //Display:  Shows a red text that is only visible when user's email is not verified.
    @BindView(R.id.username_text_field_acc)
    TextView usernameTxtField; //Display:  This displays the username of the account if it exists.
    @BindView(R.id.email_text_field_acc)
    TextView emailTxtField; //Display: this field shows the account linked email.
    @BindView(R.id.profile_img_acc)
    ImageView profilePicImg; // Display the current users profile picture



    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating layout and binding views with butterknife
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

        //Navigates to account settings fragment
        configAccBtn.setOnClickListener(e ->
                Navigation.findNavController(fragmentView).navigate(R.id.action_global_account_settings));

        //Syncs the view to user live data, and fetches and syncs
        accountViewModel.user.observe(this, e -> {
            if(e == null) {
                Navigation.findNavController(getView()).popBackStack();
                return;
            }
            //Syncs user data with view
            String uID = e.getUID();
            usernameTxtField.setText(e.getUserName());
            emailTxtField.setText(e.getEmail());
            Util.loadImage((profilePicImg),
                    e.getUserProfilePictureUri().toString(),
                    Util.getProgressDrawable(getContext()));
            if(e.isVerified())
                userNotVerifiedTxt.setVisibility(View.GONE);
            //Fetches user statistics and syncs them with view
            postUserStatistics(uID);

        });
    }

    //Syncs user statistics with the view
    private void postUserStatistics(String uID) {
        accountViewModel.getCommentsByUserId(uID).observe(this, res -> postListSize(commentsMadeCount, res));
        accountViewModel.getRatingByUserId(uID).observe(this, res -> {
            postListSize(movies_rated_count, res);
            setAverageScore(averageRating, calculateAverageRating(res));
        });
        accountViewModel.getFavoritesList(uID).observe(this, res -> postListSize(favorite_count, res));
        accountViewModel.getWatchLaterList(uID).observe(this, res -> postListSize(watch_later_count, res));
        accountViewModel.getWatchedList(uID).observe(this, res -> {
            postListSize(movies_watched_count, res);
            if (res == null)
                return;
            int[] ids = new int[res.length];
            for (int i = 0; i < res.length; i++)
                ids[i] = Integer.parseInt(res[i]);
            accountViewModel.getMovies(ids).observe(this, movies -> time_watched_count.setText(String.valueOf(calcWatchtime(movies))));
        });

    }



    //Parses a Double and sets a a textColor/text corresponding
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
    //Calculates average score for a user, this breaks separation of concern somewhat,but this way we do not have to pass fragment context to the viewModel. And we can make a bit less complex code.
    private Double calculateAverageRating(Rating[] list) { //
        double total = 0;
        if (list == null)
            return null;
        for (Rating rating : list) {
            total += rating.getScore();
        }
        return (total / list.length);
    }
    //Same as with the method above
    private int calcWatchtime(List<Movie> movies) {
        if (movies == null)
            return 0;
        int time = 0;
        for (Movie m : movies)
            if (m.getRuntime() != null)
                time += Integer.valueOf(m.getRuntime());
        return time;
    }

    //Helper method for turning list lenght to String
    private void postListSize(TextView view, Object[] list) {
        if (list == null)
            view.setText("0");
        else
            view.setText("" + list.length);
    }

}
