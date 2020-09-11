package com.example.imageslider;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<SliderItem> itemList;
    private List<SliderItem> firstItemList;
    private ViewPager2 vpImage;
    private View view, btnNext;
    private Context mContext;

    private boolean isFirstime = true;

    public ImageAdapter(Context mContext, List<SliderItem> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
//        btnNext = view.findViewById(R.id.btn_next);

//        btnNext.setOnClickListener(v -> {
//            int currentItem = getPositin();
//            Toast.makeText(mContext, ""+currentItem, Toast.LENGTH_SHORT).show();
//        });
//        firstCountItem = itemList.size();
//        if (isFirstime){
//            firstItemList = new ArrayList<>();
//            firstItemList.addAll(itemList);
//            isFirstime = false;
////            Toast.makeText(mContext, "Filling data", Toast.LENGTH_SHORT).show();
//        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mContext)
                .load("http://192.168.0.102/sentuh_image/image/" + itemList.get(position).getImage_url())
                .apply(new RequestOptions().fitCenter())
                .into(holder.ivImage);
//        if (position == itemList.size() - 2){
//            vpImage.post(runnable);
////            Toast.makeText(mContext,  "Itemlist " + itemList.size() + "/First "+ firstItemList.size(), Toast.LENGTH_SHORT).show();
//
//        }
    }



//    private Runnable runnable = () -> {
//        itemList.addAll(firstItemList);
//        notifyDataSetChanged();
//    };



    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private boolean isDrawing = false;
//        private PaintView pvCanvas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
//            pvCanvas = itemView.findViewById(R.id.pv_item_canvas);
        }


    }
}
