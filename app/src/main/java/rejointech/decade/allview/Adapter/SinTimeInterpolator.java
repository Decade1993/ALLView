package rejointech.decade.allview.Adapter;

import android.animation.TimeInterpolator;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class SinTimeInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        return (float) Math.sin(input * Math.PI / 2);
    }
}
