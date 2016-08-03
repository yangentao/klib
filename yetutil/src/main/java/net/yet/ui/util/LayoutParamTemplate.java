package net.yet.ui.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import net.yet.util.app.App;

@SuppressWarnings("unchecked")
public class LayoutParamTemplate<P extends ViewGroup.LayoutParams, L extends LayoutParamTemplate<P, L>> {
	protected P p;

	public L fill() {
		p.width = P.MATCH_PARENT;
		p.height = P.MATCH_PARENT;
		return (L) this;
	}

	public P get() {
		return p;
	}

	public L height(int dp) {
		p.height = App.dp2px(dp);
		return (L) this;
	}


	public L heightFill() {
		p.height = LayoutParams.MATCH_PARENT;
		return (L) this;
	}


	public L heightWrap() {
		p.height = LayoutParams.WRAP_CONTENT;
		return (L) this;
	}


	public void set(View view) {
		view.setLayoutParams(p);
	}

	public L size(int whDp) {
		width(whDp);
		return height(whDp);
	}

	public L size(int wdp, int hdp) {
		width(wdp);
		return height(hdp);
	}

	public L widthFill() {
		p.width = LayoutParams.MATCH_PARENT;
		return (L) this;
	}


	public L width(int dp) {
		p.width = App.dp2px(dp);
		return (L) this;
	}

	public L wrap() {
		widthWrap();
		return heightWrap();
	}

	public L widthWrap() {
		p.width = LayoutParams.WRAP_CONTENT;
		return (L) this;
	}

}
