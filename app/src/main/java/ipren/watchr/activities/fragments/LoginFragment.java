package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;



import ipren.watchr.R;
import ipren.watchr.viewModels.MainViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class LoginFragment extends Fragment {

    public LoginFragment(){}

    MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //This method is called when the fragment has created has a view.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Getting the viewmodel by using the activity context to save resources.
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        View fragmentView = getView();
        EditText passwordTextField = fragmentView.findViewById(R.id.password_text_field);
        EditText emailTextField = fragmentView.findViewById(R.id.email_text_field);

        // Used to make the phone vibrate when wrong password is entered.
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);

        fragmentView.findViewById(R.id.loginButton).setOnClickListener(e ->{
            // Checks that the fields have a value. TODO can be replaced with a "validate string value" method if its not possible to do in the layout
            Boolean isPasswEmpty = passwordTextField.getText().toString().equalsIgnoreCase("");
            Boolean isEmailEmpty = emailTextField.getText().toString().equalsIgnoreCase("");
            //If either the password or the email is empty/not accepted it shows an error message.
            if(isPasswEmpty || isEmailEmpty) {
                if (isPasswEmpty) passwordTextField.setError("Please enter your password");
                if (isEmailEmpty) emailTextField.setError("Please enter your email");
                vibrator.vibrate(200);
                e.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                return;
            }

            //If the values pass the test a login is attempted, if it fails it will show an error message
           if(mainViewModel.loginUser(emailTextField.getText().toString(), passwordTextField.getText().toString())) {
               Navigation.findNavController(fragmentView).popBackStack();
               Toast.makeText(getContext(), "Logged in", Toast.LENGTH_SHORT).show();
           }else{
               vibrator.vibrate(200);
               if(mainViewModel.validUser(emailTextField.getText().toString()))
                   passwordTextField.setError("Wrong password");
               else
                   emailTextField.setError("Wrong email");
           }
        });


    }
}
