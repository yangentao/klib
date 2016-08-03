package net.yet.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import net.yet.theme.Colors;
import net.yet.util.TaskUtil;
import net.yet.util.app.App;

/**
 * Created by yet on 2015/10/31.
 */
public class MyProgressBarIndeterminate extends View {
	private static final int INTERVAL_INDETERM = 30;//动画, 20毫秒刷新一次, 无限
	private final int MIN_WIDTH = App.dp2px(30);//
	private int durationIndeterm = 5000;//
	private Drawable fore;
	private int indeterTime = 0;

	private Runnable indetermRun = new Runnable() {
		@Override
		public void run() {
			if (isVisible()) {
				if (indeterTime < 0) {
					--indeterTime;
				} else {
					++indeterTime;
				}
				postInvalidate();
				TaskUtil.foreDelay(INTERVAL_INDETERM, indetermRun);
			}
		}
	};

	public MyProgressBarIndeterminate(Context context) {
		super(context);
		setBackDrawable(makeDrawable(0, 0.5f, Color.parseColor("#999999"), Color.parseColor("#555555"), Color.parseColor("#777777")));
		setForeDrawable(makeDrawable(0, 0.5f, Colors.Safe, Color.rgb(0, 255, 0), Colors.Safe));
	}

	public static GradientDrawable makeDrawable(int cornerRadius, int color) {
		GradientDrawable gd = new GradientDrawable();
		gd.setCornerRadius(cornerRadius);
		gd.setColor(color);
		return gd;
	}

	public static GradientDrawable makeDrawable(int cornerRadius, float centerY, int startColor, int centerColor, int endColor) {
		GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{startColor, centerColor, endColor});
		gd.setGradientCenter(0, centerY);
		gd.setShape(GradientDrawable.RECTANGLE);
		gd.setCornerRadius(cornerRadius);
		return gd;
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		startBlockAnim();
	}

	private void startBlockAnim() {
		if (isVisible()) {
			TaskUtil.fore(indetermRun);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isVisible()) {
			int width = getWidth();
			int w = width / 5;
			if (w < MIN_WIDTH) {
				w = width / 2;
			}
			int left = 0;
			int times = durationIndeterm / INTERVAL_INDETERM;
			if (times < 1) {
				times = 1;
			}
			int inc = width / times;
			if (inc < 1) {
				inc = 1;
			}
			if (indeterTime >= 0) {
				left = indeterTime * inc;
				if (left + w > width) {
					left = width - w;
					indeterTime = -1;
				}
			} else {
				left = width - w + indeterTime * inc;
				if (left < 0) {
					left = 0;
					indeterTime = 0;
				}
			}
			fore.setBounds(left, 0, left + w, getHeight());
			fore.draw(canvas);
		}

	}

	public MyProgressBarIndeterminate setBackDrawable(Drawable drawable) {
		setBackgroundDrawable(drawable);
		return this;
	}

	public MyProgressBarIndeterminate setForeDrawable(Drawable drawable) {
		fore = drawable;
		postInvalidate();
		return this;
	}

	public MyProgressBarIndeterminate show() {
		setVisibility(View.VISIBLE);
		return this;
	}

	public void hide() {
		setVisibility(View.GONE);
	}

	public boolean isVisible() {
		return getVisibility() == View.VISIBLE;
	}
}
