package ipren.watchr.activities.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.BuildConfig;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
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
    private final String TMP_IMG_FILENAME = "JPEG_PROFILE_PICTURE";
    AccountSettingsViewModel settingsViewModel;
    private Uri uriToTempFile = null;
    private Uri newProfileImgUri = null;


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
            if (e == null)
                return;

            showEmailVerifiedLayout(e.isVerified());

            if (!isUsrVerifiedBtn.isEnabled()) {
                checkUserVerifcationRespTxt.setText("User not verified!");
                checkUserVerifcationRespTxt.setTextColor(Color.RED);
                checkUserVerifcationRespTxt.setVisibility(View.VISIBLE);
                loadingButtonEnabled(isUsrVerifiedBtn, usrVerifiedCheckSpinner, false, "CLICK WHEN VERIFIED");
            }


            userEmailTxt.setText(String.format(getResources().getString(R.string.email), e.getEmail()));
            usernameInputField.setText(e.getUserName());
            uIDTextField.setText(String.format(getResources().getString(R.string.uid), e.getUID()));
            Util.loadImage(profilePic, e.getUserProfilePictureUri(), Util.getProgressDrawable(getContext()));
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_SRC_CAMERA:
                    newProfileImgUri = uriToTempFile;
                    Util.loadImage(profilePic,
                            uriToTempFile, Util.getProgressDrawable(getContext()));
                    break;
                case IMAGE_SRC_GALLERY:
                    newProfileImgUri = data.getData();
                    Util.loadImage(profilePic,
                            newProfileImgUri, Util.getProgressDrawable(getContext()));
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void showEmailVerifiedLayout(boolean verified) {
        if (verified && userNotVerifiedLayout.getVisibility() == View.VISIBLE) {
            userNotVerifiedLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
            Toast.makeText(getContext(), "You've been verified", Toast.LENGTH_SHORT).show();
        }
        userNotVerifiedLayout.setVisibility(verified ? View.INVISIBLE : View.VISIBLE);
        verifiedUserLayout.setVisibility(verified ? View.VISIBLE : View.INVISIBLE);
    }

    private void initEmailVerificationLayout() {
        verifyBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack());
        sendEmailVerBtn.setOnClickListener(e -> {
            loadingButtonEnabled(sendEmailVerBtn, sendEmailVerSpinner, true, "Sending...");
            verifcationSentRespTxt.setVisibility(View.INVISIBLE);
            checkUserVerifcationRespTxt.setVisibility(View.INVISIBLE);
            settingsViewModel.resendVerificationEmail();

        });

        isUsrVerifiedBtn.setOnClickListener(e -> {
            loadingButtonEnabled(isUsrVerifiedBtn, usrVerifiedCheckSpinner, true, "Checking");
            checkUserVerifcationRespTxt.setVisibility(View.INVISIBLE);
            settingsViewModel.refreshUsr();

        });

        settingsViewModel.getVerificationResponse().observe(this, e -> {
            loadingButtonEnabled(sendEmailVerBtn, sendEmailVerSpinner, false, "RE-SEND VERIFICATION");
            if (e == null)
                return;
            verifcationSentRespTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                verifcationSentRespTxt.setText("Email sent!");
                verifcationSentRespTxt.setTextColor(Color.GREEN);
            } else {
                verifcationSentRespTxt.setText(e.getErrorMsg());
                verifcationSentRespTxt.setTextColor(Color.RED);
            }
        });


    }

    private void initChangePasswordLayout() {
        goBackToProfileBtn.setOnClickListener(e -> transitionBetweenLayouts(changePasswordLayout, verifiedUserLayout, Direction.Left, getContext()));
        savePasswordBtn.setOnClickListener(e -> {
            changePasswordResponse.setVisibility(View.INVISIBLE);
            String oldPassword = oldPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            String reTypedPassword = reTypedPasswordInput.getText().toString();
            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                if (oldPassword.isEmpty())
                    oldPasswordInput.setError("Please write your old password");
                if (newPassword.isEmpty())
                    newPasswordInput.setError("Please write a new password");
            } else if (!newPassword.equals(reTypedPassword)) {
                if (reTypedPassword.isEmpty())
                    reTypedPasswordInput.setError("Please re-type password");
                else
                    reTypedPasswordInput.setError("Passwords not identical");
            } else {
                loadingButtonEnabled(savePasswordBtn, savePasswordSpinner, true, "Saving...");
                settingsViewModel.updateUserPassword(oldPassword, newPassword);
                return;
            }
            shakeButton(savePasswordBtn, getContext());
        });
        settingsViewModel.getChangePasswordResponse().observe(this, e -> {
            changePasswordResponse.setVisibility(View.VISIBLE);
            loadingButtonEnabled(savePasswordBtn, savePasswordSpinner, false, "Save password");
            if (e.isSuccessful()) {
                oldPasswordInput.setText("");
                newPasswordInput.setText("");
                reTypedPasswordInput.setText("");
                setTextAndColor(changePasswordResponse, "Success!", Color.GREEN);
            } else {
                if(e.getErrorMsg().contains("6"))
                    newPasswordInput.setError("Too weak");
                else if(e.getErrorMsg().contains("invalid"))
                    oldPasswordInput.setError("Wrong");
                shakeButton(savePasswordBtn, getContext());
                setTextAndColor(changePasswordResponse, e.getErrorMsg(), Color.RED);
            }


        });

    }

    private void initUserConfigurationLayout() {

        settingsBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack());
        changePasswordBtn.setOnClickListener(e -> transitionBetweenLayouts(verifiedUserLayout, changePasswordLayout, Direction.Right, getContext()));
        saveUserConfigBtn.setOnClickListener(e -> {

            String oldImage = settingsViewModel.getUser().getValue().getUserProfilePictureUri().toString();
            Uri newImage = newProfileImgUri != null && !oldImage.equals(newProfileImgUri.toString()) ? newProfileImgUri : null;


            String oldText = settingsViewModel.getUser().getValue().getUserName();
            String newText = usernameInputField.getText().toString();


            if (oldText.equals(newText) && newImage == null) {
                shakeButton(saveUserConfigBtn, getContext());
                Toast.makeText(getContext(), "You have not changed anything!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!oldText.isEmpty() && newText.isEmpty()) {
                shakeButton(saveUserConfigBtn, getContext());
                usernameInputField.setError("Your username can't be empty");
                return;
            } else if (newText.length() > 15) {
                shakeButton(saveUserConfigBtn, getContext());
                usernameInputField.setError("That username is too long");
                return;
            }

            loadingButtonEnabled(saveUserConfigBtn, saveUserConfigSpinner, true, "Saving...");
            updateProfileResponseTxt.setVisibility(View.INVISIBLE);
            settingsViewModel.updateUserProfile(newText, newImage);
        });

        profilePic.setOnClickListener(e -> chooseGalleryOrCamera());

        settingsViewModel.getUpdateProfileResponse().observe(this, e -> {
            loadingButtonEnabled(saveUserConfigBtn, saveUserConfigSpinner, false, "SAVE");
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
                Uri uri = createTempPictureFile();
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

    private Uri createTempPictureFile() {

        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File file = File.createTempFile(
                    TMP_IMG_FILENAME,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );


            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            return photoURI;

        } catch (IOException e) {
            return null;
        }

    }

}
