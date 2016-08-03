package net.yet.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.yet.ui.res.Img;
import net.yet.ui.util.XView;

/**
 * 左右对齐listview itemview
 * addLeftXXX总是从0插入子View
 * addRightXXX总是从最后插入子View
 * 
 * @author yangentao@gmail.com
 *
 */
public class LeftRightItemView extends LinearLayout {
	public static final int ITEM_HEIGHT = 45;

	public LeftRightItemView(Context context, int marginBottom) {
		super(context);
		XView.view(this).orientationHorizontal().gravityCenterVertical().backColorWhiteFade().padding(10, 5, 10, 5);
		XView.linearParam().widthFill().height(ITEM_HEIGHT).margins(0, 0, 0, marginBottom).set(this);

		View v = XView.id(new View(getContext()));
		addView(v, XView.linearParam().weight(1).heightFill().get());
	}

	public CheckBox findCheckBox() {
		for (int i = 0; i < getChildCount(); ++i) {
			View v = getChildAt(i);
			if (v instanceof CheckBox) {
				return (CheckBox) v;
			}
		}
		return null;
	}

	private TextView addText(String text, int width, boolean right, int marginLeft) {
		if (right) {
			TextView tv = XView.createTextViewB(getContext());
			XView.view(tv).textColorMinor().gravityRightCenter();
			tv.setText(text);
			XView.linearParam().heightWrap().width(width).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(tv);
			this.addView(tv);
			return tv;
		} else {
			TextView tv = XView.createTextViewA(getContext());
			tv.setText(text);
			XView.linearParam().heightWrap().width(width).gravityLeftCenter().margins(marginLeft, 0, 0, 0).set(tv);
			this.addView(tv, 0);
			return tv;
		}
	}

	public TextView addTextLeft(String text, int width, int marginLeft) {
		return addText(text, width, false, marginLeft);
	}

	public TextView addTextRight(String text, int width, int marginLeft) {
		return addText(text, width, true, marginLeft);
	}

	private ImageView addImage(Drawable d, int sizeDp, boolean right, int marginLeft) {
		ImageView iv = XView.createImageView(getContext());
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageDrawable(d);
		XView.view(iv).backColorTransFade();
		if (right) {
			XView.linearParam().size(sizeDp).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(iv);
			this.addView(iv);
		} else {
			XView.linearParam().size(sizeDp).gravityLeftCenter().margins(marginLeft, 0, 0, 0).set(iv);
			this.addView(iv, 0);
		}
		return iv;
	}

	public ImageView addImageLeft(Drawable d, int sizeDp, int marginLeft) {
		return addImage(d, sizeDp, false, marginLeft);
	}

	public ImageView addImageRight(Drawable d, int sizeDp, int marginLeft) {
		return addImage(d, sizeDp, true, marginLeft);
	}

	public CheckBox addCheckBoxRight(int marginLeft) {
		CheckBox cb = XView.createCheckbox(getContext());
		Drawable d = Img.INSTANCE.namedStatesSize("checkbox", true, 15);
		cb.setButtonDrawable(d);
		XView.linearParam().size(20).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(cb);
		this.addView(cb);
		return cb;
	}

}
