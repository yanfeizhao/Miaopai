	package com.qst.fly.widget;

import com.qst.fly.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class NewHintDialog extends Dialog implements OnClickListener{

	
	private Button mButtonYes;
	private Button mButtonNo;
	
	public NewHintDialog(Context context, int themeResId) {
		super(context, themeResId);
		initView();
	}



	protected NewHintDialog(Context context) {
		super(context);
		initView();
	}
	

	private void initView() {
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.dialog_new, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消dialog的标题栏
		

//		View v = findViewById(R.id.linerlayout);//找到你要设透明背景的layout 的id 
		view.getBackground().setAlpha(100);//0~255透明度值 
		
		setContentView(view);

		mButtonYes=(Button) findViewById(R.id.btn_yes);
		mButtonNo=(Button) findViewById(R.id.btn_no);

		mButtonYes.setOnClickListener(this);
		mButtonNo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			 mDialogClickListener.clickYes();
			dismiss();
			break;
		case R.id.btn_no:
			 mDialogClickListener.clickNo();
			dismiss();
			break;
		default:
			break;
		}
	}
	
	public interface onDialogClickListener {
		void clickYes();
		void clickNo();
	}

	private onDialogClickListener mDialogClickListener;

	public void setDialogClickListener(onDialogClickListener DialogClickListener) {
		mDialogClickListener = DialogClickListener;
	}
	

}
