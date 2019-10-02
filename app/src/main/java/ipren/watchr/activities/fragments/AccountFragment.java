package ipren.watchr.activities.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.R;
import ipren.watchr.viewModels.AccountViewModel;
import ipren.watchr.viewModels.MainViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class AccountFragment extends Fragment {

    AccountViewModel accountViewModel;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Signs the user out and vibrates the phone
        getView().findViewById(R.id.logout_btn).setOnClickListener(e -> {
            ((Vibrator) getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            Navigation.findNavController(getView()).popBackStack();
            Toast.makeText(getContext(), "You've been logged out", Toast.LENGTH_SHORT).show();
            accountViewModel.signOut();
        });

        accountViewModel.getUser().observe(this, e ->{
            ((TextView)getView().findViewById(R.id.username_text_field_acc)).setText(e.getUserName());
            ((TextView)getView().findViewById(R.id.email_text_field_acc)).setText(e.getEmail());
            //TODO Once the repository is built, this object will not allow null profile picture, this is only temporary . Remove this, if.
            if(e.getUserProfilePicture() == null)
                return;
            ((CircleImageView)getView().findViewById(R.id.profile_img_acc)).setImageDrawable(new BitmapDrawable(getResources(), e.getUserProfilePicture()));
        });


    }
}
