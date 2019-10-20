package ipren.watchr.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import ipren.watchr.viewModels.LoginViewModel;

import static ipren.watchr.activities.Util.ActivityUtils.*;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;
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
    @BindView(R.id.sign_in_response_txt)
    TextView signInResponseTxt;

    @BindView(R.id.register_user_layout)
    ConstraintLayout registerlayout;
    @BindView(R.id.new_usr_pwd)
    EditText newUsrPwdField;
    @BindView(R.id.new_user_retyped_pwd)
    EditText newUsrReTypedPwdField;
    @BindView(R.id.new_user_email_input)
    EditText newUsrEmailField;
    @BindView(R.id.register_user_btn)
    Button registerUsrBtn;
    @BindView(R.id.register_user_spinner)
    ProgressBar registerUsrBtnSpinner;
    @BindView(R.id.register_usr_response_txt)
    TextView registerUsrResponseTxt;

    @BindView(R.id.reset_password_layout)
    ConstraintLayout resetPasswordLayout;
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

        loginEmailField.addTextChangedListener(new TextForwarder(loginEmailField));
        loginPasswordField.addTextChangedListener(new TextForwarder(loginPasswordField));

        loginViewModel.logEmailError.observe(this, txt -> loginEmailField.setError(txt));
        loginViewModel.logPasswordError.observe(this, txt -> loginPasswordField.setError(txt));

        signInBtn.setOnClickListener(e -> {
            signInResponseTxt.setVisibility(View.INVISIBLE);
            if (!loginViewModel.signIn()) shakeButton(signInBtn, getContext());
        });

        loginViewModel.signingIn.observe(this, e -> loadingButtonEnabled(signInBtn, loginSpinner, e.booleanValue(), e.booleanValue() ? "Signing in..." : "Login"));

        loginViewModel.getSignInResponse().observe(this, e -> {
            signInResponseTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                exitLoginFragment(true);
            } else {
                setTextAndColor(signInResponseTxt, e.getErrorMsg(), Color.RED);
                shakeButton(signInBtn, getContext());
            }
        });

        //This allows the user to switch to the register page
        startUserRegistration.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, registerlayout, Direction.Right, getContext())
        );

        startPasswordReset.setOnClickListener(e ->
                transitionBetweenLayouts(loginLayout, resetPasswordLayout, Direction.Left, getContext()));


    }

    //This method must be called after onViewCreated and after the loginViewModel has been fetched
    private void initiateRegisterLayout() {

        newUsrEmailField.addTextChangedListener(new TextForwarder(newUsrEmailField));
        newUsrPwdField.addTextChangedListener(new TextForwarder(newUsrPwdField));
        newUsrReTypedPwdField.addTextChangedListener(new TextForwarder(newUsrReTypedPwdField));

        loginViewModel.regEmailError.observe(this , txt -> newUsrEmailField.setError(txt));
        loginViewModel.regPasswordError.observe(this , txt -> newUsrPwdField.setError(txt));
        loginViewModel.regReTypedPasswordError.observe(this , txt -> newUsrReTypedPwdField.setError(txt));

        //Attempt to register the user
        //This will display an error if any fields are poorly formatted, if not will attempt to register
        registerUsrBtn.setOnClickListener(e -> {
            registerUsrResponseTxt.setVisibility(View.INVISIBLE);
            if (!loginViewModel.registerUser()) shakeButton(registerUsrBtn, getContext());
        });

        loginViewModel.registeringUser.observe(this, e -> loadingButtonEnabled(signInBtn, loginSpinner, e.booleanValue(), e.booleanValue() ? "Registering..." : "Register"));
        loginViewModel.getCreateUserResponse().observe(this, e -> {
            registerUsrResponseTxt.setVisibility(View.VISIBLE);
            loadingButtonEnabled(registerUsrBtn, registerUsrBtnSpinner, false, "Register");
            if (e.isSuccessful()) {
                exitLoginFragment(true);
                Navigation.findNavController(getView()).navigate(R.id.action_global_account_settings);
            } else {
                setTextAndColor(registerUsrResponseTxt, e.getErrorMsg(), Color.RED);
                shakeButton(registerUsrBtn, getContext());
            }
        });

        registerBackToLoginBtn.setOnClickListener(e -> transitionBetweenLayouts(registerlayout, loginLayout, Direction.Left, getContext()));
    }

    private void initPasswordResetLayout() {
        forgotEmailTxtField.addTextChangedListener(new TextForwarder(forgotEmailTxtField));

        loginViewModel.resetEmailError.observe(this, txt -> forgotEmailTxtField.setError(txt));

        resetPwBackToLogin.setOnClickListener(e -> transitionBetweenLayouts(resetPasswordLayout, loginLayout, Direction.Right, getContext()));


        resetPasswordBtn.setOnClickListener(e -> {
            passwordResetResponse.setVisibility(View.INVISIBLE);
            if (!loginViewModel.resetPassword()) shakeButton(resetPasswordBtn, getContext());

        });

        loginViewModel.sendingResetMsg.observe(this, e ->
                loadingButtonEnabled(resetPasswordBtn, resetPasswordSpinner, e.booleanValue(), e.booleanValue() ? "Sending..." : "Reset password"));

        loginViewModel.getPasswordResetResponse().observe(this, e -> {
            passwordResetResponse.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(passwordResetResponse, "Sent!", Color.GREEN);
            else
                setTextAndColor(passwordResetResponse, e.getErrorMsg(), Color.RED);

        });


    }


    private void exitLoginFragment(boolean loginSucess) {
        Navigation.findNavController(getView()).popBackStack();
        if (loginSucess)
            Toast.makeText(getContext(), "Welcome, you are logged in", Toast.LENGTH_SHORT).show();
    }


    class TextForwarder extends TextWatcherAdapter {
        private EditText textField;

        TextForwarder(EditText textField) {
            this.textField = textField;

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String txt = editable.toString();

            switch (textField.getId()) {
                case R.id.new_user_email_input:
                    loginViewModel.setRegEmailTxt(txt);
                    break;
                case R.id.new_usr_pwd:
                    loginViewModel.setRegPasswordTxt(txt);
                    break;
                case R.id.new_user_retyped_pwd:
                    loginViewModel.setRegReTypedPasswordTxt(txt);
                    break;
                case R.id.password_text_input:
                    loginViewModel.setLogPasswordTxt(txt);
                    break;
                case R.id.email_text_input:
                    loginViewModel.setLogEmailTxt(txt);
                    break;
                case R.id.forgot_psswd_txt_field:
                    loginViewModel.setResetEmailTxt(txt);
                    break;

            }
        }
    }

}



