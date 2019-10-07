package ipren.watchr.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
        setSupportActionBar(findViewById(R.id.toolbar_menu));

        // Set up navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
                menu.findItem(R.id.sign_in_btn_toolbar).setVisible(true);menu.findItem(R.id.user_profile_toolbar).setVisible(false);
            } else {
                menu.findItem(R.id.sign_in_btn_toolbar).setVisible(false);menu.findItem(R.id.user_profile_toolbar).setVisible(true);
                Bitmap userProfilePicture = user.getUserProfilePicture();
                //Todo When the repository is built, the User objects wont hold null values, remove this nullcheck
                menu.findItem(R.id.user_profile_toolbar).setIcon(userProfilePicture != null ?
                        new BitmapDrawable(getResources(),
                                user.getUserProfilePicture()) : getResources().getDrawable(R.drawable.ic_no_user_photo_24px));
            }
        });

        //Connecting the nested layouts onClickListener to the toolbars onclick listener
        MenuItem menuItem = menu.findItem(R.id.sign_in_btn_toolbar);
        menuItem.getActionView().findViewById(R.id.login_icon).setOnClickListener( e -> onOptionsItemSelected(menuItem));

        return true;
    }

    //This method is for listening to menu onClick events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int currentID = navController.getCurrentDestination().getId();
        if(currentID == R.id.loginFragment || currentID == R.id.accountFragment)
            navController.popBackStack();
        else
            if(mainViewModel.getUser().getValue() == null)
                 navController.navigate(R.id.action_global_loginFragment);
            else
                navController.navigate(R.id.action_global_accountFragment);
        return super.onOptionsItemSelected(item);
    }

    }

