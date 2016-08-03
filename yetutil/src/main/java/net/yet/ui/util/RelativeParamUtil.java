package net.yet.ui.util;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import net.yet.util.app.App;

public class RelativeParamUtil extends LayoutParamTemplate<RelativeLayout.LayoutParams, RelativeParamUtil> {

	public RelativeParamUtil() {
		p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public RelativeParamUtil(LayoutParams p) {
		this.p = p;
	}

	public RelativeParamUtil margins(int m) {
		return margins(m, m, m, m);
	}

	public RelativeParamUtil margins(int left, int top, int right, int bottom) {
		p.setMargins(App.dp2px(left), App.dp2px(top), App.dp2px(right), App.dp2px(bottom));
		return this;
	}

	public RelativeParamUtil marginsPx(int m) {
		return marginsPx(m, m, m, m);
	}

	public RelativeParamUtil marginsPx(int left, int top, int right, int bottom) {
		p.setMargins(left, top, right, bottom);
		return this;
	}

	// --------------
	public RelativeParamUtil parentBottom() {
		p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		return this;
	}

	// --------------
	public RelativeParamUtil parentLeft() {
		p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		return this;
	}

	public RelativeParamUtil parentRight() {
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		return this;
	}

	public RelativeParamUtil parentTop() {
		p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		return this;
	}

	public RelativeParamUtil centerInParent() {
		p.addRule(RelativeLayout.CENTER_IN_PARENT);
		return this;
	}

	public RelativeParamUtil centerHorizontal() {
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		return this;
	}

	public RelativeParamUtil centerVertical() {
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		return this;
	}

	public RelativeParamUtil above(int anchor) {
		p.addRule(RelativeLayout.ABOVE, anchor);
		return this;
	}

	public RelativeParamUtil above(View anchor) {
		p.addRule(RelativeLayout.ABOVE, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil alignBaseline(int anchor) {
		p.addRule(RelativeLayout.ALIGN_BASELINE, anchor);
		return this;
	}

	public RelativeParamUtil alignBaseline(View anchor) {
		p.addRule(RelativeLayout.ALIGN_BASELINE, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil alignBottom(int anchor) {
		p.addRule(RelativeLayout.ALIGN_BOTTOM, anchor);
		return this;
	}

	public RelativeParamUtil alignBottom(View anchor) {
		p.addRule(RelativeLayout.ALIGN_BOTTOM, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil alignLeft(int anchor) {
		p.addRule(RelativeLayout.ALIGN_LEFT, anchor);
		return this;
	}

	public RelativeParamUtil alignLeft(View anchor) {
		p.addRule(RelativeLayout.ALIGN_LEFT, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil alignRight(int anchor) {
		p.addRule(RelativeLayout.ALIGN_RIGHT, anchor);
		return this;
	}

	public RelativeParamUtil alignRight(View anchor) {
		p.addRule(RelativeLayout.ALIGN_RIGHT, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil alignTop(int anchor) {
		p.addRule(RelativeLayout.ALIGN_TOP, anchor);
		return this;
	}

	public RelativeParamUtil alignTop(View anchor) {
		p.addRule(RelativeLayout.ALIGN_TOP, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil below(int anchor) {
		p.addRule(RelativeLayout.BELOW, anchor);
		return this;
	}

	public RelativeParamUtil below(View anchor) {
		p.addRule(RelativeLayout.BELOW, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil toLeftOf(int anchor) {
		p.addRule(RelativeLayout.LEFT_OF, anchor);
		return this;
	}

	public RelativeParamUtil toLeftOf(View anchor) {
		p.addRule(RelativeLayout.LEFT_OF, anchor.getId());
		return this;
	}

	// --------------
	public RelativeParamUtil toRightOf(int anchor) {
		p.addRule(RelativeLayout.RIGHT_OF, anchor);
		return this;
	}

	public RelativeParamUtil toRightOf(View anchor) {
		p.addRule(RelativeLayout.RIGHT_OF, anchor.getId());
		return this;
	}

}
