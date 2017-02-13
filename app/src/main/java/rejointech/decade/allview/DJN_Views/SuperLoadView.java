package rejointech.decade.allview.DJN_Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class SuperLoadView extends View {
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
    /**
     * 最大进度
     */
    private static final int maxProgress = 1000;
    /**
     * 当前进度
     */
    private int progress = 0;

    private int status = 0;

    public SuperLoadView(Context context) {
        super(context);
        setProgress(0);
    }

    public SuperLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProgress(0);
    }

    public SuperLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setProgress(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void setProgress(int progress) {
        this.progress = Math.min(progress,maxProgress);
        postInvalidate();
        if (progress==0){
            status = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(10);
        RectF mRectF = new RectF(0,0,mWidth,mHeight);
        float percent = 1.0f*progress/maxProgress;//当前完成百分比
        canvas.drawArc(mRectF,startAngle-270*percent,-(60+300*percent),false,circlePaint);
        setProgress(progress+1);
    }
}
