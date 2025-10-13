package com.cybene.cyposdashboard.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashedLineView extends View {
    private Paint paint;
    private Path path;

    public DashedLineView(Context context) {
        super(context);
        init();
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        PathEffect effects = new DashPathEffect(new float[]{10, 5}, 0); // Adjust values as needed
        paint.setPathEffect(effects);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.moveTo(0, (float) getHeight() /2);
        path.lineTo(getWidth(), (float) getHeight() /2);
        canvas.drawPath(path, paint);
    }
}
