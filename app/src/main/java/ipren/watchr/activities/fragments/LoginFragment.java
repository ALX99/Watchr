package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
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

        initiateRegisterLayout();
        initiateLoginLayout();

    }

    //This method must be called after onViewCreated and after the mainViewModel has been fetched
    private void initiateLoginLayout(){
        View fragmentView = getView();
        EditText passwordTextField = fragmentView.findViewById(R.id.password_text_input);
        EditText emailTextField = fragmentView.findViewById(R.id.email_text_input);

        // Used to make the phone vibrate when wrong password is entered.
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);

        //This callback loggs the user in or displays error messages
        fragmentView.findViewById(R.id.login_button).setOnClickListener(e ->{
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
            String userEmail = emailTextField.getText().toString();
            String userPassword = passwordTextField.getText().toString();
            //If the values pass the test a login is attempted, if it fails it will show an error message
            if(mainViewModel.loginUser(userEmail, userPassword)) {
                Navigation.findNavController(fragmentView).popBackStack();
                Toast.makeText(getContext(), "Welcome, you are logged in", Toast.LENGTH_SHORT).show();
            }else{
                vibrator.vibrate(200);
                e.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                if(mainViewModel.isEmailRegistered(emailTextField.getText().toString()))
                    passwordTextField.setError("Wrong password");
                else
                    emailTextField.setError("Wrong email");
            }
        });
        //This allows the user to switch to the register page
        fragmentView.findViewById(R.id.start_user_registration_btn).setOnClickListener(e -> {
            fragmentView.findViewById(R.id.login_layout).setVisibility(View.INVISIBLE);
            fragmentView.findViewById(R.id.register_user_layout).setVisibility(View.VISIBLE);
        });

    }

    //This method must be called after onViewCreated and after the mainViewModel has been fetched
    private void initiateRegisterLayout(){
        View fragmentView = getView();
        EditText password = fragmentView.findViewById(R.id.new_usr_pwd);
        EditText reTypedPassword = fragmentView.findViewById(R.id.new_user_retyped_pwd);
        EditText newUserEmail = fragmentView.findViewById(R.id.new_user_email_input);

        // This callback displays an error message if an email is taken or invalid to the register layout
        newUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String newValue = editable.toString();
                if(!(isEmailFormat(newValue)))
                    newUserEmail.setError("Not an email adress");
                else if(mainViewModel.isEmailRegistered(newValue))
                    newUserEmail.setError("Already exists");
            }
        });

        //Attempt to register the user
        fragmentView.findViewById(R.id.register_user_btn).setOnClickListener(e -> {
            String passwordTxt = password.getText().toString();
            String reTypedPasswordTxt = reTypedPassword.getText().toString();
            String email = newUserEmail.getText().toString();
            if(passwordTxt.equalsIgnoreCase(reTypedPasswordTxt) && !mainViewModel.isEmailRegistered(email)){
                //Register here
            }
        });


    }

    private boolean isEmailFormat(String email){
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
