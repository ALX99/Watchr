package ipren.watchr.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ipren.watchr.activities.Util.TextWatcherAdapter;
import ipren.watchr.viewmodels.LoginViewModel;

import static ipren.watchr.activities.Util.ActivityUtils.Direction;
import static ipren.watchr.activities.Util.ActivityUtils.clearAndHideTextViews;
import static ipren.watchr.activities.Util.ActivityUtils.loadingButtonEnabled;
import static ipren.watchr.activities.Util.ActivityUtils.setTextAndColor;
import static ipren.watchr.activities.Util.ActivityUtils.shakeButton;
import static ipren.watchr.activities.Util.ActivityUtils.transitionBetweenLayouts;


// This class has three included layouts from separate xml files, what elements are included in each layout is shown below.
//The layouts are set like this:  Reset<->Login<->Register
// What buttons allow the user to switch between layouts is marked with  NAVIGATION
// Elements for input are marked INPUT
// Elements for initiating actions are marked ACTION
// Elements that just display data are marked DISPLAY
public class LoginFragment extends Fragment {

    //Initial layout - Login
    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout; // Layout root
    @BindView(R.id.email_text_input)
    EditText loginEmailField; //INPUT: The users email, used for identification
    @BindView(R.id.password_text_input)
    EditText loginPasswordField; //INPUT: The users password. Used for authentication
    @BindView(R.id.login_button)
    Button signInBtn; //ACTION: A button for attempting to log in.
    @BindView(R.id.login_spinner)
    ProgressBar loginSpinner; //DISPLAY: A progress spinner that is visible while the application is attempting to signing the user in.
    @BindView(R.id.sign_in_response_txt)
    TextView signInResponseTxt; //DISPLAY: A text displaying the result of a sign-in attempt.
    @BindView(R.id.start_password_reset_btn)
    TextView startPasswordReset; //NAVIGATION: Switches to password reset layout.
    @BindView(R.id.start_user_registration_btn)
    TextView startUserRegistration;//NAVIGATION: Switches to user registration layout

    //Layout for account registration
    @BindView(R.id.register_user_layout)
    ConstraintLayout registerlayout; //Layout root
    @BindView(R.id.new_user_email_input)
    EditText newUsrEmailField; //INPUT: The users email address.
    @BindView(R.id.new_usr_pwd)
    EditText newUsrPwdField; //INPUT: The users preferred password. Must be > 5
    @BindView(R.id.new_user_retyped_pwd)
    EditText newUsrReTypedPwdField; //INPUT: The users preferred password retyped.
    @BindView(R.id.register_user_btn)
    Button registerUsrBtn; //ACTION: A button for attempting to register the user
    @BindView(R.id.register_user_spinner)
    ProgressBar registerUsrBtnSpinner; //DISPLAY: A progress spinner that is visible while the app is attempting to register the user.
    @BindView(R.id.register_usr_response_txt)
    TextView registerUsrResponseTxt; //DISPLAY: A text showing the results of an attempted account registration
    @BindView(R.id.register_go_to_login_btn)
    ImageView registerBackToLoginBtn; //NAVIGATION : Switches to the login layout.

    //Reset password layout
    @BindView(R.id.reset_password_layout)
    ConstraintLayout resetPasswordLayout; //Layout root
    @BindView(R.id.forgot_psswd_txt_field)
    EditText forgotEmailTxtField; //INPUT: An input field for writing the email to the account which password you forgot.
    @BindView(R.id.reset_password_btn)
    Button resetPasswordBtn; //ACTION: A button for attempting to send a verification email
    @BindView(R.id.reset_password_spinner)
    ProgressBar resetPasswordSpinner;//DISPLAY: A progress spinner visible while the application is attempting to send a verification email.
    @BindView(R.id.password_reset_response_txt)
    TextView passwordResetResponse;//DISPLAY: A text showing the results of an attempt to send a reset password email.
    @BindView(R.id.go_to_login_btn)
    ImageView resetPwBackToLogin; //NAVIGATION: Switches to the login layout

    LoginViewModel loginViewModel;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating layout and binding views with butterknife
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    //This method is called when the fragment has created has a view.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        //These methods can only be called in onViewCreated or later, or it might cause a null pointer
        initiateLoginLayout(); //Initial layout: Initiates the layout for logging in
        initiateRegisterLayout(); //Initiates the layout for registering an account
        initPasswordResetLayout(); //Initiates the layout for resetting your password

    }

    //This method must be called after onViewCreated and after the loginViewModel has been fetched or risk nullpointers
    //This method initiates the Login layout, syncing with LiveData.
    private void initiateLoginLayout() {

        loginEmailField.addTextChangedListener(new TextForwarder(loginEmailField));       // These methods allows the ViewModel to receive the current Text of respective fields
        loginPasswordField.addTextChangedListener(new TextForwarder(loginPasswordField)); // settings error messages if need be.

        loginViewModel.logEmailError.observe(this, txt -> loginEmailField.setError(txt));       // This method observes an errorTxt in the ViewModel corresponding to each input field
        loginViewModel.logPasswordError.observe(this, txt -> loginPasswordField.setError(txt));  // which is triggered when it receives a badly formatted text from respective inputField

        signInBtn.setOnClickListener(e -> { //Attempts to sign the user in
            clearAndHideTextViews(signInResponseTxt);
            if (!loginViewModel.signIn())
                shakeButton(signInBtn, getContext()); //The ViewModel can reject the attempt if any fields are badly formatted by returning false, if so the fragment will respond by vibrating the button
        });
        //This method observes the state of signingIn and updates the signInBtn  as a result. Mirroring the state
        loginViewModel.signingIn.observe(this, bool -> loadingButtonEnabled(signInBtn, loginSpinner, bool, bool ? "Signing in..." : "Login"));

        //This method observes the result of attempting to log in, if its not successful it will display an error message and shake the button, if it is it inform the user and exit the fragment.
        loginViewModel.signInResponse.observe(this, e -> {
            signInResponseTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                exitLoginFragment(true); //Exit the fragment and notify the user that they are now logged in
            } else {
                setTextAndColor(signInResponseTxt, e.getErrorMsg(), Color.RED); //Display error
                shakeButton(signInBtn, getContext());  //Vibrate the phone and shake the button
            }
        });

        //This allows the user to switch to the register account layout
        startUserRegistration.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, registerlayout, Direction.Right, getContext())
        );
        //This allows the user  to switch to the resetPassword layout
        startPasswordReset.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, resetPasswordLayout, Direction.Left, getContext()));


    }

    //This method must be called after onViewCreated and after the loginViewModel has been fetched or risk nullpointers
    //This method initiates the register layout, syncing with LiveData.
    private void initiateRegisterLayout() {

        newUsrEmailField.addTextChangedListener(new TextForwarder(newUsrEmailField)); // These methods allows the ViewModel to receive the current Text of respective fields
        newUsrPwdField.addTextChangedListener(new TextForwarder(newUsrPwdField));     // settings error messages if need be.
        newUsrReTypedPwdField.addTextChangedListener(new TextForwarder(newUsrReTypedPwdField)); //

        loginViewModel.regEmailError.observe(this, txt -> newUsrEmailField.setError(txt));  // These methods observes an errorTxt in the ViewModel corresponding to each input field
        loginViewModel.regPasswordError.observe(this, txt -> newUsrPwdField.setError(txt)); // which is triggered when it receives a badly formatted text from respective inputField
        loginViewModel.regReTypedPasswordError.observe(this, txt -> newUsrReTypedPwdField.setError(txt));


        registerUsrBtn.setOnClickListener(e -> {          //Attempt to register the user
            clearAndHideTextViews(registerUsrResponseTxt);
            if (!loginViewModel.registerUser())   //The ViewModel can reject the attempt if any fields are badly formatted if so it will return false, the fragment will respond by vibrating/shaking the button
                shakeButton(registerUsrBtn, getContext());
        });
        //This method observes the state of registering a user and updates the registerUsrBtn  as a result. Mirroring the state
        loginViewModel.registeringUser.observe(this, bool -> loadingButtonEnabled(registerUsrBtn, registerUsrBtnSpinner, bool, bool ? "Registering..." : "Register"));


        //This method observes the result of attempting to register an account, if its not successful it will display an error message and shake the button, if it is it exit the fragment and navigate to the accountsettings fragment  where the user can verify his/hers account.
        loginViewModel.registerUserResponse.observe(this, e -> {
            registerUsrResponseTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                exitLoginFragment(true); //Exit fragment and notify the user that they are logged in;
                Navigation.findNavController(getView()).navigate(R.id.action_global_account_settings); //Navigate to the account settings page so that they can verify their account.
            } else {
                setTextAndColor(registerUsrResponseTxt, e.getErrorMsg(), Color.RED); //Display error
                shakeButton(registerUsrBtn, getContext()); // Vibrate phone and shake button
            }
        });
        //This allows the user  to switch to the loginLayout
        registerBackToLoginBtn.setOnClickListener(e -> transitionBetweenLayouts(registerlayout, loginLayout, Direction.Left, getContext()));
    }

    private void initPasswordResetLayout() {

        forgotEmailTxtField.addTextChangedListener(new TextForwarder(forgotEmailTxtField));  // These methods allows the ViewModel to receive the current Text of respective fields
                                                                                            // settings error messages if need be.

        loginViewModel.resetEmailError.observe(this, txt -> forgotEmailTxtField.setError(txt));  // This method observes an errorTxt in the ViewModel corresponding to this input field
                                                                                                        // which is triggered when it receives a badly formatted text from it

        resetPasswordBtn.setOnClickListener(e -> { //Attempt to send reset password link to current user
            clearAndHideTextViews(passwordResetResponse);
            if (!loginViewModel.resetPassword()) shakeButton(resetPasswordBtn, getContext());  //The ViewModel can reject the attempt if any fields are badly formatted if so it will return false, the fragment will respond by vibrating/shaking the button
        });

        //This method observes the state of sending(a)ResetMSG to the specified email and updates the resetPasswordBtn  as a result. Mirroring the state
        loginViewModel.sendingResetMsg.observe(this, bool ->
                loadingButtonEnabled(resetPasswordBtn, resetPasswordSpinner, bool, bool ? "Sending..." : "Reset password"));
        //This method observes the result of attempting to send a reset password email, if its not successful it will display an error message and shake the button,
        loginViewModel.passwordResetResponse.observe(this, e -> { // if it is it the fragment will display a success message
            passwordResetResponse.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(passwordResetResponse, "Sent!", Color.GREEN); // Display success message
            else
                setTextAndColor(passwordResetResponse, e.getErrorMsg(), Color.RED); // Display error message

        });

        //This allows the user to switch to the login fragment
        resetPwBackToLogin.setOnClickListener(e -> transitionBetweenLayouts(resetPasswordLayout, loginLayout, Direction.Right, getContext()));
    }

    //Helper method for exiting the fragment, in the event of a login it will display a message
    private void exitLoginFragment(boolean loginSuccess) {
        Navigation.findNavController(getView()).popBackStack();
        if (loginSuccess)
            Toast.makeText(getContext(), "Welcome, you are logged in", Toast.LENGTH_SHORT).show();
    }

    //Helper class extending a TextWatcher adapter
    class TextForwarder extends TextWatcherAdapter {
        private EditText textField; // Observing editText

        TextForwarder(EditText textField) {
            this.textField = textField; //set editText to observe
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String txt = editable.toString();
            //All TextFields in this fragment passing their current txt to the viewmodel
            switch (textField.getId()) {
                case R.id.new_user_email_input:
                    loginViewModel.setRegEmailTxt(txt); // Register user layout, new email
                    break;
                case R.id.new_usr_pwd:
                    loginViewModel.setRegPasswordTxt(txt); // Register user layout, new password
                    break;
                case R.id.new_user_retyped_pwd:
                    loginViewModel.setRegReTypedPasswordTxt(txt);  // Register user layout, retyped new password
                    break;
                case R.id.password_text_input:
                    loginViewModel.setLogPasswordTxt(txt); // Login layout  users password
                    break;
                case R.id.email_text_input:
                    loginViewModel.setLogEmailTxt(txt); // Login layout users email
                    break;
                case R.id.forgot_psswd_txt_field:
                    loginViewModel.setResetEmailTxt(txt); // Reset password layout email connected to the account with lost password
                    break;

            }
        }
    }

}



