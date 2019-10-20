package ipren.watchr.activities.Util;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ipren.watchr.R;
import ipren.watchr.activities.fragments.LoginFragment;

import static android.content.Context.VIBRATOR_SERVICE;

public class ActivityUtils {


    public static void loadingButtonEnabled(Button button, ProgressBar spinner, boolean on, String text) {
        button.setEnabled(!on);
        button.setText(text);
        spinner.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    public static void setTextAndColor(TextView view, String text, int color) {
        view.setText(text);
        view.setTextColor(color);
    }

    public static void shakeButton(Button button, Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        button.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }

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
        to.setVisibility(View.VISIBLE);
    }

    public enum Direction {
        Left, Right, Up, Down
    }
}
