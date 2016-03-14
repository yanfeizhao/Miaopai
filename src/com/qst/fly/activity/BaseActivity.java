package com.qst.fly.activity;



import com.qst.fly.R;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	protected static final String TAG = "BaseActivity";

	protected void showToast(String msg) {
		Toast toast = new Toast(this);
		// FIXME 有了切图之后自行把TextView换成素材，可能要做成.9图
		View view = LayoutInflater.from(this).inflate(R.layout.toast_img, null);
		TextView tvMsg = (TextView) view.findViewById(R.id.toast_img_tv_msg);
		tvMsg.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		final Window window = getWindow();
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
		valueAnimator.setDuration(2500);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float input = (Float) animation.getAnimatedValue();
				Log.v(TAG, "input:" + input);
				float d = 0.1f;
				float alpha = 0.5f;
				if (input < d) {
					alpha = 1 - input * (0.5f / d);
				} else if (input > (1-d)) {
					alpha = 0.5f + (input - (1-d)) * (0.5f / d);
				} else {
					alpha = 0.5f;
				}
				WindowManager.LayoutParams params = window.getAttributes();
				params.alpha = alpha;
				Log.e(TAG, "亮度：" + params.alpha);
				window.setAttributes(params);
			}
		});

		valueAnimator.start();
		toast.show();
	}

}
