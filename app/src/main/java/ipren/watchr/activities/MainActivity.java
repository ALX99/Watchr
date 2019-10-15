package ipren.watchr.activities;


import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
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
    }

    //This method can be overridden and allows us to inject a ViewModel for testing
    @VisibleForTesting
    protected MainViewModelInterface getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    private void connectProfileButton() {
        ImageButton profileBtn = findViewById(R.id.toolbar_profile);

        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                profileBtn.setImageResource(R.drawable.ic_profile);
            } else {
                // Android just has to complicate things
                Glide.with(this).asBitmap().load(user.getUserProfilePictureUri()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        findViewById(R.id.toolbar_profile).setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        });

        profileBtn.setOnClickListener(v -> handleProfileFragment());
    }

//    // This method is for setting the toolbar menu and registering respective listeners
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //Setting our menu
//        getMenuInflater().inflate(R.menu.toolbar_main, menu);
//        //Syncing layout to model
//        mainViewModel.getUser().observe(this, user -> {
//            if (user == null) {
//                showSignInIcon(true, menu);
//            } else {
//                showSignInIcon(false, menu);
//
//                //Android just has to complicate things
//                Glide.with(this).asBitmap().load(user.getUserProfilePictureUri()).into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        menu.findItem(R.id.user_profile_toolbar).setIcon(new BitmapDrawable(resource));
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });
//            }
//        });
//
//
//        //Connecting the nested layouts onClickListener to the toolbars onclick listener
//        MenuItem menuItem = menu.findItem(R.id.sign_in_btn_toolbar);
//        menuItem.getActionView().findViewById(R.id.login_icon).setOnClickListener(e -> onOptionsItemSelected(menuItem));
//
//        return true;
//    }

//    private void showSignInIcon(boolean show, Menu menu) {
//        menu.findItem(R.id.sign_in_btn_toolbar).setVisible(show);
//        menu.findItem(R.id.user_profile_toolbar).setVisible(!show);
//    }
//
    //This method is for listening to menu onClick events
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
