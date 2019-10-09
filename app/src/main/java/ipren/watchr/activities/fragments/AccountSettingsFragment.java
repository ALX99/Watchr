package ipren.watchr.activities.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ipren.watchr.BuildConfig;
import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.viewModels.AccountSettingsViewModel;

import static android.app.Activity.RESULT_OK;

//TODO This class is not finnished
public class AccountSettingsFragment extends Fragment {

    AccountSettingsViewModel settingsViewModel;

    private final int IMAGE_SRC_GALLERY = 0;
    private final int IMAGE_SRC_CAMERA = 1;
    private final String TMP_IMG_FILENAME = "JPEG_PROFILE_PICTURE";
    private Uri uriToTempFile = null;

    private Uri newProfileImgUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsViewModel = ViewModelProviders.of(this).get(AccountSettingsViewModel.class);
        return inflater.inflate(R.layout.account_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEmailVerificationLayout();
        initUserConfigurationLayout();

        //Syncing to Livedata<User>
        settingsViewModel.getUser().observe(this, e -> {
            if (e == null) {
                Navigation.findNavController(getView()).popBackStack();
                return;
            }
            showEmailVerifiedLayout(e.isVerified());

            // set Image here getView().findViewById(R.id.profile_img_acc)
            ((TextView) getView().findViewById(R.id.email_input_field)).setText("Email: " + e.getEmail());
            ((TextView) getView().findViewById(R.id.username_Input)).setText(e.getUserName());
            ((TextView) getView().findViewById(R.id.UID_txt_field)).setText("UID: " + e.getUID());
            Util.loadImage(getView().findViewById(R.id.profile_img_acc), e.getUserProfilePictureUri().toString(), Util.getProgressDrawable(getContext()));
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_SRC_CAMERA:
                    newProfileImgUri = uriToTempFile;
                    Util.loadImage(getView().findViewById(R.id.profile_img_acc),
                            uriToTempFile.toString(), Util.getProgressDrawable(getContext()));
                    break;
                case IMAGE_SRC_GALLERY:
                    newProfileImgUri = data.getData();
                    Util.loadImage(getView().findViewById(R.id.profile_img_acc),
                            newProfileImgUri.toString(), Util.getProgressDrawable(getContext()));
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void showEmailVerifiedLayout(boolean verified) {
        getView().findViewById(R.id.ver_email_layout).setVisibility(verified ? View.GONE : View.VISIBLE);
        getView().findViewById(R.id.settings_layout).setVisibility(verified ? View.VISIBLE : View.GONE);
    }

    private void initEmailVerificationLayout() {

        getView().findViewById(R.id.send_ver_email_btn).setOnClickListener(e -> settingsViewModel.resendVerificationEmail());

        getView().findViewById(R.id.usr_verif_btn).setOnClickListener(e -> settingsViewModel.refreshUsr());
    }

    private void initUserConfigurationLayout() {

        getView().findViewById(R.id.save_user_config_btn).setOnClickListener(e -> {

             String oldImage = settingsViewModel.getUser().getValue().getUserProfilePictureUri().toString();
             Uri newImage = newProfileImgUri != null && !oldImage.equals(newProfileImgUri.toString()) ? newProfileImgUri : null;


             String oldText = settingsViewModel.getUser().getValue().getUserName();
             String newText = ((EditText)getView().findViewById(R.id.username_Input)).getText().toString();
             String username = !newText.isEmpty() && !oldText.equals(newText) ? newText : null;

             if(username == null && newImage==null)
                 return;

             settingsViewModel.updateUserProfile(username,newImage);
        });

        getView().findViewById(R.id.profile_img_acc).setOnClickListener(e -> chooseGalleryOrCamera());
    }

    private void chooseGalleryOrCamera() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a method");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Uri uri = getIMGTmpFileUri();
                if (uri == null) {
                    Toast.makeText(getContext(),
                            "Something went wrong, try the gallery instead"
                            , Toast.LENGTH_LONG);
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

    private Uri getIMGTmpFileUri() {

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
