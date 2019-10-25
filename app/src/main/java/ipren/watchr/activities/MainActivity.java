package ipren.watchr.activities;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

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

import ipren.watchr.R;
import ipren.watchr.dataholders.MovieFilter;
import ipren.watchr.repository.movierepository.Database.MovieDB;
import ipren.watchr.viewmodels.MainViewModel;
import ipren.watchr.viewmodels.MainViewModelInterface;

public class MainActivity extends AppCompatActivity {

    private MainViewModelInterface mainViewModel;
    private NavController navController;
    private MovieFilter movieFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init DB
        MovieDB.initDB(getApplicationContext());

        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Setup navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Get model
        mainViewModel = getViewModel();

        initFilter();
        initProfileButton();

        // To remove focus from the search view at start
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.requestFocus();
    }

    /**
     * Initializes the movieFilter
     */
    private void initFilter() {
        // MovieFilter model
        movieFilter = MovieFilter.getInstance();

        // Init movieFilter drawer
        DrawerLayout filterDrawer = findViewById(R.id.filter_drawer);
        filterDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        initSwitches();
        initSpinners();
    }

    /**
     * Initializes the movieFilter switches
     */
    private void initSwitches() {
        initSwitch(R.id.watched_switch);
        initSwitch(R.id.watch_later_switch);
        initSwitch(R.id.favorites_switch);
    }

    /**
     * Sets up a switch view and its listener
     *
     * @param view Id of the switch
     * @return The newly created switch
     */
    private Switch initSwitch(int view) {
        Switch switchView = findViewById(view);
        String tag = (String) switchView.getTag();
        switchView.setOnClickListener(v -> {
            if (switchView.isChecked()) {
                movieFilter.setSwitch(tag, true);
            } else {
                movieFilter.setSwitch(tag, false);
            }
        });
        return switchView;
    }

    /**
     * Initializes the movieFilter spinners
     */
    private void initSpinners() {
        initSpinner(R.id.rating_spinner, R.array.rating_spinner);
        initSpinner(R.id.genre_spinner, R.array.genre_spinner);
        initSpinner(R.id.order_by_spinner, R.array.order_by_spinner);
    }

    /**
     * Sets up a spinner from a spinner view and its listener
     *
     * @param layout The spinner view id
     * @param array  Id of array of items
     */
    private Spinner initSpinner(int layout, int array) {
        Spinner spinner = findViewById(layout);
        String tag = (String) spinner.getTag();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
                movieFilter.setSpinner(tag, value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return spinner;
    }

    //This method can be overridden and allows us to inject a ViewModel for testing
    @VisibleForTesting
    protected MainViewModelInterface getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    /**
     * Sets up the profile button
     */
    private void initProfileButton() {
        ImageButton profileBtn = findViewById(R.id.toolbar_profile);
        profileBtn.setImageResource(R.drawable.ic_profile);

        // Observes logged in status of user and sets profile picture accordingly
        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                // Not logged in
                profileBtn.setColorFilter(getResources().getColor(R.color.text));
            } else {
                // Logged in
                Glide.with(this).asBitmap().load(user.getUserProfilePictureUri()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        profileBtn.setColorFilter(getResources().getColor(R.color.colorAccent));
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

    /**
     * Hides the search and movieFilter buttons from the toolbar
     */
    private void hideSearchAndFilter() {
        // Get the search view from toolbar and hide
        SearchView searchView = findViewById(R.id.toolbar_search);
        searchView.setVisibility(View.GONE);

        // Get the movieFilter button from toolbar and show
        ImageButton filterBtn = findViewById(R.id.toolbar_filter);
        filterBtn.setVisibility(View.GONE);
    }

}
