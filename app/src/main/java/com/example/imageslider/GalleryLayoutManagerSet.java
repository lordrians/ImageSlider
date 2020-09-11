package com.example.imageslider;

import android.content.Context;

import com.leochuan.CarouselLayoutManager;
import com.leochuan.GalleryLayoutManager;

public class GalleryLayoutManagerSet extends GalleryLayoutManager {
    private boolean isScrollEnable = true;

    public GalleryLayoutManagerSet(Context context, int itemSpace) {
        super(context, itemSpace);
    }

    public GalleryLayoutManagerSet(Context context, int itemSpace, int orientation) {
        super(context, itemSpace, orientation);
    }

    public GalleryLayoutManagerSet(Context context, int itemSpace, int orientation, boolean reverseLayout) {
        super(context, itemSpace, orientation, reverseLayout);
    }

    public GalleryLayoutManagerSet(Builder builder) {
        super(builder);
    }

    public void setScrollEnabled(boolean flag){
        this.isScrollEnable = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnable;
    }

    @Override
    public int getCurrentPosition() {
        return super.getCurrentPosition();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
