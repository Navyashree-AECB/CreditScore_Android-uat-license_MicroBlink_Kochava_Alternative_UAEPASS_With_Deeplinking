package com.aecb.presentation.arc;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aecb.R;

public class SeekArc extends View {
    private static final String TAG = SeekArc.class.getSimpleName();
    private static int INVALID_PROGRESS_VALUE = -1;
    private final int mAngleOffset = -90;
    private Drawable mThumb;
    private int mMax = 100;
    private int mProgress = 0;
    private int mProgressWidth = 4;
    private int mArcWidth = 2;
    private int mStartAngle = 0;
    private int mSweepAngle = 360;
    private int mRotation = 0;
    private boolean mRoundedEdges = false;
    private boolean mTouchInside = true;
    private boolean mClockwise = true;
    private boolean mEnabled = true;
    private boolean mHideThumb = true;
    private int mArcRadius = 0;
    private float mProgressSweep = 0.0F;
    private RectF mArcRect = new RectF();
    private Paint mArcPaint;
    private Paint mProgressPaint;
    private int mTranslateX;
    private int mTranslateY;
    private int mThumbXPos;
    private int mThumbYPos;
    private double mTouchAngle;
    private TypedArray a;
    private float mTouchIgnoreRadius;
    private SeekArc.OnSeekArcChangeListener mOnSeekArcChangeListener;

    public SeekArc(Context context) {
        super(context);
        this.init(context, (AttributeSet) null, 0);
    }

    public SeekArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, R.attr.seekArcStyle);
    }

    public SeekArc(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        Log.d(TAG, "Initialising SeekArc");
        Resources res = getResources();
        float density = context.getResources().getDisplayMetrics().density;
        int arcColor = res.getColor(R.color.progress_gray);
        int progressColor = res.getColor(R.color.default_blue_light);
        mProgressWidth = (int) ((float) mProgressWidth * density);
        if (attrs != null) {
            a = context.obtainStyledAttributes(attrs, R.styleable.SeekArc, defStyle, 0);
            Drawable thumb = a.getDrawable(R.styleable.SeekArc_seek_arc_thumb);
            if (thumb != null) {
                mThumb = thumb;
            }

            int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;
            int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;
            mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
            mMax = a.getInteger(R.styleable.SeekArc_seek_arc_max, mMax);
            mProgress = a.getInteger(R.styleable.SeekArc_seek_arc_progress, mProgress);
            mProgressWidth = (int) a.getDimension(R.styleable.SeekArc_seek_arc_progressWidth, (float) mProgressWidth);
            mArcWidth = (int) a.getDimension(R.styleable.SeekArc_seek_arc_arcWidth, (float) mArcWidth);
            mStartAngle = a.getInt(R.styleable.SeekArc_seek_arc_startAngle, mStartAngle);
            mSweepAngle = a.getInt(R.styleable.SeekArc_seek_arc_sweepAngle, mSweepAngle);
            mRotation = a.getInt(R.styleable.SeekArc_rotation, mRotation);
            mRoundedEdges = a.getBoolean(R.styleable.SeekArc_seek_arc_roundEdges, mRoundedEdges);
            mTouchInside = a.getBoolean(R.styleable.SeekArc_seek_arc_touchInside, mTouchInside);
            mClockwise = a.getBoolean(R.styleable.SeekArc_seek_arc_clockwise, mClockwise);
            mEnabled = a.getBoolean(R.styleable.SeekArc_seek_arc_enabled, mEnabled);
            mHideThumb = a.getBoolean(R.styleable.SeekArc_seek_arc_hideThumb, mHideThumb);
            arcColor = a.getColor(R.styleable.SeekArc_seek_arc_arcColor, arcColor);
            progressColor = a.getColor(R.styleable.SeekArc_seek_arc_progressColor, progressColor);
            a.recycle();
        }
        if (mHideThumb) {
            mThumb = res.getDrawable(R.drawable.seek_arc_control_selector);
        }
        mProgress = mProgress > mMax ? mMax : mProgress;
        mProgress = mProgress < 0 ? 0 : mProgress;
        mSweepAngle = mSweepAngle > 360 ? 360 : mSweepAngle;
        mSweepAngle = mSweepAngle < 0 ? 0 : mSweepAngle;
        mProgressSweep = (float) mProgress / (float) mMax * (float) mSweepAngle;
        mStartAngle = mStartAngle > 360 ? 0 : mStartAngle;
        mStartAngle = mStartAngle < 0 ? 0 : mStartAngle;
        mArcPaint = new Paint();
        mArcPaint.setColor(arcColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Style.STROKE);
        mArcPaint.setStrokeWidth((float) mArcWidth);
        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Style.STROKE);
        mProgressPaint.setStrokeWidth((float) mProgressWidth);
        if (mRoundedEdges) {
            mArcPaint.setStrokeCap(Cap.ROUND);
            mProgressPaint.setStrokeCap(Cap.ROUND);
        }

    }

    protected void onDraw(Canvas canvas) {
        if (!mClockwise) {
            canvas.scale(-1.0F, 1.0F, mArcRect.centerX(), mArcRect.centerY());
        }

        int arcStart = mStartAngle + -90 + mRotation;
        int arcSweep = mSweepAngle;
        canvas.drawArc(mArcRect, (float) arcStart, (float) arcSweep, false, mArcPaint);
        canvas.drawArc(mArcRect, (float) arcStart, mProgressSweep, false, mProgressPaint);
        if (mEnabled) {
            canvas.translate((float) (mTranslateX - mThumbXPos), (float) (mTranslateY - mThumbYPos));
            mThumb.draw(canvas);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        float top = 0.0F;
        float left = 0.0F;
        mTranslateX = (int) ((float) width * 0.5F);
        mTranslateY = (int) ((float) height * 0.5F);
        int arcDiameter = min - this.getPaddingLeft();
        mArcRadius = arcDiameter / 2;
        top = (float) (height / 2 - arcDiameter / 2);
        left = (float) (width / 2 - arcDiameter / 2);
        mArcRect.set(left, top, left + (float) arcDiameter, top + (float) arcDiameter);
        int arcStart = (int) mProgressSweep + mStartAngle + mRotation + 90;
        mThumbXPos = (int) ((double) mArcRadius * Math.cos(Math.toRadians((double) arcStart)));
        mThumbYPos = (int) ((double) mArcRadius * Math.sin(Math.toRadians((double) arcStart)));
        setTouchInSide(mTouchInside);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mEnabled) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()) {
                case 0:
                    onStartTrackingTouch();
                    updateOnTouch(event);
                    break;
                case 1:
                case 3:
                    onStopTrackingTouch();
                    setPressed(false);
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case 2:
                    updateOnTouch(event);
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mThumb != null && mThumb.isStateful()) {
            int[] state = getDrawableState();
            mThumb.setState(state);
        }

        this.invalidate();
    }

    private void onStartTrackingTouch() {
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onStartTrackingTouch(this);
        }

    }

    private void onStopTrackingTouch() {
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onStopTrackingTouch(this);
        }

    }

    private void updateOnTouch(MotionEvent event) {
        boolean ignoreTouch = this.ignoreTouch(event.getX(), event.getY());
        if (!ignoreTouch) {
            this.setPressed(true);
            mTouchAngle = getTouchDegrees(event.getX(), event.getY());
            int progress = getProgressForAngle(mTouchAngle);
            onProgressRefresh(progress, true);
        }
    }

    private boolean ignoreTouch(float xPos, float yPos) {
        boolean ignore = false;
        float x = xPos - (float) mTranslateX;
        float y = yPos - (float) mTranslateY;
        float touchRadius = (float) Math.sqrt((double) (x * x + y * y));
        if (touchRadius < mTouchIgnoreRadius) {
            ignore = true;
        }

        return ignore;
    }

    private double getTouchDegrees(float xPos, float yPos) {
        float x = xPos - (float) mTranslateX;
        float y = yPos - (float) mTranslateY;
        x = mClockwise ? x : -x;
        double angle = Math.toDegrees(Math.atan2((double) y, (double) x) + 1.5707963267948966D - Math.toRadians((double) this.mRotation));
        if (angle < 0.0D) {
            angle += 360.0D;
        }

        angle -= (double) mStartAngle;
        return angle;
    }

    private int getProgressForAngle(double angle) {
        int touchProgress = (int) Math.round((double) valuePerDegree() * angle);
        touchProgress = touchProgress < 0 ? INVALID_PROGRESS_VALUE : touchProgress;
        touchProgress = touchProgress > mMax ? INVALID_PROGRESS_VALUE : touchProgress;
        return touchProgress;
    }

    private float valuePerDegree() {
        return (float) mMax / (float) mSweepAngle;
    }

    private void onProgressRefresh(int progress, boolean fromUser) {
        updateProgress(progress, fromUser);
    }

    private void updateThumbPosition() {
        int thumbAngle = (int) ((float) mStartAngle + mProgressSweep + (float) mRotation + 90.0F);
        mThumbXPos = (int) ((double) mArcRadius * Math.cos(Math.toRadians((double) thumbAngle)));
        mThumbYPos = (int) ((double) mArcRadius * Math.sin(Math.toRadians((double) thumbAngle)));
    }

    private void updateProgress(int progress, boolean fromUser) {
        if (progress != INVALID_PROGRESS_VALUE) {
            if (mOnSeekArcChangeListener != null) {
                mOnSeekArcChangeListener.onProgressChanged(this, progress, fromUser);
            }

            progress = progress > mMax ? mMax : progress;
            progress = progress < 0 ? 0 : progress;
            mProgress = progress;
            mProgressSweep = (float) progress / (float) mMax * (float) mSweepAngle;
            updateThumbPosition();
            invalidate();
        }
    }

    public void setOnSeekArcChangeListener(SeekArc.OnSeekArcChangeListener l) {
        mOnSeekArcChangeListener = l;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        updateProgress(progress, false);
    }

    public int getProgressWidth() {
        return mProgressWidth;
    }

    public void setProgressWidth(int mProgressWidth) {
        mProgressWidth = mProgressWidth;
        mProgressPaint.setStrokeWidth((float) mProgressWidth);
    }

    public int getArcWidth() {
        return mArcWidth;
    }

    public void setArcWidth(int mArcWidth) {
        mArcWidth = mArcWidth;
        mArcPaint.setStrokeWidth((float) mArcWidth);
    }

    public int getArcRotation() {
        return mRotation;
    }

    public void setArcRotation(int mRotation) {
        mRotation = mRotation;
        updateThumbPosition();
    }

    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int mStartAngle) {
        mStartAngle = mStartAngle;
        updateThumbPosition();
    }

    public int getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(int mSweepAngle) {
        mSweepAngle = mSweepAngle;
        updateThumbPosition();
    }

    public void setRoundedEdges(boolean isEnabled) {
        mRoundedEdges = isEnabled;
        if (mRoundedEdges) {
            mArcPaint.setStrokeCap(Cap.ROUND);
            mProgressPaint.setStrokeCap(Cap.ROUND);
        } else {
            mArcPaint.setStrokeCap(Cap.SQUARE);
            mProgressPaint.setStrokeCap(Cap.SQUARE);
        }

    }

    public void setTouchInSide(boolean isEnabled) {
        int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;
        int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;
        mTouchInside = isEnabled;
        if (mTouchInside) {
            mTouchIgnoreRadius = (float) mArcRadius / 4.0F;
        } else {
            mTouchIgnoreRadius = (float) (mArcRadius - Math.min(thumbHalfWidth, thumbHalfheight));
        }

    }

    public boolean isClockwise() {
        return mClockwise;
    }

    public void setClockwise(boolean isClockwise) {
        mClockwise = isClockwise;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public int getProgressColor() {
        return mProgressPaint.getColor();
    }

    public void setProgressColor(int color) {
        mProgressPaint.setColor(color);
        invalidate();
    }

    public void setmHideThumb(boolean visibility) {
        Resources res = getResources();
        if (visibility) {
            mThumb = res.getDrawable(R.drawable.seek_arc_control_selector);
        } else {
            mThumb = res.getDrawable(R.drawable.seek_arc_control_gone);
        }
        invalidate();
    }

    public int getArcColor() {
        return mArcPaint.getColor();
    }

    public void setArcColor(int color) {
        mArcPaint.setColor(color);
        invalidate();
    }

    public interface OnSeekArcChangeListener {
        void onProgressChanged(SeekArc var1, int var2, boolean var3);

        void onStartTrackingTouch(SeekArc var1);

        void onStopTrackingTouch(SeekArc var1);
    }
}
