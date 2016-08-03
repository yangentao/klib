package net.yet.ui.util;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.widget.LinearLayout;

import net.yet.util.app.App;

@SuppressLint("RtlHardcoded")
public class LinearParamUtil extends LayoutParamTemplate<LinearLayout.LayoutParams, LinearParamUtil> {

	public LinearParamUtil() {
		p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	public LinearParamUtil(LinearLayout.LayoutParams p) {
		this.p = p;
	}

	public LinearParamUtil weight(float w) {
		p.weight = w;
		return this;
	}

	public LinearParamUtil margins(int m) {
		return margins(m, m, m, m);
	}

	public LinearParamUtil margins(int left, int top, int right, int bottom) {
		p.setMargins(App.dp2px(left), App.dp2px(top), App.dp2px(right), App.dp2px(bottom));
		return this;
	}

	public LinearParamUtil marginsPx(int m) {
		p.setMargins(m, m, m, m);
		return this;
	}

	public LinearParamUtil marginsPx(int left, int top, int right, int bottom) {
		p.setMargins(left, top, right, bottom);
		return this;
	}

	public LinearParamUtil gravityNone() {
		p.gravity = Gravity.NO_GRAVITY;
		return this;
	}

	public LinearParamUtil gravityTop() {
		p.gravity = Gravity.TOP;
		return this;
	}

	public LinearParamUtil gravityTopCenter() {
		p.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		return this;
	}

	public LinearParamUtil gravityLeft() {
		p.gravity = Gravity.LEFT;
		return this;
	}

	public LinearParamUtil gravityLeftCenter() {
		p.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		return this;
	}

	public LinearParamUtil gravityRight() {
		p.gravity = Gravity.RIGHT;
		return this;
	}

	public LinearParamUtil gravityRightCenter() {
		p.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		return this;
	}

	public LinearParamUtil gravityBottom() {
		p.gravity = Gravity.BOTTOM;
		return this;
	}

	public LinearParamUtil gravityBottomCenter() {
		p.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		return this;
	}

	public LinearParamUtil gravityFill() {
		p.gravity = Gravity.FILL;
		return this;
	}

	public LinearParamUtil gravityFillVertical() {
		p.gravity = Gravity.FILL_VERTICAL;
		return this;
	}

	public LinearParamUtil gravityFillHorizontal() {
		p.gravity = Gravity.FILL_HORIZONTAL;
		return this;
	}

	public LinearParamUtil gravityCenterVertical() {
		p.gravity = Gravity.CENTER_VERTICAL;
		return this;
	}

	public LinearParamUtil gravityCenterHorizontal() {
		p.gravity = Gravity.CENTER_HORIZONTAL;
		return this;
	}

	public LinearParamUtil gravityCenter() {
		p.gravity = Gravity.CENTER;
		return this;
	}

}
