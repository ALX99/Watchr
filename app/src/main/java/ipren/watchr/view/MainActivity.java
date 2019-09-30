package ipren.watchr.view;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ipren.watchr.R;
import ipren.watchr.viewmodel.MainViewModel;
import ipren.watchr.viewmodel.MainViewModelInterface;

public class MainActivity extends AppCompatActivity {

    private MainViewModelInterface mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Set up navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Get model
        mainViewModel = getViewModel();
    }

    //This method can be overriden and allows us to inject a ViewModell for testing
    @VisibleForTesting
    protected MainViewModelInterface getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }


    // This method is for setting the toolbar menu and registering respective listeners
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Setting our menu
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        //Syncing layout to model
        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                menu.findItem(R.id.login_button).setTitle("Login");
                menu.findItem(R.id.profile_photo).setIcon(R.drawable.ic_no_user_photo_24px);
            } else {
                menu.findItem(R.id.login_button).setTitle(user.getUserName());
                Bitmap userProfilePicture = user.getUserProfilePicture();
                menu.findItem(R.id.profile_photo).setIcon(userProfilePicture != null ?
                        new BitmapDrawable(getResources(),
                                user.getUserProfilePicture()) : getResources().getDrawable(R.drawable.ic_no_user_photo_24px));
            }
        });


        return true;
    }

    //This method is for listening to menu onClick events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
