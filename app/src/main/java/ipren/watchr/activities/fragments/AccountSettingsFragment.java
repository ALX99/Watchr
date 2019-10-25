package ipren.watchr.activities.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import de.hdodenhof.circleimageview.CircleImageView;
import ipren.watchr.R;
import ipren.watchr.activities.Util.TextWatcherAdapter;
import ipren.watchr.activities.Util.Util;
import ipren.watchr.viewmodels.AccountSettingsViewModel;


import static android.app.Activity.RESULT_OK;
import static ipren.watchr.activities.Util.ActivityUtils.Direction;
import static ipren.watchr.activities.Util.ActivityUtils.clearAndHideTextViews;
import static ipren.watchr.activities.Util.ActivityUtils.createTempPictureFile;
import static ipren.watchr.activities.Util.ActivityUtils.loadingButtonEnabled;
import static ipren.watchr.activities.Util.ActivityUtils.setTextAndColor;
import static ipren.watchr.activities.Util.ActivityUtils.shakeButton;
import static ipren.watchr.activities.Util.ActivityUtils.transitionBetweenLayouts;

// This class has three included layouts from separate xml files, what elements are included in each layout is shown below.
// What buttons allow the user to switch between layouts is marked with  NAVIGATION
// Elements for input are marked INPUT
// Elements for initiating actions are marked ACTION
// Elements that just display data are marked DISPLAY
public class AccountSettingsFragment extends Fragment {

    //User verified layout -> User profile layout
    @BindView(R.id.settings_layout)
    ConstraintLayout verifiedUserLayout; //Layout root
    @BindView(R.id.user_email_txt)
    TextView userEmailTxt; //DISPLAY: A text displaying the current users email.
    @BindView(R.id.UID_txt_field)
    TextView userUIDTxt; //DISPLAY: A text displaying the current users UID
    @BindView(R.id.username_Input)
    EditText usernameInputField; //INPUT: An input field for setting the current users username
    @BindView(R.id.profile_img_acc)
    CircleImageView profilePic; //INPUT: A picture showing the current users profile picture. It also works as a button for changing the current users profile picture
    @BindView(R.id.save_user_config_btn)
    Button saveProfilesSettingsBtn; //ACTION: A button for trying to save the set profile settings to the current user
    @BindView(R.id.save_user_profile_spinner)
    ProgressBar saveUserConfigSpinner; //DISPLAY: Progress spinner that is visible while the app is trying to save profile settings
    @BindView(R.id.update_profile_result_txt)
    TextView updateProfileResponseTxt; //DISPLAY: A Text displaying the result of trying to save profile settings to current user
    @BindView(R.id.settings_back_btn)
    ImageView settingsBackBtn; //NAVIGATION: This image allows the user to exit account settings without using the back button
    @BindView(R.id.change_password_btn)
    Button goToPasswordChangeBtn; //NAVIGATION:  A button for switching to change password layout

    // User not verified layout
    @BindView(R.id.ver_email_layout)
    ConstraintLayout userNotVerifiedLayout; //Layout root
    @BindView(R.id.send_ver_email_btn)
    Button sendEmailVerBtn; //ACTION: A button for sending a verification email to the current user
    @BindView(R.id.email_veri_spinner)
    ProgressBar sendingEmailVerSpinner; //DISPLAY: Progress spinner that is visible while the app is trying to send a verification email.
    @BindView(R.id.email_veri_resp_txt)
    TextView sendEmailVerRespTxt; //DISPLAY: A text showing the result of trying to send a verification email
    @BindView(R.id.usr_verif_btn)
    Button checkIfUsrIsVerifiedBtn; //ACTION: A button for checking if the user is verified
    @BindView(R.id.user_veri_check_spinner)
    ProgressBar checkingVerificationSpinner; //DISPLAY: Progress spinner that is visible while the app is checking if the user is verified
    @BindView(R.id.is_user_verified_text)
    TextView checkUserVerificationRespTxt; //DISPLAY: A text showing if the user is verified or not as a result of checking it.
    @BindView(R.id.verify_back_btn)
    ImageView verifyLayoutBackBtn; //NAVIGATION:  This image allows the user to exit account settings without using the back button

    // Layout view for changing password
    @BindView(R.id.change_password_layout)
    ConstraintLayout changePasswordLayout;//Layout root
    @BindView(R.id.old_password_input)
    EditText oldPasswordInput; //INPUT: The users old/current password. Used for authentication
    @BindView(R.id.new_password_input)
    EditText newPasswordInput; //INPUT: The users new password
    @BindView(R.id.retype_password_input)
    EditText reTypedPasswordInput; //INPUT: The users new password retyped
    @BindView(R.id.save_password_btn)
    Button savePasswordBtn; //ACTION: Button for trying to save new password
    @BindView(R.id.save_password_spinner)
    ProgressBar savePasswordSpinner; //DISPLAY: Progress spinner that is visible while password is being saved
    @BindView(R.id.change_password_response_txt)
    TextView changePasswordResponse; //DISPLAY: A text that shows the results of the save password action
    @BindView(R.id.go_back_to_profile_btn)
    ImageView goBackToProfileBtn; //NAVIGATION: Button for returning to the User profile layout


    private final int IMAGE_SRC_GALLERY = 0; //OnActivityResults code, informs that the result is from the camera
    private final int IMAGE_SRC_CAMERA = 1; //OnActivityResults code, informs that the result is from gallery
    private AccountSettingsViewModel settingsViewModel;
    private Uri uriToTempFile = null; //Uri for the temp file that will/is holding the new profilePicture. Getting the picture is async so this must be stored in the object

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating layout and binding views with butterknife
        settingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        View view = inflater.inflate(R.layout.account_settings_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEmailVerificationLayout(); // Initiates layout for verifying a user.
        initUserConfigurationLayout(); // Initiates the layout for a verified user
        initChangePasswordLayout(); // Initiates the layout for changing password.

        //Syncing to Livedata<User> if user == null no user is logged in and this fragment should not exist.
        settingsViewModel.liveUser.observe(this, user -> {
            if (user == null)
                return;

            showEmailVerifiedLayout(user.isVerified()); // Sets initial layout and transitions to the verified layout if a user has become verified while activity is active
        });


    }

    // This override is used as a callback for when a user picks a new photo, or if they fail to do so.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_SRC_CAMERA: // A user has taken a photograph saving URI to viewmodel
                    settingsViewModel.setNewProfilePicture(uriToTempFile);
                    Util.loadImage(profilePic,
                            uriToTempFile, Util.getProgressDrawable(getContext()));
                    break;
                case IMAGE_SRC_GALLERY: // A user has chosen a photo from the gallery setting URI to viewmodel
                    settingsViewModel.setNewProfilePicture(data.getData());
                    Util.loadImage(profilePic,
                            data.getData(), Util.getProgressDrawable(getContext()));
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show(); // Aborted/failed any error in choosing a picture.
        }
    }

    //This method sets the initial layout and transitions to the profile settings page if a user
    private void showEmailVerifiedLayout(boolean verified) {
        if (verified && userNotVerifiedLayout.getVisibility() == View.VISIBLE) { //Verification layout is visible but the user is verified -> Transitioning to profile settings
            transitionBetweenLayouts(userNotVerifiedLayout, verifiedUserLayout, Direction.Right, getContext());
            Toast.makeText(getContext(), "You've been verified", Toast.LENGTH_SHORT).show();
        }
        userNotVerifiedLayout.setVisibility(verified ? View.INVISIBLE : View.VISIBLE);
        verifiedUserLayout.setVisibility(verified ? View.VISIBLE : View.INVISIBLE);
    }

    //Layout for verifying user email, always visible if the user is not verified.
    private void initEmailVerificationLayout() {
        verifyLayoutBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack()); //Exits application

        settingsViewModel.liveUser.observe(this, user -> { // This method works as a callback for checking if the user is verified. Setting error texts and informing that the checkUser action is completed
            if (user == null) return;                               // .refreshUsr(); Triggers Livedata<user>.
            if (!checkIfUsrIsVerifiedBtn.isEnabled()) {
                checkUserVerificationRespTxt.setVisibility(View.VISIBLE);
                if (!user.isVerified())
                    setTextAndColor(checkUserVerificationRespTxt, "User not verified!", Color.RED);
                loadingButtonEnabled(checkIfUsrIsVerifiedBtn, checkingVerificationSpinner, false, "CLICK WHEN VERIFIED");
            }
        });

        //Refreshes the user, which triggers Livedata<User>
        checkIfUsrIsVerifiedBtn.setOnClickListener(e -> {
            loadingButtonEnabled(checkIfUsrIsVerifiedBtn, checkingVerificationSpinner, true, "Checking...");
            clearAndHideTextViews(checkUserVerificationRespTxt);
            settingsViewModel.refreshUsr();

        });


        sendEmailVerBtn.setOnClickListener(e -> { //Attempts to send a verification email
            clearAndHideTextViews(sendEmailVerRespTxt, checkUserVerificationRespTxt); //Hide previous error messages
            settingsViewModel.resendVerificationEmail();

        });

        //This method observes the state of sendingVerificationEmails and updates the sendEmailVerBtn as a result. Mirroring the state
        settingsViewModel.sendingVerificationEmail.observe(this, bool -> loadingButtonEnabled(sendEmailVerBtn, sendingEmailVerSpinner, bool, bool ? "Sending..." : "RE-SEND VERIFICATION"));

        //This method observes the result from a send verification action and displays the result.
        settingsViewModel.sendVerEmailResponse.observe(this, e -> {
            sendEmailVerRespTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(sendEmailVerRespTxt, "Email sent!", Color.GREEN);
            else
                setTextAndColor(sendEmailVerRespTxt, e.getErrorMsg(), Color.RED);
        });


    }

    private void initChangePasswordLayout() {



        oldPasswordInput.addTextChangedListener(new TextForwarder(oldPasswordInput)); // These three methods allows the ViewModel to receive the current Text of respective fields
        newPasswordInput.addTextChangedListener(new TextForwarder(newPasswordInput)); // settings error messages if need be.
        reTypedPasswordInput.addTextChangedListener(new TextForwarder(reTypedPasswordInput)); //

        settingsViewModel.oldPasswordErrorTxt.observe(this, txt -> oldPasswordInput.setError(txt));// This method observes an errorTxt in the ViewModel corresponding to each input field
        settingsViewModel.newPasswordErrorTxt.observe(this, txt -> newPasswordInput.setError(txt)); // which is triggered when it receives a badly formatted text from respective inputField
        settingsViewModel.reTypedErrorTxt.observe(this, txt -> reTypedPasswordInput.setError(txt));

        goBackToProfileBtn.setOnClickListener(e -> transitionBetweenLayouts(changePasswordLayout, verifiedUserLayout, Direction.Left, getContext())); //Navigates back to the profile settings page / verified layout

        savePasswordBtn.setOnClickListener(e -> { // Attempts to save the password
            clearAndHideTextViews(changePasswordResponse);
            if (!settingsViewModel.updateUserPassword()) // The viewmodel can reject the attempt if so it will return false and the fragment will respond by vibrating and shaking the button
                shakeButton(savePasswordBtn, getContext());
        });

        //This method observes the state of changing passwords and updates the Save Password button as a result. Mirroring the state
        settingsViewModel.changingPassword.observe(this, bool -> loadingButtonEnabled(savePasswordBtn, savePasswordSpinner, bool, bool ? "Saving..." : "save password"));

        //This method observes the result from a save password action and displays the result. Also clears the previous password fields
        settingsViewModel.changePasswordResponse.observe(this, e -> {
            changePasswordResponse.setVisibility(View.VISIBLE);
            if (e.isSuccessful()) {
                oldPasswordInput.setText("");
                newPasswordInput.setText("");
                reTypedPasswordInput.setText("");
                setTextAndColor(changePasswordResponse, "Success!", Color.GREEN); //Displaying result success
            } else {
                shakeButton(savePasswordBtn, getContext());
                setTextAndColor(changePasswordResponse, e.getErrorMsg(), Color.RED); // Displaying result error
            }


        });

    }

    private void initUserConfigurationLayout() {
        usernameInputField.addTextChangedListener(new TextForwarder(usernameInputField)); // These three methods allows the viewmodel to receive the currentText of the inputField, settings error messages if need be.

        settingsViewModel.usernameErrorTxt.observe(this, txt -> usernameInputField.setError(txt)); // This method observes an errorTxt in the viewmodel
        // which is triggered when it receives a badly formatted password from this textfield

        settingsBackBtn.setOnClickListener(e -> Navigation.findNavController(getView()).popBackStack()); //Exit button for leaving the fragment
        goToPasswordChangeBtn.setOnClickListener(e -> transitionBetweenLayouts(verifiedUserLayout, changePasswordLayout, Direction.Right, getContext())); //Switches to the change password layout

        settingsViewModel.liveUser.observe(this, e -> { // Sets fields to reflect the current logged in user, if user is null(it should never be here) it simply returns as no value can be extracted.
            if (e == null) return;
            userEmailTxt.setText(String.format(getResources().getString(R.string.email), e.getEmail()));
            usernameInputField.setText(e.getUserName());
            userUIDTxt.setText(String.format(getResources().getString(R.string.uid), e.getUID()));
            Util.loadImage(profilePic, e.getUserProfilePictureUri(), Util.getProgressDrawable(getContext()));
        });


        saveProfilesSettingsBtn.setOnClickListener(e -> { // Attempts to save profile, it can be either rejected or accepted
            clearAndHideTextViews(updateProfileResponseTxt);
            String usernameTxt = usernameInputField.getText().toString();
            if (!settingsViewModel.updateUserProfile()) {   //Attempts to update the userprofile, if all fields are entered correctly it will return true, if they are not it will return false
                shakeButton(saveProfilesSettingsBtn, getContext()); // If an update request was rejected the fragment will react by vibrating and shaking the button.
                if (!usernameTxt.isEmpty() && (usernameTxt.length() < 15))
                    Toast.makeText(getContext(), "You have not changed anything!", Toast.LENGTH_SHORT).show(); //Toasts cannot be triggered from the regular viewmodel and they should not. In favor of making a another observable livedata
                // or a more flexible return value , I did this small check which breaks seperation of concern slightly, however makes the code less complex.
            }
        });

        //This method observes the state of saving profile action and updates the Save profile as a result. Mirroring the state
        settingsViewModel.savingNewProfile.observe(this, bool -> loadingButtonEnabled(saveProfilesSettingsBtn, saveUserConfigSpinner, bool, bool ? "saving profile..." : "Save profile"));
        profilePic.setOnClickListener(e -> chooseGalleryOrCamera()); // Attempts to fetch a new profile picture by either camera or gallery

        //This method observes the result from a save profile action and displays the result.
        settingsViewModel.updateProfileResponse.observe(this, e -> {
            updateProfileResponseTxt.setVisibility(View.VISIBLE);
            if (e.isSuccessful())
                setTextAndColor(updateProfileResponseTxt, "Success!", Color.GREEN);
            else
                setTextAndColor(updateProfileResponseTxt, e.getErrorMsg(), Color.RED);
        });
    }

    //Helper method for choosing a new profile picture. It will open a modal dialog.
    private void chooseGalleryOrCamera() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a method");
        builder.setItems(options, (dialog, which) -> { //Callback for when/if a user pics picture fetching method.
            if (which == 0) {  // User choose camera
                Uri uri = createTempPictureFile(getContext());
                if (uri == null) {
                    Toast.makeText(getContext(),
                            "Something went wrong, try the gallery instead"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                Intent pickCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Get new photo with the camera
                pickCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                uriToTempFile = uri;
                startActivityForResult(pickCamera, IMAGE_SRC_CAMERA);
            } else {   // User choose gallery
                Intent pickGallery = new Intent(Intent.ACTION_PICK,   // Choose a new profile photo from the gallery
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pickGallery, IMAGE_SRC_GALLERY);

            }
        });
        builder.show(); // Show the modal dialog
    }

    //Helper class extending a Textwatcher adapter
    class TextForwarder extends TextWatcherAdapter {
        private EditText textField; // Observing editText

        TextForwarder(EditText textField) {
            this.textField = textField;

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String txt = editable.toString();
            //All TextFields in this fragment passing their current txt to the viewmodel
            switch (textField.getId()) {
                case R.id.username_Input:
                    settingsViewModel.setUsername(txt); //Profile settings layout  username
                    break;
                case R.id.old_password_input:
                    settingsViewModel.setOldPassword(txt); //Change password  layout old/currentpassword
                    break;
                case R.id.new_password_input:
                    settingsViewModel.setNewPassword(txt); //Change password  layout new password
                    break;
                case R.id.retype_password_input:
                    settingsViewModel.setReTypedPassword(txt); //Change password  layout re-typed new password
                    break;

            }
        }
    }

}