package com.share51.banner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.share51.banner.view.BannerGroupView;
import com.share51.banner.view.ImageBannerFramelayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerGroupView banner;

    private ImageBannerFramelayout banner_2;
    private int ids[] = new int[]{
            R.drawable.top1, R.drawable.top2, R.drawable.top3, R.drawable.top4, R.drawable.top5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
//        banner = (BannerGroupView) findViewById(R.id.banner);
//
//        for (int i = 0; i < ids.length; i++) {
//            ImageView iv = new ImageView(this);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            iv.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
//            iv.setImageResource(ids[i]);
//            banner.addView(iv);
//        }
//        banner.setBanagerLinstener(new BannerGroupView.BanagerLinstener() {
//            @Override
//            public void clickImageIndex(int pos) {
//                Toast.makeText(getApplicationContext(), pos + "", Toast.LENGTH_SHORT).show();
//            }
//        });
        banner_2 = (ImageBannerFramelayout) findViewById(R.id.banner_2);
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            list.add(bitmap);
        }
        banner_2.addBitmaps(list);
    }

}
