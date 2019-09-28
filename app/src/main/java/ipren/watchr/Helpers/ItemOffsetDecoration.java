package ipren.watchr.Helpers;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Draw spaces between items in the genres RecyclerView
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;

    public ItemOffsetDecoration(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemOffset / 2, itemOffset, itemOffset / 2, itemOffset);
    }
}