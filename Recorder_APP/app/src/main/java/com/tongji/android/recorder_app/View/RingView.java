
package com.tongji.android.recorder_app.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.tongji.android.recorder_app.R;


public class RingView extends View
{
    private int color;
    private float percentage;
    private float labelMarginTop;
    private TextPaint pRing;
    private String label;
    private RectF rect;
    private StaticLayout labelLayout;

    private int width;
    private int height;
    private float diameter;

    private float maxDiameter;
    private float textSize;
    private int textColor;
    private int backgroundColor;

    public RingView(Context context)
    {
        super(context);
        init();
    }

    public RingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.label = "tensity";
        this.maxDiameter = 400;
        this.maxDiameter = 400;
        init();
    }

    public void setColor(int color)
    {
        this.color = color;
        postInvalidate();
    }

    public void setMaxDiameter(float maxDiameter)
    {
        this.maxDiameter = maxDiameter;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setPercentage(float percentage)
    {
        this.percentage = percentage;
        postInvalidate();
    }

    private void init()
    {
        pRing = new TextPaint();
        pRing.setAntiAlias(true);
        pRing.setColor(color);
        pRing.setTextAlign(Paint.Align.CENTER);

        backgroundColor = getResources().getColor(R.color.lightBackground);
        textColor = getResources().getColor(R.color.colorPrimary);
        textSize = getResources().getDimensionPixelSize(R.dimen.text_size);

        rect = new RectF();
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        diameter = Math.min(maxDiameter, width);

        pRing.setTextSize(textSize);
        labelMarginTop = textSize * 0.80f;
        labelLayout = new StaticLayout(label, pRing, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f,
                false);

        width = Math.max(width, labelLayout.getWidth());
        height = (int) (diameter + labelLayout.getHeight() + labelMarginTop);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float thickness = diameter * 0.15f;

        pRing.setColor(color);
        rect.set(0, 0, diameter, diameter);
        rect.offset((width - diameter) / 2, 0);
        canvas.drawArc(rect, -90, 360 * percentage, true, pRing);

        int grey = getResources().getColor(R.color.circleBackground);
        pRing.setColor(grey);
        canvas.drawArc(rect, 360 * percentage - 90 + 2, 360 * (1 - percentage) - 4, true, pRing);

        pRing.setColor(backgroundColor);
        rect.inset(thickness, thickness);
        canvas.drawArc(rect, -90, 360, true, pRing);

        pRing.setColor(textColor);
        pRing.setTextSize(textSize);
        float lineHeight = pRing.getFontSpacing();
        canvas.drawText(String.format("%.0f%%", percentage * 100), rect.centerX(),
                rect.centerY() + lineHeight / 3, pRing);
        pRing.setTextSize(textSize);
        canvas.translate(width / 2, diameter + labelMarginTop);
        labelLayout.draw(canvas);
    }
}
