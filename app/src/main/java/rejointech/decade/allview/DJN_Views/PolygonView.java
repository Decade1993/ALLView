package rejointech.decade.allview.DJN_Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Queue;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class PolygonView extends View {

    int count = 18;
    Paint mPaint = new Paint();

    Integer mWidth;
    Integer mHeight;

    Queue<Point> q;


    public PolygonView(Context context) {
        super(context);
        initPaints();
        getWindowSize();
    }

    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        getWindowSize();
    }

    public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
        getWindowSize();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        canvas.restore();
        canvas.save();
        drawPolygon(canvas,50);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,100);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,150);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,200);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,250);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,300);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,350);
        canvas.restore();
        canvas.save();
        drawPolygon(canvas,400);
        canvas.restore();
        canvas.save();
        drawLine(canvas,400);

    }
    void drawLine(Canvas canvas,int radius){
        canvas.translate(mWidth/2, mHeight/2);
        Path path = new Path();
        path.lineTo(0,radius);
        canvas.rotate(180/count);
        for (int i = 0; i < count; i++){
            canvas.drawPath(path,mPaint);
            canvas.rotate(360/count);
        }

    }
    void drawTest(Canvas canvas){
        canvas.translate(mWidth/2, mHeight/2);

        Path path = new Path();
        path.lineTo(200,200);
        canvas.rotate(90);
        canvas.drawPath(path, mPaint);

        canvas.restore();
        canvas.save();
        canvas.translate(mWidth/2, mHeight/2);
        path.reset();
        path.setLastPoint(-200,0);
        path.lineTo(200,0);

        canvas.drawPath(path, mPaint);
    }
    void drawPolygon(Canvas canvas,int radius){
        canvas.translate(mWidth/2, mHeight/2);
        Path path = new Path();
        float y = (float) (radius * Math.cos(Math.PI/count));
        float x = (float) (radius * Math.sin(Math.PI/count));
        path.setLastPoint(-x,y);
        path.lineTo(x,y);
        for (int i = 0;i<count;i++){
            canvas.drawPath(path,mPaint);
            canvas.rotate(360/count);
        }

    }

    void initPaints(){
        mPaint.setColor(Color.RED);           // 画笔颜色 - 黑色
        mPaint.setStyle(Paint.Style.STROKE);    // 填充模式 - 描边
        mPaint.setStrokeWidth(5);
    }

    void getWindowSize(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        Log.i("windowSize", "Width:" + mWidth + ",Height:" + mHeight);
    }
    class Point{
        Integer x;
        Integer y;
        Point(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

}
