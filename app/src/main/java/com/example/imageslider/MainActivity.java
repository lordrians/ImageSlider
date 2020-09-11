package com.example.imageslider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leochuan.CarouselLayoutManager;
import com.leochuan.CenterSnapHelper;
import com.leochuan.GalleryLayoutManager;
import com.rm.freedrawview.FreeDrawView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 vpImage;
    private PaintView paintView;
    private List<SliderItem> sliderItems;
    private ImageButton btnNext, btnDraw, btnClear;
    private Button btnPlay;
    private RecyclerView rvImage;
    private GalleryLayoutManagerSet galleryLayoutManager;
    private boolean isEnable = false;
    private boolean isLastScroll = false;
    private CenterSnapHelper centerSnapHelper;
    private static final String URL_SHOW = "http://192.168.0.102/sentuh_image/show_item.php";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNext = findViewById(R.id.btn_next);
        btnDraw = findViewById(R.id.btn_draw);
        btnClear = findViewById(R.id.btn_clear);
        btnPlay = findViewById(R.id.btn_play);
        vpImage = findViewById(R.id.vp_image);
        rvImage = findViewById(R.id.rv_image);
        paintView = findViewById(R.id.paintView);


        btnDraw.setAlpha((float) 0.7);
        btnClear.setAlpha((float) 0.7);
        btnNext.setAlpha((float) 0.7);
//        rvImage.setAlpha((float) 0.85);



        sliderItems = new ArrayList<>();
        loadData();

        View clCoverStart = findViewById(R.id.cover_start);
        clCoverStart.setAlpha((float)0.7);

        btnPlay.setOnClickListener(v -> {
            rvImage.scrollToPosition(0);
            rvImage.setVisibility(View.VISIBLE);
            btnDraw.setVisibility(View.VISIBLE);
            btnClear.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            clCoverStart.setVisibility(View.GONE);
            btnPlay.setVisibility(View.GONE);

        });

        btnNext.setOnClickListener(v -> {
            View itemView;
            FreeDrawView fdPaint;
//            int currentPosition = galleryLayoutManager.getCurrentPosition() ;
            int currentPosition = galleryLayoutManager.findLastVisibleItemPosition();
            galleryLayoutManager.scrollToPosition( currentPosition + 1);
            if (currentPosition == galleryLayoutManager.getItemCount()-1){
                itemView = Objects.requireNonNull(rvImage.findViewHolderForAdapterPosition(currentPosition)).itemView;
                fdPaint = itemView.findViewById(R.id.fd_item_canvas);
            } else {
                itemView = Objects.requireNonNull(rvImage.findViewHolderForAdapterPosition(currentPosition+1)).itemView;
                fdPaint = itemView.findViewById(R.id.fd_item_canvas);
            }

            if (isEnable){
                fdPaint.setVisibility(View.VISIBLE);
            } else {
                if (fdPaint.getVisibility() == View.VISIBLE){
                    btnDraw.setBackgroundColor(getResources().getColor(R.color.ActiveButton));
                } else {
                    btnDraw.setBackgroundColor(getResources().getColor(R.color.defaultButton));
                }
            }



        });

        btnDraw.setOnClickListener(v -> {

            int currentPosition = galleryLayoutManager.getCurrentPosition();
            View itemView = Objects.requireNonNull(rvImage.findViewHolderForAdapterPosition(currentPosition)).itemView;
            FreeDrawView fdPaint = itemView.findViewById(R.id.fd_item_canvas);

            if (fdPaint.getVisibility() != View.VISIBLE){
                fdPaint.setVisibility(View.VISIBLE);
                btnDraw.setBackgroundColor(getResources().getColor(R.color.ActiveButton));
                isEnable = true;
            } else {
                fdPaint.setVisibility(View.GONE);
                btnDraw.setBackgroundColor(getResources().getColor(R.color.defaultButton));
                isEnable = false;
            }


        });



        btnClear.setOnClickListener(v -> {

            int currentPosition = galleryLayoutManager.findLastVisibleItemPosition();
            PaintView pvItem = rvImage.findViewHolderForAdapterPosition(currentPosition).itemView.findViewById(R.id.pv_item_canvas);

            FreeDrawView fdPaint = Objects.requireNonNull(rvImage.findViewHolderForAdapterPosition(currentPosition)).itemView.findViewById(R.id.fd_item_canvas);
            fdPaint.clearDraw();

        });



    }

    private void recyclerviewSetting() {


        centerSnapHelper = new CenterSnapHelper();
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(),sliderItems);
        galleryLayoutManager = new GalleryLayoutManagerSet(getApplicationContext(),0);
        galleryLayoutManager.setItemSpace(-500);
        galleryLayoutManager.setMaxAlpha((float)1.2);
        galleryLayoutManager.setEnableBringCenterToFront(true);
        float scale = (float) 0.8;
//        galleryLayoutManager.setMinScale(scale);
        galleryLayoutManager.setInfinite(true);
        rvImage.setLayoutManager(galleryLayoutManager);
        rvImage.setAdapter(imageAdapter);
        rvImage.setItemViewCacheSize(sliderItems.size());
        centerSnapHelper.attachToRecyclerView(rvImage);

        galleryLayoutManager.scrollToPosition(1);


    }

    private void loadData() {
        StringRequest request = new StringRequest(StringRequest.Method.POST, URL_SHOW, response -> {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            if (!response.equals("")){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++ ){
                        JSONObject object = jsonArray.getJSONObject(i);

                        SliderItem item = new SliderItem();

                        item.setImage_url(object.getString("image_url"));

                        sliderItems.add(item);

                    }
                    recyclerviewSetting();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },error -> {
            error.printStackTrace();
            Log.d("errorTrace :", error.getMessage());

        }){

        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

}