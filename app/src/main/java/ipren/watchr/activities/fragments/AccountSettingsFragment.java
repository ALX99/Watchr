package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import ipren.watchr.R;
import ipren.watchr.viewModels.AccountSettingsViewModel;
//TODO This class is not finnished
public class AccountSettingsFragment extends Fragment {

    AccountSettingsViewModel settingsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        return inflater.inflate(R.layout.account_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsViewModel.getUser().observe(this, e -> {
            if(e == null) {
                Navigation.findNavController(getView()).popBackStack();
                return;
            } else if(!e.isVerified()){
                getView().findViewById(R.id.ver_email_layout).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.settings_layout).setVisibility(View.GONE);

            }else {
                getView().findViewById(R.id.ver_email_layout).setVisibility(View.GONE);
                getView().findViewById(R.id.settings_layout).setVisibility(View.VISIBLE);
                // set Image here getView().findViewById(R.id.profile_img_acc)
                ((TextView)getView().findViewById(R.id.email_input_field)).setText(e.getEmail());
                ((TextView)getView().findViewById(R.id.user_name_Input)).setText(e.getUserName());
                ((TextView)getView().findViewById(R.id.UID_txt_field)).setText(e.getUID());
            }
        });

    }


}
