package ipren.watchr.activities.fragments.listeners;

import android.view.View;

public interface MovieClickListener {
    void onMovieClicked(View v);
    void onFavoriteClicked(View v);
    void onWatchLaterClicked(View v);
    void onWatchedClicked(View v);
}
