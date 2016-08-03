package net.yet.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import net.yet.theme.Colors;
import net.yet.util.TaskUtil;

/**
 * Created by yet on 2015/10/31.
 */
public class XProgressBar extends View {
	private static final int INTERVAL_A = 20;//动画, 20毫秒刷新一次 , 有限
	private int duration = 200;//动画时间 200毫秒---从0到最大值(100%)的时间
	private Drawable fore;
	private int max = 100;
	private int progress = 0;
	private int animProgress = 0;

	public XProgressBar(Context context) {
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
	protected void onDraw(Canvas canvas) {
		int prog = progress;
		if (animProgress >= 0) {
			prog = animProgress;
		}
		int width = getWidth();
		max = max == 0 ? 1 : max;
		int w = prog * width / max;
		w = w < 0 ? -w : w;
		fore.setBounds(0, 0, w, getHeight());
		fore.draw(canvas);

	}

	public XProgressBar setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public XProgressBar setBackDrawable(Drawable drawable) {
		setBackgroundDrawable(drawable);
		return this;
	}

	public XProgressBar setForeDrawable(Drawable drawable) {
		fore = drawable;
		postInvalidate();
		return this;
	}

	public XProgressBar setProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		}
		if (progress > max) {
			progress = max;
		}
		this.animProgress = this.progress;
		this.progress = progress;
		TaskUtil.fore(run);
		return this;
	}

	public void postProgress(final int progress) {
		TaskUtil.fore(new Runnable() {
			@Override
			public void run() {
				setProgress(progress);
			}
		});
	}

	public XProgressBar setMax(int max) {
		if (max < 0) {
			max = 1;
		}
		this.max = max;
		postInvalidate();
		return this;
	}

	public void postMax(final int max) {
		TaskUtil.fore(new Runnable() {
			@Override
			public void run() {
				setMax(max);
			}
		});
	}

	public XProgressBar show() {
		setVisibility(View.VISIBLE);
		return this;
	}

	public XProgressBar show(int max) {
		setVisibility(View.VISIBLE);
		this.progress = 0;
		this.animProgress = 0;
		setMax(max);
		return this;
	}

	public void postShow(final int max) {
		TaskUtil.fore(new Runnable() {
			@Override
			public void run() {
				show(max);
			}
		});
	}

	public void postHide() {
		TaskUtil.fore(new Runnable() {
			@Override
			public void run() {
				hide();
			}
		});
	}

	public void hide() {
		setVisibility(View.GONE);
	}

	public boolean isVisible() {
		return getVisibility() == View.VISIBLE;
	}

	private Runnable run = new Runnable() {
		@Override
		public void run() {//每次递进1%, 或1
			int times = duration / INTERVAL_A;
			if (times < 1) {
				animProgress = progress;
				postInvalidate();
			} else {
				int percent = max / times;
				if (percent == 0) {
					percent = 2;
				}
				int cha = progress - animProgress;
				if (Math.abs(cha) <= percent) {
					animProgress = progress;
					postInvalidate();
				} else {
					if (cha > 0) {
						animProgress += percent;
					} else {
						animProgress -= percent;
					}
					postInvalidate();
					TaskUtil.foreDelay(INTERVAL_A, run);
				}
			}
		}
	};
}
