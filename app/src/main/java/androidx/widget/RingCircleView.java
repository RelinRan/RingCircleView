package androidx.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 圆环+圆（文字）
 */
public class RingCircleView extends View implements ValueAnimator.AnimatorUpdateListener {

    //宽高
    private float width, height;
    //中心坐标
    private float cx, cy;
    //圆圈背景宽度
    private int ringBackgroundStrokeWidth = dip(8);
    //圆圈背景宽度
    private int ringProgressStrokeWidth = dip(12);
    //圆圈背景颜色
    private int ringBackgroundColor = Color.parseColor("#8D8D8D");
    //圆圈进度颜色
    private int ringProgressColor = Color.parseColor("#00CACF");
    //圆圈和圆的间距
    private int circleRingSpace = dip(80);
    //圆圈背景宽度
    private int circleRadius = dip(45);
    //圆圈背景颜色
    private int circleBackgroundColor = Color.parseColor("#00CACF");
    //圆圈文字颜色
    private int circleTextColor = Color.parseColor("#FFFFFF");
    //圆圈文字大小
    private int circleTextSize = dip(14);
    //圆圈文字间距
    private int circleTextSpace = dip(10);
    //进度值
    private int progress = 50;
    private int max = 100;
    //动画对象
    private ValueAnimator animator;
    //动画持续时间
    private long duration = 500;
    //文字
    private String text = "已完成";

    public RingCircleView(Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public RingCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public RingCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RingCircleView);
            ringBackgroundStrokeWidth = array.getDimensionPixelOffset(R.styleable.RingCircleView_ringBackgroundStrokeWidth, ringBackgroundStrokeWidth);
            ringProgressStrokeWidth = array.getDimensionPixelOffset(R.styleable.RingCircleView_ringProgressStrokeWidth, ringProgressStrokeWidth);
            ringBackgroundColor = array.getColor(R.styleable.RingCircleView_ringBackgroundColor, ringBackgroundColor);
            ringProgressColor = array.getColor(R.styleable.RingCircleView_ringProgressColor, ringProgressColor);
            circleRingSpace = array.getDimensionPixelOffset(R.styleable.RingCircleView_circleRingSpace, circleRingSpace);
            circleRadius = array.getDimensionPixelOffset(R.styleable.RingCircleView_circleRadius, circleRadius);
            circleBackgroundColor = array.getColor(R.styleable.RingCircleView_circleBackgroundColor, circleBackgroundColor);
            circleTextColor = array.getColor(R.styleable.RingCircleView_circleTextColor, circleTextColor);
            circleTextSize = array.getDimensionPixelOffset(R.styleable.RingCircleView_circleTextSize, circleTextSize);
            circleTextSpace = array.getDimensionPixelOffset(R.styleable.RingCircleView_circleTextSpace, circleTextSpace);
            progress = array.getInt(R.styleable.RingCircleView_progress, progress);
            max = array.getInt(R.styleable.RingCircleView_max, max);
            text = array.getString(R.styleable.RingCircleView_text);
            text = text == null || text.length() == 0 ? "已完成" : text;
            array.recycle();
        }
    }

    /**
     * @param value
     * @return 尺寸转换
     */
    protected int dip(int value) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * value);
    }

    /**
     * 设置最大值
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    /**
     * 设置进度值
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        startAnimation(progress);
    }

    /**
     * 开始动画
     *
     * @param progress
     */
    private void startAnimation(int progress) {
        animator = ValueAnimator.ofInt(progress);
        animator.setDuration(duration);
        animator.addUpdateListener(this);
        animator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        progress = (int) animation.getAnimatedValue();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        cx = width / 2;
        cy = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int value = (int) (max * (progress * 1D / max * 1D));
        drawInnerCircle(canvas, value);
    }

    /**
     * 绘制内部圆
     *
     * @param canvas   画布
     * @param progress 进度值
     */
    private void drawInnerCircle(Canvas canvas, int progress) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleBackgroundColor);
        //背景圆
        canvas.drawCircle(cx, cy, circleRadius, paint);
        //文字
        paint.setColor(circleTextColor);
        paint.setTextSize(circleTextSize);
        //上部分文字
        String topText = text;
        Rect topBounds = new Rect();
        paint.getTextBounds(topText, 0, topText.length(), topBounds);
        canvas.drawText(topText, cx - topBounds.width() / 2, cy - topBounds.height() / 2, paint);
        //下部分文字
        String bottomText = progress + "%";
        Rect bottomBounds = new Rect();
        paint.getTextBounds(bottomText, 0, bottomText.length(), bottomBounds);
        canvas.drawText(bottomText, cx - bottomBounds.width() / 2, cy + bottomBounds.height() / 2 + circleTextSpace, paint);
        //外部圆圈
        drawOutRing(canvas, progress);
    }

    /**
     * 绘制外部圆圈
     *
     * @param canvas   画布
     * @param progress 进度值
     */
    private void drawOutRing(Canvas canvas, int progress) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ringBackgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringBackgroundStrokeWidth);
        //背景
        float bgLeft = cx - circleRingSpace;
        float bgTop = cy - circleRingSpace;
        float bgRight = cx + circleRingSpace;
        float bgBottom = cy + circleRingSpace;
        RectF bgOval = new RectF(bgLeft, bgTop, bgRight, bgBottom);
        canvas.drawArc(bgOval, 0, 360, false, paint);
        //进度
        paint.setColor(ringProgressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(ringProgressStrokeWidth);
        canvas.drawArc(bgOval, -90, progress * 1.0F / 100F * 360, false, paint);
    }


}

