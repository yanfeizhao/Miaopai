package com.qst.fly.widget;

import com.qst.fly.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


public class GifView extends View {

	/**
	 * 默认为1秒
	 */
	private static final int DEFAULT_MOVIE_DURATION = 1000;

	private int mMovieResourceId;

	private Movie mMovie;

	private long mMovieStart;

	private int mCurrentAnimationTime = 0;

	private float mLeft;

	private float mTop;

	private float mScale;

	private int mMeasuredMovieWidth;

	private int mMeasuredMovieHeight;

	private boolean mVisible = true;

	private volatile boolean mPaused = false;

	public GifView(Context context) {
		this(context, null);
	}

	public GifView(Context context, AttributeSet attrs) {
		this(context, attrs, R.styleable.CustomTheme_gifViewStyle);
	}

	public GifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setViewAttributes(context, attrs, defStyle);
	}

	@SuppressLint("NewApi")
	private void setViewAttributes(Context context, AttributeSet attrs,
			int defStyle) {
		// 从描述文件中读出gif的值，创建出Movie实例
		final TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.GifView, defStyle, 0);
		mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
		mPaused = array.getBoolean(R.styleable.GifView_paused, true);
		array.recycle();
	}

	/**
	 * 设置gif图资源
	 * 
	 * @param movieResId
	 */
	public void setMovieResource(int movieResId) {
		this.mMovieResourceId = movieResId;
		mMovie = Movie.decodeStream(getResources().openRawResource(
				mMovieResourceId));
		requestLayout();
	}

	public void setMovie(Movie movie) {
		this.mMovie = movie;
		requestLayout();
	}

	public Movie getMovie() {
		return mMovie;
	}

	/**
	 * 时间
	 * 
	 * @param time
	 */
	public void setMovieTime(int time) {
		mCurrentAnimationTime = time;
		invalidate();
	}

	/**
	 * 测宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mMovie != null) {
			int movieWidth = mMovie.width();
			int movieHeight = mMovie.height();
			int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
			float scaleW = (float) movieWidth / (float) maximumWidth;
			mScale = 1f / scaleW;
			mMeasuredMovieWidth = maximumWidth;
			mMeasuredMovieHeight = (int) (movieHeight * mScale);
			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
		} else {
			setMeasuredDimension(getSuggestedMinimumWidth(),
					getSuggestedMinimumHeight());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie != null) {
			if (mPaused) {
				updateAnimationTime(canvas);
				invalidateView();
			}
		}
	}

	@SuppressLint("NewApi")
	private void invalidateView() {
		if (mVisible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
		}
	}

	private boolean updateAnimationTime(Canvas canvas) {
		long now = android.os.SystemClock.uptimeMillis();
		// 如果第一帧，记录起始时间
		if (mMovieStart == 0) {
			if (mOnGifStartListener!=null)
				mOnGifStartListener.onStart();
			mMovieStart = now;
		}
		// 取出动画的时长
		int dur = mMovie.duration();
		if (dur == 0) {
			dur = DEFAULT_MOVIE_DURATION;
		}
		// 算出需要显示第几帧
		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
		mMovie.setTime(mCurrentAnimationTime);
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
		canvas.restore();
		if ((now - mMovieStart) >= dur) {
			mPaused = false;
			mMovieStart = 0;
			if (mOnGifFinishListener != null)
				mOnGifFinishListener.onFinish();
			return true;
		}
		return false;
	}

	private OnGifStartListener mOnGifStartListener;

	public void setOnGifStartListener(OnGifStartListener onGifStartListener) {
		this.mOnGifStartListener = onGifStartListener;
	}

	public interface OnGifStartListener {
		void onStart();
	}

	private OnGifFinishListener mOnGifFinishListener;

	public void setOnGifFinishListener(OnGifFinishListener onGifFinishListener) {
		this.mOnGifFinishListener = onGifFinishListener;
	}

	public interface OnGifFinishListener {
		void onFinish();
	}
}