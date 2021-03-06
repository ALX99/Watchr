package ipren.watchr.activities.Util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ipren.watchr.BuildConfig;
import ipren.watchr.R;

import static android.content.Context.VIBRATOR_SERVICE;

//Helper class for activities
public class ActivityUtils {

    //Sets the state of a button to disables and shows/hides a spinner. Helps changing button state
    public static void loadingButtonEnabled(Button button, ProgressBar spinner, boolean on, String text) {
        button.setEnabled(!on);
        button.setText(text);
        spinner.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    //Helper method for manipulating textviews
    public static void setTextAndColor(TextView view, String text, int color) {
        view.setText(text);
        view.setTextColor(color);
    }

    //Shakes a button and the phone
    public static void shakeButton(Button button, Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        button.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }

    //Creates a temporary URI for pictures
    public static Uri createTempPictureFile(Context context) {

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";


        try {
            File file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",/* suffix */
                    storageDir      /* directory */
            );


            Uri photoURI = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            return photoURI;

        } catch (IOException e) {
            return null;
        }

    }

    //Sets textviews to empty and hides them
    public static void clearAndHideTextViews(TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setText("");
            textView.setVisibility(View.INVISIBLE);
        }
    }

    //This method animates changes between layouts and sets their state variables
    public static void transitionBetweenLayouts(ViewGroup from, ViewGroup to, Direction dir, Context context) {
        switch (dir) {
            case Left:
                from.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_right));
                to.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_left));
                break;
            case Right:
                from.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_left));
                to.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_right));
                break;
            case Up:
                from.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_top));
                to.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom));
                break;
            case Down:
                from.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_bottom));
                to.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_top));
                break;
        }

        from.setVisibility(View.INVISIBLE);
        from.setEnabled(false);
        to.setVisibility(View.VISIBLE);
        to.setEnabled(true);
    }

    public enum Direction {
        Left, Right, Up, Down
    }
}
