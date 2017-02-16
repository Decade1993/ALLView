package rejointech.decade.allview.DJN_Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import rejointech.decade.allview.Adapter.SinTimeInterpolator;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class SuperLoadView extends View {

    private static final String TAG = "SuperLoadView";

    /**
     * 是否绘制成功
     */
    boolean ifSuccess;
    /**
     * view's size
     */
    private Integer mWidth;
    private Integer mHeight;
    /**
     * 起始角度
     */
    private static final float startAngle = -90;
    /**
     * 画笔
     */
    Paint circlePaint = new Paint();
    Paint smallRectPaint = new Paint();
    Paint tickPaint = new Paint();
    /**
     * 最大进度
     */
    private static final int maxProgress = 100;

    /**
     * 大圆圈动画时间
     */
    private final int mDuration = 3000;

    /**
     * 当前进度
     */
    private int progress = 0;
    /**
     * 当前完成百分比
     */
    float percent;
    /**
     * 大圆位置
     */
    RectF mRectF = new RectF();
    /**
     *大圆半径
     */
    int radius;
    /**
     * 画笔宽度
     */
    int strokeWidth = 10;
    /**
     * 当前状态
     */
    private int status = 0;
    /**
     * 小方块抛出动画角度
     */
    private float endAngle = (float)Math.atan(4f/3);
    /**
     * 小方块抛出动画当前角度
     */
    private float curSweepAngle;
    /**
     * 小方块下落路径
     */
    private PathMeasure downPathMeasure1;
    private PathMeasure downPathMeasure2;
    /**
     * 小方块下落进度
     */
    private float downPrecent = 0;
    /**
     * 三叉路径
     */
    private PathMeasure forkMeasure1;
    private PathMeasure forkMeasure2;
    private PathMeasure forkMeasure3;
    /**
     * 三叉进度
     */
    private float forkPrecent;

    ValueAnimator animator_circle;
    ValueAnimator animator_01;
    ValueAnimator animator_02;
    ValueAnimator animator_03;
    ValueAnimator animator_04;

    /**
     * 打勾路径
     */
    PathMeasure tickPathMeasure;
    /**
     * 打勾进度
     */
    float tickPrecent;

    public SuperLoadView(Context context) {
        super(context);
        initPaint();
        start(true);
    }

    public SuperLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        start(true);
    }

    public SuperLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        start(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        radius = Math.min(getMeasuredWidth(),getMeasuredHeight())/4-strokeWidth;
        mRectF.set(new RectF(radius+strokeWidth, radius+strokeWidth, 3 * radius+strokeWidth, 3 * radius+strokeWidth));
        Path downPath1 = new Path();
        downPath1.moveTo(2 * radius+strokeWidth,strokeWidth);
        downPath1.lineTo(2 * radius+strokeWidth, 1.2f*radius+strokeWidth);
        Path downPath2 = new Path();
        downPath2.moveTo(2 * radius+strokeWidth, strokeWidth);
        downPath2.lineTo(2 * radius+strokeWidth, 2 * radius+strokeWidth);
        downPathMeasure1 = new PathMeasure(downPath1,false);
        downPathMeasure2 = new PathMeasure(downPath2,false);

        float sin_45 = (float)Math.sin(45.0 / 180 * Math.PI);
        Path forkPath1 = new Path();
        forkPath1.moveTo(2 * radius+strokeWidth,2 * radius+strokeWidth);
        forkPath1.rLineTo(-radius * sin_45,radius * sin_45);
        Path forkPath2 = new Path();
        forkPath2.moveTo(2 * radius+strokeWidth,2 * radius+strokeWidth);
        forkPath2.rLineTo(radius * sin_45,radius * sin_45);
        Path forkPath3 = new Path();
        forkPath3.moveTo(2 * radius+strokeWidth, (float) (1.2 * radius + strokeWidth));
        forkPath3.lineTo(2 * radius+strokeWidth,mRectF.top);
        forkMeasure1 = new PathMeasure(forkPath1,false);
        forkMeasure2 = new PathMeasure(forkPath2,false);
        forkMeasure3 = new PathMeasure(forkPath3,false);

        Path tickPath = new Path();
        tickPath.moveTo(1.5f * radius+strokeWidth, 2 * radius+strokeWidth);
        tickPath.lineTo(1.5f * radius + 0.3f * radius+strokeWidth, 2 * radius + 0.3f * radius+strokeWidth);
        tickPath.lineTo(2*radius+0.5f * radius+strokeWidth,2*radius-0.3f * radius+strokeWidth);
        tickPathMeasure = new PathMeasure(tickPath,false);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    void drawCircle(Canvas canvas){
        percent = 1.0095f*progress/maxProgress;//当前完成百分比
        percent = percent > 1 ? 1 : percent;
        canvas.drawArc(mRectF,startAngle-270*percent,-(60+300*percent),false,circlePaint);
    }

    /**
     * 画抛出小方块
     * @param canvas
     */
    void drawSmallRectFly(Canvas canvas){
        canvas.save();
        canvas.translate(radius/2+strokeWidth,2 * radius + strokeWidth);
        float bigRadius = 5 * radius / 2;
        float x1 = (float) (bigRadius*Math.cos(curSweepAngle));
        float y1 = -(float) (bigRadius*Math.sin(curSweepAngle));
        float x2 = (float) (bigRadius*Math.cos(curSweepAngle+0.05*endAngle+0.05*endAngle*(1-curSweepAngle/0.9*endAngle)));
        float y2 = -(float) (bigRadius*Math.sin(curSweepAngle+0.05*endAngle+0.05*endAngle*(1-curSweepAngle/0.9*endAngle)));
        canvas.drawLine(x1, y1, x2, y2, smallRectPaint);
        canvas.restore();
    }

    /**
     * 画小方块下落
     * @param canvas
     */
    void drawSmallRectDown(Canvas canvas){
        float circlePrecent = downPrecent < 0.5 ? 0 : downPrecent-0.5f;
        float pos1[] = new float[2];
        float pos2[] = new float[2];
        downPathMeasure1.getPosTan(downPrecent * downPathMeasure1.getLength(),pos1,null);
        downPathMeasure2.getPosTan(downPrecent * downPathMeasure2.getLength(),pos2,null);
        RectF mRect = new RectF(Math.round(mRectF.left),Math.round(mRectF.top+mRectF.height()*0.2f*circlePrecent),
                Math.round(mRectF.right),Math.round(mRectF.bottom-mRectF.height()*0.2f*circlePrecent));
        canvas.drawArc(mRect,0,360,false,circlePaint);
        canvas.drawLine(pos1[0],pos1[1],pos2[0],pos2[1],smallRectPaint);
        if(circlePrecent > 0){
            canvas.drawLine(pos2[0],mRectF.top+mRectF.height()*0.2f*circlePrecent,pos2[0],pos2[1],circlePaint);
        }
    }

    /**
     * 画三叉
     * @param canvas
     */
    void drawFork(Canvas canvas){
        float pos1[] = new float[2];
        float pos2[] = new float[2];
        float pos3[] = new float[2];
        forkMeasure1.getPosTan(forkMeasure1.getLength()*forkPrecent,pos1,null);
        forkMeasure2.getPosTan(forkMeasure2.getLength()*forkPrecent,pos2,null);
        forkMeasure3.getPosTan(forkMeasure3.getLength()*forkPrecent,pos3,null);
        canvas.drawLine(2 * radius + strokeWidth,2 * radius + strokeWidth,pos1[0],pos1[1],circlePaint);
        canvas.drawLine(2 * radius + strokeWidth,2 * radius + strokeWidth,pos2[0],pos2[1],circlePaint);
        canvas.drawLine(2 * radius + strokeWidth,2 * radius + strokeWidth,pos3[0],pos3[1],circlePaint);
        RectF mRect = new RectF(Math.round(mRectF.left),Math.round(mRectF.top+mRectF.height() * 0.1f * (1 - forkPrecent)),
                Math.round(mRectF.right),Math.round(mRectF.bottom-mRectF.height()  *0.1f * (1 - forkPrecent)));
        canvas.drawArc(mRect,0,360,false,circlePaint);

    }

    /**
     * 画成功
     * @param canvas
     */
    void drawSuccess(Canvas canvas){
        Path path = new Path();
        tickPathMeasure.getSegment(0,tickPathMeasure.getLength() * tickPrecent,path,true);
        canvas.drawPath(path, tickPaint);//绘制出这一部分
        canvas.drawArc(mRectF, 0, 360, false, tickPaint);
    }

    void initPaint(){
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(strokeWidth);

        smallRectPaint.setAntiAlias(true);
        smallRectPaint.setColor(Color.BLUE);
        smallRectPaint.setStrokeWidth(strokeWidth/2);
        smallRectPaint.setStyle(Paint.Style.STROKE);

        tickPaint.setAntiAlias(true);
        tickPaint.setColor(Color.GREEN);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeWidth(strokeWidth);
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        switch (status){
            /**
             * 画大圆圈
             */
            case 0:
                drawCircle(canvas);
                break;
            /**
             * 小方块抛出
             */
            case 1:
                canvas.drawArc(mRectF, 0, 360, false, circlePaint);
                drawSmallRectFly(canvas);
                break;
            /**
             * 小方块下落
             */
            case 2:
                drawSmallRectDown(canvas);
                break;
            /**
             * 三叉
             */
            case 3:
                drawFork(canvas);
                break;
            case 4:
                drawSuccess(canvas);
                break;
        }
    }

    public void setCurSweepAngle(float curSweepAngle) {
        this.curSweepAngle = curSweepAngle;
        invalidate();
    }

    public void setDownPrecent(float downPrecent) {
        this.downPrecent = downPrecent;
        invalidate();
    }

    public void setForkPrecent(float forkPrecent) {
        this.forkPrecent = forkPrecent;
        invalidate();
    }

    public void setTickPrecent(float tickPrecent) {
        this.tickPrecent = tickPrecent;
        invalidate();
    }

    public void start(boolean isSuccess){

        /**
         * init
         */
        this.progress = 0;
        this.status = 0;
        this.percent = 0;
        this.forkPrecent = 0;
        this.downPrecent = 0;
        this.curSweepAngle = 0;
        this.tickPrecent = 0;
        this.ifSuccess = isSuccess;
        /**
         * 大圆圈动效
         */
        animator_circle = ObjectAnimator.ofInt(this,"progress",0,maxProgress);
        animator_circle.setDuration(mDuration);
        animator_circle.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_circle.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = 1;
                animator_01.start();
            }
        });

        /**
         * 小方块抛出动效
         */
        animator_01 = ValueAnimator.ofFloat(0f,endAngle * 0.9f);
        animator_01.setDuration(500);
        animator_01.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_01.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurSweepAngle((float)animation.getAnimatedValue());
            }
        });
        animator_01.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = 2;
                animator_02.start();
            }
        });

        /**
         * 小方块下落动画
         */
        animator_02 = ValueAnimator.ofFloat(0f,1f);
        animator_02.setDuration(2000);
        animator_02.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_02.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setDownPrecent((float)animation.getAnimatedValue());
            }
        });
        animator_02.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = 3;
                animator_03.start();
            }
        });

        /**
         * 三叉动画
         */
        animator_03= ValueAnimator.ofFloat(0f,1f);
        animator_03.setDuration(2000);
        animator_03.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_03.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setForkPrecent((float)animation.getAnimatedValue());
            }
        });
        animator_03.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = 4;
                animator_04.start();
            }
        });

        animator_04 = ValueAnimator.ofFloat(0f,1f);
        animator_04.setDuration(1000);
        animator_04.setInterpolator(new AccelerateInterpolator());
        animator_04.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTickPrecent((float)animation.getAnimatedValue());
            }
        });


        animator_circle.start();
    }
}
