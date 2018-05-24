package com.share51.banner.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建轮播图的父容器
 * Created by gaobo on 2018/5/24
 */
public class BannerGroupView extends ViewGroup {

    private int childrenNum;//子视图的个数
    private int width;//容器的宽度
    private int height;//容器的高度
    private int childrenWidth;//子视图的宽度
    private int childrenHeight;//子视图的高度
    private int index = 0;//子视图的索引
    private int x;//代表第一次手按下的横坐标，每次移动过程中移动之前的位置的横坐标

    /**
     * 要想实现图片底部的圆点及随图片转动
     * 1、 自定义一个继承frameLayout的布局，利用frameLayout布局的特性可以实现底部远点的布局
     * 2、 准备素材，利用drable功能实现一个圆点图的展示
     * 3、 需要继承frameLayout来自定义一个类，在该类的实现过程中我们去加载刚才自定义的图片轮播类，实现底部的布局，使用lineLayout
     */

    //自动轮播
    private boolean isAuto = true;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (++index >= childrenNum) {//如果滑动到最后一页 则从第一页开始重新滑动
                        index = 0;
                    }
                    scrollTo(childrenWidth * index, 0);
                    imageBannerViewLinstener.selectImage(index);
                    break;
            }
        }
    };
    private BanagerLinstener banagerLinstener;
    private boolean isClick;//用户点击  true为点击  false为非点击

    public void setBanagerLinstener(BanagerLinstener banagerLinstener) {
        this.banagerLinstener = banagerLinstener;
    }

    public interface BanagerLinstener {
        /**
         * 用户点击图片时的触发事件
         *
         * @param pos 具体的点击图片的索引
         */
        void clickImageIndex(int pos);
    }


    public BannerGroupView(Context context) {
        this(context, null);
    }

    public BannerGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    private void initObj() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {//开启轮播图
                    handler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(timerTask, 3000, 3000);
    }

    /**
     * 启动自动轮播
     */
    private void startAuto() {
        isAuto = true;
    }

    /**
     * 停止自动轮播
     */
    private void stopAuto() {
        isAuto = false;
    }


    /**
     * 定义的viewgroup中必须实现的方法有  测量-->布局-->绘制
     * <p>
     * 容器的绘制过程 只需要调用系统自带的方法即可
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 由于实现一个viewgroup容器，所以就应该知道其中的所有子视图，我们要测量viewgroup的宽度和高度，必须先测量子视图的宽度和高度之和，才能知道
         * viewgroup的宽和高
         */
        //1、求出子视图的个数
        childrenNum = getChildCount();
        if (childrenNum == 0) {//若子视图的个数为0，则设置其容器宽高为0
            setMeasuredDimension(0, 0);
        } else {
            //2、测量子视图的宽高
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //以第一个子视图为基准也就是viewgrup的高度就是第一个子视图的高度，宽度也是子视图的宽度*子视图的个数
            View view = getChildAt(0);
            childrenHeight = view.getMeasuredHeight();
            childrenWidth = view.getMeasuredWidth();
            //3、根据子视图的宽高求出viewgroup的宽高
            width = view.getWidth() * childrenNum;
            height = childrenHeight;
            setMeasuredDimension(width, height);
        }

    }

    /**
     * 事件的传递过程中的调用方法、我们调用容器的拦截方法
     *
     * @param ev
     * @return 若为true我们的viewgroup就会拦截事件 若为false则自定义的viewgroup将不会接受自定义的事件处理、将继续事件传递下去
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 处理用户自定义的事件拦截
     * <p>
     * 使用两种方法实现轮播图的手动轮播
     * 1、 利用scrollTo()、scrollBy()完成手势轮播
     * 2、 利用Scroller对象实现轮播
     * 第一  我们在滑动图片的过程就是viewgroup的移动过程、那么只要知道移动之前的位置和滑动后的坐标就可求出移动距离
     * 使用scrollBy()方法实现图片滑动所以需要两个值、移动前后的横坐标的值
     * 第二   我们在手按下的一瞬间，此时移动之前和移动之后的值是相等的、也就是我们按下的瞬间的横坐标的值
     * <p>
     * 第三   我们在手指滑动过程中，不断调触发CTION_MOVE事件，我们就应该讲移动前后的值保存，可以计算滑动的距离
     * <p>
     * 第四   我们手抬起的瞬间，我们计算滑动到那张图片上，此时需要求得要滑动到图片的索引index  计算方法为：
     * (当前滑动的位置+每个子视图/2)/子视图的宽度  就是索引值，然后利用scrollTo()方法滑动到图片的方法
     *
     * @param event
     * @return 若为true则执行自定义的事件方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://用户点击屏幕是触发
                x = (int) event.getX();
                stopAuto();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE://用户手在屏幕上滑动触发  移动过程中
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance, 0);
                x = moveX;
                isClick = false;
                break;
            case MotionEvent.ACTION_UP://用户手离开屏幕触发
                int scrollX = getScrollX();
                index = (scrollX + childrenWidth / 2) / childrenWidth;
                if (index < 0) {//说明此时已经滑动到最左侧第一张图片
                    //index = 0;
                    index = childrenNum - 1;
                } else if (index > childrenNum - 1) {//说明此时滑动到最右侧的图片
                    //index = childrenNum - 1;
                    index = 0;
                }
                if (isClick) {//代表是单击事件
                    if (banagerLinstener != null) {
                        banagerLinstener.clickImageIndex(index);
                    }

                } else {
                    scrollTo(index * childrenWidth, 0);
                }
                startAuto();
                imageBannerViewLinstener.selectImage(index);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 继承viewgroup实现的布局
     *
     * @param changed 若为true 则viewgroup的位置发生改变，false则未改变
     * @param l       距离左边距离
     * @param t       距离顶部距离
     * @param r       左侧到右侧距离
     * @param b       顶部到底部距离
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int leftMargin = 0;
            for (int i = 0; i < childrenNum; i++) {
                //获得每个子视图的对象
                View view = getChildAt(i);
                //设置每个子视图的布局  距离左侧位置为上次的位置加子视图的宽度、顶部位置为0、
                // 右侧位置为此子视图的leftMargin+视图宽度、底部距离为视图的高度
                view.layout(leftMargin, 0, leftMargin + childrenWidth, height);
                leftMargin += childrenWidth;
            }
        }
    }

    private ImageBannerViewLinstener imageBannerViewLinstener;

    public void setImageBannerViewLinstener(ImageBannerViewLinstener imageBannerViewLinstener) {
        this.imageBannerViewLinstener = imageBannerViewLinstener;
    }

    public interface ImageBannerViewLinstener {
        void selectImage(int index);
    }

    public void setIndex(int index) {
        if (index < 0) {
            index = 0;
        } else if (index > childrenNum - 1) {
            index = childrenNum - 1;
        }
//        scrollTo(childrenWidth * index, 0);
//        imageBannerViewLinstener.selectImage(index);
        this.index = index - 1;
        handler.sendEmptyMessage(0);
    }

}
