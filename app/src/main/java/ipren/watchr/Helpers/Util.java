package ipren.watchr.Helpers;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ipren.watchr.R;

public class Util {

    /**
     * Loads image on a new thread and displays a circular progress bar while loading. This function
     * will only work with bitmap images and will
     */
    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.drawable.poster_error);
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(imageView);
    }

    /**
     * Overloaded method, converts URI to string
     */
    public static void loadImage(ImageView imageView, Uri uri, CircularProgressDrawable progressDrawable){
        loadImage(imageView, uri.toString(), progressDrawable);
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
