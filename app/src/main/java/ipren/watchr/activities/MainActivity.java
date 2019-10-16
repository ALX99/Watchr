package ipren.watchr.activities;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ipren.watchr.R;
import ipren.watchr.viewModels.MainViewModel;
import ipren.watchr.viewModels.MainViewModelInterface;

public class MainActivity extends AppCompatActivity {

    private MainViewModelInterface mainViewModel;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Set up navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Get model
        mainViewModel = getViewModel();

        connectProfileButton();

        // To remove focus from the search view at start
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.requestFocus();
    }

    //This method can be overridden and allows us to inject a ViewModel for testing
    @VisibleForTesting
    protected MainViewModelInterface getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    /**
     * Sets up the profile button
     */
    private void connectProfileButton() {
        ImageButton profileBtn = findViewById(R.id.toolbar_profile);

        // Observes logged in status of user and sets profile picture accordingly
        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                // Not logged in
                profileBtn.setImageResource(R.drawable.ic_profile);
            } else {
                // Logged in
                Glide.with(this).asBitmap().load(user.getUserProfilePictureUri()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // TODO: replace with the resource
                        profileBtn.setImageResource(R.drawable.default_profile_photo_toolbar);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        });

        profileBtn.setOnClickListener(v -> handleProfileFragment());
    }

    /**
     * Handles behaviour of the profile button
     */
    private void handleProfileFragment() {
        int currentID = navController.getCurrentDestination().getId();
        if (currentID == R.id.loginFragment || currentID == R.id.accountFragment)
            navController.popBackStack();
        else if (mainViewModel.getUser().getValue() == null)
            navController.navigate(R.id.action_global_loginFragment);
        else
            navController.navigate(R.id.action_global_accountFragment);
    }

}
