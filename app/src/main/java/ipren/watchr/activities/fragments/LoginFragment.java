package ipren.watchr.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import butterknife.BindView;
import butterknife.ButterKnife;
import ipren.watchr.R;
import ipren.watchr.viewModels.LoginViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;
    @BindView(R.id.register_user_layout)
    ConstraintLayout registerlayout;
    @BindView(R.id.reset_password_layout)
    ConstraintLayout resetPasswordLayout;

    @BindView(R.id.password_text_input)
    EditText loginPasswordField;
    @BindView(R.id.email_text_input)
    EditText loginEmailField;
    @BindView(R.id.login_button)
    Button signInBtn;
    @BindView(R.id.login_spinner)
    ProgressBar loginSpinner;
    @BindView(R.id.start_user_registration_btn)
    TextView startUserRegistration;
    @BindView(R.id.start_password_reset_btn)
    TextView startPasswordReset;
    @BindView(R.id.go_to_login_btn)
    ImageView resetPwBackToLogin;
    @BindView(R.id.reset_password_btn)
    Button resetPasswordBtn;
    @BindView(R.id.forgot_psswd_txt_field)
    EditText forgotEmailTxtField;
    LoginViewModel loginViewModel;
    @BindView(R.id.reset_password_spinner)
    ProgressBar resetPasswordSpinner;
    @BindView(R.id.password_reset_response_txt)
    TextView passwordResetResponse;
    @BindView(R.id.register_go_to_login_btn)
    ImageView registerBackToLoginBtn;


    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    //This method is called when the fragment has created has a view.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Getting the viewmodel by using the activity context to save resources.
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        initiateRegisterLayout();
        initiateLoginLayout();
        initPasswordResetLayout();

    }

    //This method must be called after onViewCreated and after the loginViewModel has been fetched
    private void initiateLoginLayout() {

        //Displays an error if the email is badly formatted
        loginEmailField.addTextChangedListener(new EmailFormatListener(loginEmailField));

        //Used to make the phone vibrate when wrong password is entered.
        //This callback loggs the user in or displays error messages
        signInBtn.setOnClickListener(e -> {

            String userEmailTxt = loginEmailField.getText().toString();
            String userPasswordTxt = loginPasswordField.getText().toString();

            //Checks if any fields are empty or badly formatted, if not it attempts a login
            //If the values pass the test a login is attempted, if it fails it will show an error message
            if (!userPasswordTxt.isEmpty() && isEmailFormat(userEmailTxt)) {
                loginViewModel.signIn(userEmailTxt, userPasswordTxt);
                loadingButtonEnabled((Button) e, loginSpinner, true, "Signing in...");
            } else {
                if (userPasswordTxt.isEmpty())
                    loginPasswordField.setError("Please enter your password");
                if (userEmailTxt.isEmpty()) loginEmailField.setError("Please enter your email");
                shakeButton((Button) e);
            }
        });
        //Response from the model regarding the login attempt
        loginViewModel.getSignInResponse().observe(this, e -> {
            loadingButtonEnabled(signInBtn, loginSpinner, false, "Login");
            if (e.isSuccessful()) {
                exitLoginFragment(true);
            } else {
                shakeButton(signInBtn);
                displayAuthError(e.getErrorMsg(), loginEmailField, loginPasswordField);
            }

        });

        //This allows the user to switch to the register page
        startUserRegistration.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, registerlayout, Direction.Right)
        );

        startPasswordReset.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, resetPasswordLayout, Direction.Left));


    }

    //This method must be called after onViewCreated and after the loginViewModel has been fetched
    private void initiateRegisterLayout() {
        View fragmentView = getView();
        EditText password = fragmentView.findViewById(R.id.new_usr_pwd);
        EditText reTypedPassword = fragmentView.findViewById(R.id.new_user_retyped_pwd);
        EditText newUserEmail = fragmentView.findViewById(R.id.new_user_email_input);

        //Displays an error if the email is badly formatted
        newUserEmail.addTextChangedListener(new EmailFormatListener(newUserEmail));

        //TODO add some password check here to see if they are valid, if a method exists for firebase api.
        //TODO Reformat below lambda method
        //Attempt to register the user
        //This will display an error if any fields are poorly formatted, if not will attempt to register
        fragmentView.findViewById(R.id.register_user_btn).setOnClickListener(e -> {
            String passwordTxt = password.getText().toString();
            String reTypedPasswordTxt = reTypedPassword.getText().toString();
            String email = newUserEmail.getText().toString();

            if (email.isEmpty()) newUserEmail.setError("Please enter your email");
            if (!passwordTxt.equalsIgnoreCase(reTypedPasswordTxt)) {
                reTypedPassword.setError("Passwords don't match");

            } else if (passwordTxt.isEmpty()) {
                password.setError("Please enter a password");
                reTypedPassword.setError("Please re-enter password");

            } else if (isEmailFormat(email)) {
                loadingButtonEnabled(getView().findViewById(R.id.register_user_btn), getView().findViewById(R.id.register_user_spinner), true, "registering...");
                loginViewModel.registerUser(email, passwordTxt);
                return;
            }
            shakeButton((Button) e);
        });

        loginViewModel.getCreateUserResponse().observe(this, e -> {
            loadingButtonEnabled(getView().findViewById(R.id.register_user_btn), getView().findViewById(R.id.register_user_spinner), false, "Register");
            if (e.isSuccessful()) {
                exitLoginFragment(true);
                Navigation.findNavController(getView()).navigate(R.id.action_global_account_settings);
            } else {
                displayAuthError(e.getErrorMsg(), newUserEmail, password, reTypedPassword);
                shakeButton(getView().findViewById(R.id.register_user_btn));
            }
        });

        registerBackToLoginBtn.setOnClickListener(e -> transitionBetweenLayouts(registerlayout, loginLayout, Direction.Left));
    }

    private void initPasswordResetLayout() {
        forgotEmailTxtField.addTextChangedListener(new EmailFormatListener(forgotEmailTxtField));
        resetPwBackToLogin.setOnClickListener(e -> transitionBetweenLayouts(resetPasswordLayout, loginLayout, Direction.Right));

        resetPasswordBtn.setOnClickListener(e -> {
            passwordResetResponse.setVisibility(View.INVISIBLE);
            String email = forgotEmailTxtField.getText().toString();
            if (email.isEmpty()) {
                forgotEmailTxtField.setError("This field cannot be empty");
                shakeButton(resetPasswordBtn);
            } else if (isEmailFormat(email)) {
                loadingButtonEnabled(resetPasswordBtn, resetPasswordSpinner, true, "Sending...");
                loginViewModel.resetPassword(email);
            }
        });

        loginViewModel.getPasswordResetResponse().observe(this, e -> {
            passwordResetResponse.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                setTextAndColor(passwordResetResponse, "Sent!", Color.GREEN);
            } else {
                setTextAndColor(passwordResetResponse, e.getErrorMsg(), Color.RED);
            }

            loadingButtonEnabled(resetPasswordBtn, resetPasswordSpinner, false, "Reset password");
        });


    }

    //TODO rework error parsing
    private void displayAuthError(String error, EditText email, EditText... passwords) {
        String msg = error.replace("ERROR_", "").toLowerCase();
        if (msg.contains("email")) email.setError(msg);
        else for (EditText field : passwords) field.setError(msg);

    }

    private void exitLoginFragment(boolean loginSucess) {
        Navigation.findNavController(getView()).popBackStack();
        if (loginSucess)
            Toast.makeText(getContext(), "Welcome, you are logged in", Toast.LENGTH_SHORT).show();
    }

    private boolean isEmailFormat(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void shakeButton(Button button) {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        button.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
    }

    class EmailFormatListener implements TextWatcher {
        private EditText inputTextField;

        public EmailFormatListener(EditText inputTextField) {
            this.inputTextField = inputTextField;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newValue = editable.toString();
            if (!(isEmailFormat(newValue)))
                inputTextField.setError("Not an email address");
        }
    }

    private void loadingButtonEnabled(Button button, ProgressBar spinner, boolean on, String text) {
        button.setEnabled(!on);
        button.setText(text);
        spinner.setVisibility(on ? View.VISIBLE : View.INVISIBLE);
    }

    private void setTextAndColor(TextView view, String text, int color) {
        view.setText(text);
        view.setTextColor(color);
    }

    private void transitionBetweenLayouts(ViewGroup from, ViewGroup to, Direction dir) {
        switch (dir) {
            case Left:
                from.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_right));
                to.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_left));
                break;
            case Right:
                from.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_left));
                to.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right));
                break;
            case Up:
                from.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_top));
                to.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_bottom));
                break;
            case Down:
                from.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_bottom));
                to.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_top));
                break;
        }

        from.setVisibility(View.INVISIBLE);
        to.setVisibility(View.VISIBLE);


    }

    private enum Direction {
        Left, Right, Up, Down
    }
}



