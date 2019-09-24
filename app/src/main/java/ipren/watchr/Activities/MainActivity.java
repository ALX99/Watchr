package ipren.watchr.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ipren.watchr.R;
import ipren.watchr.ViewModels.MainViewModel;
import ipren.watchr.ViewModels.MainViewModelInterface;

public class MainActivity extends AppCompatActivity {

    private MainViewModelInterface mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        mainViewModel = getViewModel();
    }

    //This method can be overriden and allows us to inject a ViewModell for testing
    @VisibleForTesting
    protected MainViewModelInterface getViewModel(){
      return ViewModelProviders.of(this).get(MainViewModel.class); }


    // This method is for setting the toolbar menu and registering respective listeners
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Setting our menu
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        //Syncing layout to model
        mainViewModel.getUser().observe(this , e -> {
            if(e == null)
                menu.findItem(R.id.login_button).setTitle("Login");
            else
                menu.findItem(R.id.login_button).setTitle(e.getUserName());
        });

        return true;
    }

    //This method is for listening to menu onClick events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
