package com.zhz_zsl.glutweather.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhz_zsl.glutweather.R;


/**
 * 自定义对话框
 */
public class AAlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	public AAlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AAlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	/**
	 * 设置对话框标题
	 */
	public AAlertDialog setTitle(String title) {
		showTitle = true;
		//如无指定标题则默认为“标题”
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	/**
	 * 设置显示内容
	 */
	public AAlertDialog setMsg(String msg) {
		showMsg = true;
		//如无指定内容则默认为空
		if ("".equals(msg)) {
			txt_msg.setText(" ");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}


	/**
	 * 设置点击对话框以外位置时是否关闭对话框
	 */
	public AAlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	/**
	 * 设置对话框底部左侧BUTTON的显示内容
	 */
	public AAlertDialog setPositiveButton(String text, final OnClickListener listener) {
		showPosBtn = true;
		//如无指定，默认为“确定”
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		//设置点击事件
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				//关闭对话框
				dialog.dismiss();
			}
		});
		return this;
	}

	/**
	 * 设置对话框底部右侧BUTTON的显示内容
	 */
	public AAlertDialog setNegativeButton(String text, final OnClickListener listener) {
		showNegBtn = true;
		//如无指定，默认为“取消”
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		//设置点击事件
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				//关闭对话框
				dialog.dismiss();
			}
		});
		return this;
	}

	/**
	 * 设置对话框布局
	 */
	private void setLayout() {
		//如对话框中标题与内容均未指定
		if (!showTitle && !showMsg) {
			txt_title.setText("提示");
			txt_title.setVisibility(View.VISIBLE);
		}
		//对话框标题可见
		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}
		//对话框内容可见
		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}
		//如对话框底部两个BUTOON均未指定
		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		//如对话框底部两个BUTOON均已指定
		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			//设置BUTTON背景
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			//设置BUTTON背景
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}
		//如只指定对话框顶部左侧BUTOON
		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
		//如只指定对话框顶部右侧BUTOON
		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
