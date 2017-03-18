package tech.kolektiv.forum.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import tech.kolektiv.forum.R;


public class AnimatedCircle extends View {

    private float strokeWidth = 4;
    private float progress = 0;
    private int min = 0;
    private int max = 100;
    private ObjectAnimator objectAnimator;

    private int color = Color.DKGRAY;
    private RectF rectF;
    private Paint strokeBackGround;
    private Paint arcPaint;
    private Paint circlePaint;

    public AnimatedCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AnimatedCircle,
                0, 0);
        int circleColor;

        try {
            strokeWidth = typedArray.getDimension(R.styleable.AnimatedCircle_strokeThickness, strokeWidth);
            progress = typedArray.getFloat(R.styleable.AnimatedCircle_strokeProgress, progress);
            color = typedArray.getColor(R.styleable.AnimatedCircle_strokeColor, color);
            min = typedArray.getInt(R.styleable.AnimatedCircle_min, min);
            max = typedArray.getInt(R.styleable.AnimatedCircle_max, max);
            circleColor =  typedArray.getColor(R.styleable.AnimatedCircle_circleColor, color);
        } finally {
            typedArray.recycle();
        }

        strokeBackGround = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokeBackGround.setColor(circleColor);
        strokeBackGround.setStyle(Paint.Style.STROKE);
        strokeBackGround.setStrokeWidth(strokeWidth);
        strokeBackGround.setAlpha(100);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeWidth(strokeWidth);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setColor(lightenColor(color,0.90f));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAlpha(100);
        arcPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(),( rectF.width() + strokeWidth) / 2, circlePaint);
        canvas.drawOval(rectF, strokeBackGround);
        float angle = 360 * progress / max;
        int startAngle = -90;
//        canvas.drawArc(rectF, startAngle, angle, false, arcPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

    public int lightenColor(int color, float factor) {
        float r = Color.red(color) * factor;
        float g = Color.green(color) * factor;
        float b = Color.blue(color) * factor;
        int ir = Math.min(255, (int) r);
        int ig = Math.min(255, (int) g);
        int ib = Math.min(255, (int) b);
        int ia = Color.alpha(color);
        return (Color.argb(ia, ir, ig, ib));
    }

    public void  stopAnimation () {
        if(objectAnimator != null && objectAnimator.isRunning()){
            objectAnimator.end();
            setProgress(0);
        }
    }

    public void animateTo(float progress, long duration) {
        if(objectAnimator != null && objectAnimator.isRunning()){
            objectAnimator.end();
        }
        objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
