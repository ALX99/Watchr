package ipren.watchr.activities.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.BuildConfig;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.activities.Util.TextWatcherAdapter;
import ipren.watchr.dataHolders.User;
import ipren.watchr.viewModels.AccountSettingsViewModel;

import static ipren.watchr.activities.Util.ActivityUtils.*;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

//TODO This class is not finnished
public class AccountSettingsFragment extends Fragment {

    //User verified layout -> Settings
    @BindView(R.id.ver_email_layout)
    ConstraintLayout userNotVerifiedLayout;
    @BindView(R.id.user_email_txt)
    TextView userEmailTxt;
    @BindView(R.id.username_Input)
    EditText usernameInputField;
    @BindView(R.id.UID_txt_field)
    TextView uIDTextField;
    @BindView(R.id.profile_img_acc)
    CircleImageView profilePic;
    @BindView(R.id.save_user_config_btn)
    Button saveUserConfigBtn;
    @BindView(R.id.settings_back_btn)
    ImageView settingsBackBtn;
    @BindView(R.id.change_password_btn)
    Button changePasswordBtn;

    // User not verified layout
    @BindView(R.id.settings_layout)
    ConstraintLayout verifiedUserLayout;
    @BindView(R.id.email_veri_resp_txt)
    TextView verifcationSentRespTxt;
    @BindView(R.id.is_user_verified_text)
    TextView checkUserVerifcationRespTxt;
    @BindView(R.id.send_ver_email_btn)
    Button sendEmailVerBtn;
    @BindView(R.id.email_veri_spinner)
    ProgressBar sendEmailVerSpinner;
    @BindView(R.id.usr_verif_btn)
    Button isUsrVerifiedBtn;
    @BindView(R.id.user_veri_check_spinner)
    ProgressBar usrVerifiedCheckSpinner;
    @BindView(R.id.update_profile_result_txt)
    TextView updateProfileResponseTxt;
    @BindView(R.id.save_user_profile_spinner)
    ProgressBar saveUserConfigSpinner;
    @BindView(R.id.verify_back_btn)
    ImageView verifyBackBtn;

    @BindView(R.id.change_password_layout)
    ConstraintLayout changePasswordLayout;
    @BindView(R.id.go_back_to_profile_btn)
    ImageView goBackToProfileBtn;
    @BindView(R.id.save_password_btn)
    Button savePasswordBtn;
    @BindView(R.id.save_password_spinner)
    ProgressBar savePasswordSpinner;
    @BindView(R.id.old_password_input)
    EditText oldPasswordInput;
    @BindView(R.id.new_password_input)
    EditText newPasswordInput;
    @BindView(R.id.retype_password_input)
    EditText reTypedPasswordInput;
    @BindView(R.id.change_password_response_txt)
    TextView changePasswordResponse;


    private final int IMAGE_SRC_GALLERY = 0;
    private final int IMAGE_SRC_CAMERA = 1;
    private AccountSettingsViewModel settingsViewModel;
    private Uri uriToTempFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        View view = inflater.inflate(R.layout.account_settings_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEmailVerificationLayout();
        initUserConfigurationLayout();
        initChangePasswordLayout();

        //Syncing to Livedata<User>
        settingsViewModel.getUser().observe(this, e -> {
            if (e == null) {
                Navigation.findNavController(getView()).popBackStack();
                return;
            }
            showEmailVerifiedLayout(e.isVerified());
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_SRC_CAMERA:
                    settingsViewModel.setNewProfilePicture(uriToTempFile);
                    Util.loadImage(profilePic,
                            uriToTempFile, Util.getProgressDrawable(getContext()));
                    break;
                case IMAGE_SRC_GALLERY:
                    settingsViewModel.setNewProfilePicture(data.getData());
                    Util.loadImage(profilePic,
                            data.getData(), Util.getProgressDrawable(getContext()));
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void showEmailVerifiedLayout(boolean verified) {
        if (verified && userNotVerifiedLayout.getVisibility() == View.VISIBLE) {
            userNotVerifiedLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
            transitionBetweenLayouts(userNotVerifiedLayout, verifiedUserLayout, Direction.Right, getContext());
            Toast.makeText(getContext(), "You've been verified", Toast.LENGTH_SHORT).show();
        }
        userNotVerifiedLayout.setVisibility(verified ? View.INVISIBLE : View.VISIBLE);
        verifiedUserLayout.setVisibility(verified ? View.VISIBLE : View.INVISIBLE);
    }

    private void initEmailVerificationLayout() {
        verifyBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack());

        settingsViewModel.getUser().observe(this, e -> {
            if (e == null) return;
            if (!isUsrVerifiedBtn.isEnabled()) {
                checkUserVerifcationRespTxt.setVisibility(View.VISIBLE);
                setTextAndColor(checkUserVerifcationRespTxt,"User not verified!", Color.RED );
                loadingButtonEnabled(isUsrVerifiedBtn, usrVerifiedCheckSpinner, false, "CLICK WHEN VERIFIED");
            }
        });

        isUsrVerifiedBtn.setOnClickListener(e -> {
            loadingButtonEnabled(isUsrVerifiedBtn, usrVerifiedCheckSpinner, true, "Checking...");
            checkUserVerifcationRespTxt.setVisibility(View.INVISIBLE);
            settingsViewModel.refreshUsr();

        });


        sendEmailVerBtn.setOnClickListener(e -> {
            verifcationSentRespTxt.setVisibility(View.INVISIBLE);
            checkUserVerifcationRespTxt.setVisibility(View.INVISIBLE);
            settingsViewModel.resendVerificationEmail();

        });

        settingsViewModel.sendingVerificationEmail.observe(this, bool -> loadingButtonEnabled(sendEmailVerBtn, sendEmailVerSpinner, bool, bool ? "Sending..." : "RE-SEND VERIFICATION"));

        settingsViewModel.getVerificationResponse().observe(this, e -> {
            verifcationSentRespTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(verifcationSentRespTxt, "Email sent!", Color.GREEN);
            else
                setTextAndColor(verifcationSentRespTxt, e.getErrorMsg(), Color.RED);
        });


    }

    private void initChangePasswordLayout() {

        settingsViewModel.usernameErrorTxt.observe(this, txt -> usernameInputField.setError(txt));


        oldPasswordInput.addTextChangedListener(new TextForwarder(oldPasswordInput));
        newPasswordInput.addTextChangedListener(new TextForwarder(newPasswordInput));
        reTypedPasswordInput.addTextChangedListener(new TextForwarder(reTypedPasswordInput));

        settingsViewModel.oldPasswordErrorTxt.observe(this, txt -> oldPasswordInput.setError(txt));
        settingsViewModel.newPasswordErrorTxt.observe(this, txt -> newPasswordInput.setError(txt));
        settingsViewModel.reTypedErrorTxt.observe(this, txt -> reTypedPasswordInput.setError(txt));

        settingsViewModel.changingPassword.observe(this, bool -> loadingButtonEnabled(savePasswordBtn, savePasswordSpinner, bool, bool ? "Saving..." : "save password"));
        goBackToProfileBtn.setOnClickListener(e -> transitionBetweenLayouts(changePasswordLayout, verifiedUserLayout, Direction.Left, getContext()));
        savePasswordBtn.setOnClickListener(e -> {
            changePasswordResponse.setVisibility(View.INVISIBLE);
            if (!settingsViewModel.updateUserPassword())
                shakeButton(savePasswordBtn, getContext());
        });


        settingsViewModel.getChangePasswordResponse().observe(this, e -> {
            changePasswordResponse.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                oldPasswordInput.setText("");newPasswordInput.setText("");
                reTypedPasswordInput.setText("");
                setTextAndColor(changePasswordResponse, "Success!", Color.GREEN);
            } else {
                shakeButton(savePasswordBtn, getContext());
                setTextAndColor(changePasswordResponse, e.getErrorMsg(), Color.RED);
            }


        });

    }

    private void initUserConfigurationLayout() {
        usernameInputField.addTextChangedListener(new TextForwarder(usernameInputField));

        settingsBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack());
        changePasswordBtn.setOnClickListener(e -> transitionBetweenLayouts(verifiedUserLayout, changePasswordLayout, Direction.Right, getContext()));


        settingsViewModel.getUser().observe(this, e -> {
            if (e == null) return;
            userEmailTxt.setText(String.format(getResources().getString(R.string.email), e.getEmail()));
            usernameInputField.setText(e.getUserName());
            uIDTextField.setText(String.format(getResources().getString(R.string.uid), e.getUID()));
            Util.loadImage(profilePic, e.getUserProfilePictureUri(), Util.getProgressDrawable(getContext()));
        });



        saveUserConfigBtn.setOnClickListener(e -> {
            updateProfileResponseTxt.setVisibility(View.INVISIBLE);
            String usernameTxt = usernameInputField.getText().toString();
            if (!settingsViewModel.updateUserProfile()) {
                shakeButton(saveUserConfigBtn, getContext());
                if (!usernameTxt.isEmpty() && (usernameTxt.length() < 15))
                    Toast.makeText(getContext(), "You have not changed anything!", Toast.LENGTH_SHORT).show();
            }
        });

        settingsViewModel.savingPublicProfile.observe(this, bool -> loadingButtonEnabled(saveUserConfigBtn, saveUserConfigSpinner, bool, bool ? "saving profile..." : "Save profile"));
        profilePic.setOnClickListener(e -> chooseGalleryOrCamera());

        settingsViewModel.getUpdateProfileResponse().observe(this, e -> {
            updateProfileResponseTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(updateProfileResponseTxt, "Success!", Color.GREEN);
            else
                setTextAndColor(updateProfileResponseTxt, e.getErrorMsg(), Color.RED);
        });
    }

    private void chooseGalleryOrCamera() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a method");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Uri uri = createTempPictureFile(getContext());
                if (uri == null) {
                    Toast.makeText(getContext(),
                            "Something went wrong, try the gallery instead"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                Intent pickCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pickCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                uriToTempFile = uri;
                startActivityForResult(pickCamera, IMAGE_SRC_CAMERA);
            } else {
                Intent pickGallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pickGallery, IMAGE_SRC_GALLERY);

            }
        });
        builder.show();
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
                case R.id.username_Input:
                    settingsViewModel.setUsername(txt);
                    break;
                case R.id.old_password_input:
                    settingsViewModel.setOldPassword(txt);
                    break;
                case R.id.new_password_input:
                    settingsViewModel.setNewPassword(txt);
                    break;
                case R.id.retype_password_input:
                    settingsViewModel.setReTypedPassword(txt);
                    break;

            }
        }
    }

}
