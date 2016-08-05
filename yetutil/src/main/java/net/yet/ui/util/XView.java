package net.yet.ui.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

// view 扩展
public class XView {

	public static <T extends View> T id(T view) {
		view.setId(genViewId());
		return view;
	}

	public static CheckBox createCheckbox(Context context) {
		return id(new CheckBox(context));
	}


	public static TextView createTextView(Context context) {
		return createTextViewB(context);
	}

	public static TextView createTextViewA(Context context) {
		TextView tv = XView.id(new TextView(context));
		XView.view(tv).gravityLeftCenter().textSizeA();
		return tv;
	}

	public static TextView createTextViewB(Context context) {
		TextView tv = XView.id(new TextView(context));
		XView.view(tv).gravityLeftCenter().textSizeB().textColorMajor();
		return tv;
	}

	public static TextView createTextViewC(Context context) {
		TextView tv = XView.id(new TextView(context));
		XView.view(tv).gravityLeftCenter().textSizeC().textColorMinor();
		return tv;
	}


	public static ImageView createImageView(Context context) {
		ImageView b = XView.id(new ImageView(context));
		b.setAdjustViewBounds(true);
		b.setScaleType(ScaleType.CENTER_INSIDE);
		return b;
	}

	// util method for linearlayout body view
	public static LinearLayout createLinearVertical(Context context) {
		LinearLayout ll = XView.id(new LinearLayout(context));
		XView.view(ll).orientationVertical();
		return ll;
	}

	public static LinearLayout createLinearHorizontal(Context context) {
		LinearLayout ll = XView.id(new LinearLayout(context));
		XView.view(ll).orientationHorizontal();
		return ll;
	}


	public static LayoutParamUtil param() {
		return new LayoutParamUtil();
	}

	public static LayoutParamUtil param(ViewGroup.LayoutParams p) {
		return new LayoutParamUtil(p);
	}

	public static LinearParamUtil linearParam() {
		return new LinearParamUtil();
	}

	public static LinearParamUtil linearParam(LinearLayout.LayoutParams p) {
		return new LinearParamUtil(p);
	}

	public static RelativeParamUtil relativeParam() {
		return new RelativeParamUtil();
	}



	public static ViewWrap view(View v) {
		return new ViewWrap(v);
	}

	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	public static int genViewId() {
		for (; ; ) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				// xlog.d("generate ID:", Integer.toHexString(result));
				return result;
			}
		}
	}
}
