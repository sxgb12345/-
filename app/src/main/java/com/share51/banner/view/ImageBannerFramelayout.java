package com.share51.banner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.share51.banner.R;

import java.util.List;

/**
 * Created by gaobo on 2018/5/24
 */
public class ImageBannerFramelayout extends FrameLayout {
    private BannerGroupView bannerGroupView;
    private LinearLayout linearLayout;

    public ImageBannerFramelayout(@NonNull Context context) {
        this(context, null);
    }

    public ImageBannerFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBannerFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageBannerViewGroup();
        initDotLineLayout();
    }


    /**
     * 初识化自定义的图片轮播功能的核心
     */
    private void initImageBannerViewGroup() {
        bannerGroupView = new BannerGroupView(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bannerGroupView.setLayoutParams(lp);
        addView(bannerGroupView);
        bannerGroupView.setImageBannerViewLinstener(new BannerGroupView.ImageBannerViewLinstener() {
            @Override
            public void selectImage(int index) {
                int count = linearLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    ImageView iv = (ImageView) linearLayout.getChildAt(i);
                    if (i == index) {
                        iv.setImageResource(R.drawable.dot_select);
                    } else {
                        iv.setImageResource(R.drawable.dot_normal);
                    }
                }
            }
        });
        bannerGroupView.setBanagerLinstener(new BannerGroupView.BanagerLinstener() {
            @Override
            public void clickImageIndex(int pos) {
                Toast.makeText(getContext(), pos + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化 我们的底部圆点布局
     */
    private void initDotLineLayout() {
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.rgb(33, 33, 33));
        addView(linearLayout);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        linearLayout.setAlpha(0.4f);
    }

    public void addBitmaps(List<Bitmap> list) {
        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            addBitmapToImageViewGroup(bitmap);
            addDotToImageLayout(i);
        }
    }

    private void addDotToImageLayout(final int pos) {
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(iv);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerGroupView.setIndex(pos);
            }
        });
    }

    private void addBitmapToImageViewGroup(Bitmap bitmap) {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setImageBitmap(bitmap);
        bannerGroupView.addView(iv);
    }

}
