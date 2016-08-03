package net.yet.ui.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import net.yet.util.Util;
import net.yet.util.app.App;

/**
 * TODO 阴影
 */
public class ShapeUtil {
	public static Drawable line(int strokeWidth, int strokeColor) {
		return linePx(App.dp2px(strokeWidth), strokeColor);
	}

	public static Drawable linePx(int strokeWidth, int strokeColor) {
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.LINE);
		gd.setStroke(strokeWidth, strokeColor);
		return gd;
	}

	public static Drawable round(int corner, int fillColor) {
		return round(corner, fillColor, null, null);
	}

	public static Drawable round(Integer corner, Integer fillColor, Integer strokeWidth, Integer strokeColor) {
		return roundPx(corner == null ? null : App.dp2px(corner), fillColor,
				strokeWidth == null ? null : App.dp2px(strokeWidth), strokeColor);
	}

	public static Drawable round(Integer corner, String fillColor, Integer strokeWidth, String strokeColor) {
		Integer fC = fillColor == null ? null : Util.argb(fillColor);
		Integer sC = strokeColor == null ? null : Util.argb(strokeColor);
		return roundPx(corner == null ? null : App.dp2px(corner), fC,
				strokeWidth == null ? null : App.dp2px(strokeWidth), sC);
	}

	public static Drawable roundPx(Integer corner, String fillColor, Integer strokeWidth, String strokeColor) {
		Integer fC = fillColor == null ? null : Util.argb(fillColor);
		Integer sC = strokeColor == null ? null : Util.argb(strokeColor);
		return roundPx(corner == null ? null : corner, fC, strokeWidth == null ? null : strokeWidth, sC);
	}

	public static Drawable roundPx(int corner, int fillColor) {
		return roundPx(corner, fillColor, null, null);
	}

	public static Drawable roundPx(Integer corner, Integer fillColor, Integer strokeWidth, Integer strokeColor) {
		GradientDrawable gd = new GradientDrawable();
		if (corner != null) {
			gd.setCornerRadius(corner);
		}
		if (fillColor != null) {
			gd.setColor(fillColor);
		}
		if (strokeWidth != null && strokeColor != null) {
			gd.setStroke(strokeWidth, strokeColor);
		}
		return gd;
	}

	public static Drawable rect(Integer fillColor, Integer strokeWidth, Integer strokeColor) {
		return rectPx(fillColor, strokeWidth == null ? null : App.dp2px(strokeWidth), strokeColor);
	}

	public static Drawable rectPx(Integer fillColor, Integer strokeWidth, Integer strokeColor) {
		GradientDrawable gd = new GradientDrawable();
		if (fillColor != null) {
			gd.setColor(fillColor);
		}
		if (strokeWidth != null && strokeColor != null) {
			gd.setStroke(strokeWidth, strokeColor);
		}
		return gd;
	}

	public static Drawable oval(int width, int color) {
		width = App.dp2px(width);
		Drawable d = ovalPx(width, color);
		d.setBounds(0, 0, width, width);//
		return d;
	}

	private static Drawable ovalPx(int width, int color) {
		return ovalPx(width, width, color);
	}

	private static Drawable ovalPx(int width, int height, int color) {
		return ovalPx(width, height, color, null, null);
	}

	public static Drawable ovalPx(int width, int height, int color, Integer strokeWidth, Integer strokeColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(color);
		drawable.setShape(GradientDrawable.OVAL);
		if (strokeWidth != null) {
			drawable.setStroke(strokeWidth, strokeColor);
		}
		drawable.setSize(width, height);
		return drawable;
	}

	public static Drawable ovalDP(int width, int height, int color, Integer strokeWidth, Integer strokeColor) {
		width = App.dp2px(width);
		height = App.dp2px(height);
		if (strokeWidth != null) {
			strokeWidth = App.dp2px(strokeWidth);
		}
		return ovalPx(width, height, color, strokeWidth, strokeColor);
	}
}
