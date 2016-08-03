package net.yet.ui.util;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class LayoutParamUtil extends LayoutParamTemplate<ViewGroup.LayoutParams, LayoutParamUtil> {

	public LayoutParamUtil() {
		p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public LayoutParamUtil(ViewGroup.LayoutParams p) {
		this.p = p;
	}

}
