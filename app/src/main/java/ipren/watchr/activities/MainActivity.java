package ipren.watchr.activities;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ipren.watchr.R;
import ipren.watchr.activities.fragments.FilterFragment;
import ipren.watchr.repository.Database.MovieDB;
import ipren.watchr.viewModels.MainViewModel;
import ipren.watchr.viewModels.MainViewModelInterface;

public class MainActivity extends AppCompatActivity {

    private MainViewModelInterface mainViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init DB
        MovieDB.initDB(getApplicationContext());

        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Set up navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Get model
        mainViewModel = getViewModel();





        // TODO: @johan Temporary, refactor this
        // Set up filter drawer
        DrawerLayout filterDrawer = findViewById(R.id.filter_drawer);
        filterDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Set up the rating spinner
        Spinner ratingSpinner = findViewById(R.id.rating_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.rating_spinner, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        ratingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        ratingSpinner.setAdapter(ratingAdapter);

        // Set up the rating spinner
        Spinner genreSpinner = findViewById(R.id.genre_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.genre_spinner, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        genreAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        genreSpinner.setAdapter(genreAdapter);

        // Set up the rating spinner
        Spinner orderBySpinner = findViewById(R.id.order_by_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> orderByAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.order_by_spinner, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        orderByAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        orderBySpinner.setAdapter(orderByAdapter);







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

    public NavController getNavController() {
        return navController;
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
        if (currentID == R.id.loginFragment || currentID == R.id.accountFragment || currentID == R.id.accountSettingsFragment) {
            navController.popBackStack();
        } else if (mainViewModel.getUser().getValue() == null) {
            navController.navigate(R.id.action_global_loginFragment);
            hideSearchAndFilter();
        } else {
            navController.navigate(R.id.action_global_accountFragment);
            hideSearchAndFilter();
        }
    }

    private void hideSearchAndFilter() {
        // Get the search view from toolbar and hide
        SearchView searchView = findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.GONE);

        // Get the filter button from toolbar and show
        ImageButton filterBtn = findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.GONE);
    }

}
