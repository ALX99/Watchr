package ipren.watchr.Helpers;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ipren.watchr.R;

public class Util {

    /**
     * Loads image on a new thread and displays a circular progress bar while loading
     */
    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.drawable.poster_w92);
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(imageView);
    }

    /**
     * Creates the circular progress bar
     */
    public static CircularProgressDrawable getProgressDrawable(Context context) {
        CircularProgressDrawable cpd = new CircularProgressDrawable(context);
        cpd.setStrokeWidth(10f);
        cpd.setCenterRadius(50f);
        cpd.start();
        return cpd;
    }

    /**
     * For binding images directly in XML
     */
    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }
}
