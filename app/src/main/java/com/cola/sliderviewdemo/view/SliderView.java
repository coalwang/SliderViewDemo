package com.cola.sliderviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.cola.sliderviewdemo.R;

/**
 * 自定义滑块view，移动时没有动画，直接跳过去
 * created by wangkai 2018/6/11
 */
public class SliderView extends View {

    private static final String TAG = SliderView.class.getSimpleName();

    // 画笔
    private Paint mPaint;

    // 当前位置
    private int mSliderViewLeft = 0;
    private int mSliderViewRight = 0;

    // 父view的location
    private int[] mParentViewLocation;

    // 监听接口
    private OnMoveListener mOnMoveListener;

    /**
     * 监听开始移动和结束移动
     */

    public interface OnMoveListener {
        void endMove();

        void startMove();
    }

    public void setmOnMoveListener(OnMoveListener onMoveListener) {
        this.mOnMoveListener = onMoveListener;
    }

    public SliderView(Context context) {
        super(context);
        init();

    }

    public SliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 设置view的颜色，即设置画笔的颜色
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getVisibility() == View.VISIBLE){
            // 画一个矩形框
            // 五个参数
            // 第一个参数：距离其父容器的左边的距离
            // 第二个参数：距离父容器顶部的距离
            // 第三个参数：距离父容器
            // 第四个参数：矩形底部距离父容器顶部的距离
            // 第五个参数：画笔
            // 特别注意，这里的父容器指的是我们的SliderView
            canvas.drawRect(mSliderViewLeft, 0, mSliderViewRight, getHeight(), mPaint); //getHeight()它指的是父容器sliderView的高度
            // 画出滑块之后，设置监听结束回调的方法
            if (mOnMoveListener != null){
                mOnMoveListener.endMove();
            }
        }

    }

    /**
     * 初始化父view的位置
     */
    private void initParentLocation(){
        this.mParentViewLocation = new int[2];
        if ((getParent() == null) || (getParent() instanceof ViewGroup)){
            return;
        }
        ((View)getParent()).getLocationOnScreen(this.mParentViewLocation);
    }

    /**
     * 获取父布局的相对位置
     */
    private void getRelativePositionInParent(int[] destinationViewLocation) {
        initParentLocation();
        destinationViewLocation[0] = destinationViewLocation[0] - this.mParentViewLocation[0];
        destinationViewLocation[1] = destinationViewLocation[1] - this.mParentViewLocation[1];
    }

    /**
     * 移动到目标view
     * @param destinationView
     */
    public void moveToDestinationView(final View destinationView){
        if (destinationView == null){
            return;
        }
        if (destinationView.getWidth() == 0){
            // 有时候需要在onCreate方法中知道某个View组件的宽度和高度等信息，
            // 而直接调用View组件的getWidth()、getHeight()、getMeasuredWidth()、
            // getMeasuredHeight()、getTop()、getLeft()等方法是无法获取到真实值的，只会得到0。
            // 这是因为View组件布局要在onResume回调后完成。下面提供实现方法，
            // onGlobalLayout回调会在view布局完成时自动调用:
            // OnGlobalLayoutListener 是ViewTreeObserver的内部类，
            // 当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到，
            // OnGlobalLayoutListener可能会被多次触发，因此在得到了高度之后，要将OnGlobalLayoutListener注销掉。
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int[] destinationViewLocation = new int[2];
                    // getLocationOnScreen(int[] a)：以屏幕左上角为原点，destinationView的左上角的坐标
                    // 拿到destinationView的左上角的x,y坐标
                    destinationView.getLocationOnScreen(destinationViewLocation);
                    int[] destinationWidthAndHeight = new int[2];
                    destinationWidthAndHeight[0] = destinationView.getWidth();
                    destinationWidthAndHeight[1] = destinationView.getHeight();
                    moveToDestinationView(destinationViewLocation, destinationWidthAndHeight);
                }
            });
        }else{
            int[] destinationViewLocation = new int[2];
            // getLocationOnScreen(int[] a)：以屏幕左上角为原点，destinationView的左上角的坐标
            // 拿到destinationView的左上角的x,y坐标
            destinationView.getLocationOnScreen(destinationViewLocation);
            int[] destinationWidthAndHeight = new int[2];
            destinationWidthAndHeight[0] = destinationView.getWidth();
            destinationWidthAndHeight[1] = destinationView.getHeight();
            moveToDestinationView(destinationViewLocation, destinationWidthAndHeight);
        }
        }

    /**
     * 移动到目标view
     */
    private void moveToDestinationView(int[] destinationViewLocation, int[] destinationViewWidthAndHeight){
        if (destinationViewWidthAndHeight == null){
            destinationViewWidthAndHeight = new int[2];
            destinationViewWidthAndHeight[0] = mSliderViewRight - mSliderViewLeft;
            destinationViewWidthAndHeight[1] = getHeight();
        }

        // 获取相对父布局的位置
        getRelativePositionInParent(destinationViewLocation);

        if (mOnMoveListener != null){
            mOnMoveListener.startMove();  // 滑块开始移动监听
        }

        mSliderViewLeft = destinationViewLocation[0];
        mSliderViewRight = mSliderViewLeft + destinationViewWidthAndHeight[0];
        // 刷新界面，调用方法后会调用一些onDraw()方法
        invalidate();

    }

}

