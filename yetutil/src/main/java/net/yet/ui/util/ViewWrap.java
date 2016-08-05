package net.yet.ui.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.yet.theme.Colors;
import net.yet.theme.Dim;
import net.yet.ui.res.Img;
import net.yet.util.app.App;

// 没有特定标注的单位, 一般是dp, 字体是sp
public class ViewWrap {
	public  View v;

	public ViewWrap() {
	}

	public ViewWrap(View view) {
		v = view;
	}

	public TextView asText() {
		return (TextView) v;
	}

	public ImageView asImage() {
		return (ImageView) v;
	}

	public LinearLayout asLinear() {
		return (LinearLayout) v;
	}

	public ViewWrap miniWidthTV(int widthDp) {
		asText().setMinWidth(App.dp2px(widthDp));
		return this;
	}

	public ViewWrap miniHeightTV(int heightDp) {
		asText().setMinHeight(App.dp2px(heightDp));
		return this;
	}

	public ViewWrap tag(Object obj) {
		v.setTag(obj);
		return this;
	}

	public ViewWrap inputTypePassword() {
		asText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		return this;
	}

	public ViewWrap inputTypePhone() {
		asText().setInputType(InputType.TYPE_CLASS_PHONE);
		return this;
	}

	public ViewWrap inputTypeEmail() {
		asText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		return this;
	}

	public ViewWrap clickable() {
		v.setClickable(true);
		return this;
	}

	public ViewWrap ellipsizeStart() {
		asText().setEllipsize(TruncateAt.START);
		return this;
	}

	public ViewWrap ellipsizeMid() {
		asText().setEllipsize(TruncateAt.MIDDLE);
		return this;
	}

	public ViewWrap ellipsizeEnd() {
		asText().setEllipsize(TruncateAt.END);
		return this;
	}

	public ViewWrap ellipsizeMarquee() {
		asText().setEllipsize(TruncateAt.MARQUEE);
		return this;
	}

	public ViewWrap singleLine() {
		asText().setLines(1);
		return this;
	}

	public ViewWrap lines(int lines) {
		asText().setLines(lines);
		return this;
	}

	public ViewWrap maxLines(int maxLines) {
		asText().setMaxLines(maxLines);
		return this;
	}

	public ViewWrap textSizeTitle() {
		return textSize(Dim.textSizeTitle);
	}

	public ViewWrap textSizeNormal() {
		return textSize(Dim.textSize);
	}

	public ViewWrap textSizeA() {
		return textSize(Dim.textSizeA);
	}

	public ViewWrap textSizeB() {
		return textSize(Dim.textSizeB);
	}

	public ViewWrap textSizeC() {
		return textSize(Dim.textSizeC);
	}

	public ViewWrap textSizeD() {
		return textSize(Dim.textSizeD);
	}

	public ViewWrap orientationVertical() {
		asLinear().setOrientation(LinearLayout.VERTICAL);
		return this;
	}

	public ViewWrap orientationHorizontal() {
		asLinear().setOrientation(LinearLayout.HORIZONTAL);
		return this;
	}

	public ViewWrap image(Drawable d) {
		asImage().setImageDrawable(d);
		return this;
	}

	public ViewWrap image(String name) {
		asImage().setImageDrawable(Img.INSTANCE.namedStates(name, true));
		return this;
	}

	public ViewWrap image(String name, boolean state) {
		asImage().setImageDrawable(Img.INSTANCE.namedStates(name, state));
		return this;
	}

	public ViewWrap image(String name, boolean withState, int size) {
		Drawable d = Img.INSTANCE.namedStates(name, withState);
		d.setBounds(0, 0, App.dp2px(size), App.dp2px(size));
		asImage().setImageDrawable(d);
		return this;
	}

	public ViewWrap gravity(int gravity) {
		View view = v;
		if (view instanceof TextView) {
			TextView textView = (TextView) view;
			textView.setGravity(gravity);
		} else if (view instanceof LinearLayout) {
			LinearLayout ll = (LinearLayout) view;
			ll.setGravity(gravity);
		} else {
			throw new IllegalArgumentException("不识别的Gravity");
		}
		return this;
	}

	public ViewWrap gravityCenter() {
		gravity(Gravity.CENTER);
		return this;
	}

	public ViewWrap gravityCenterVertical() {
		gravity(Gravity.CENTER_VERTICAL);
		return this;
	}

	public ViewWrap gravityCenterHorizontal() {
		gravity(Gravity.CENTER_HORIZONTAL);
		return this;
	}

	public ViewWrap gravityLeft() {
		gravity(Gravity.LEFT);
		return this;
	}

	// left and vertical-center
	public ViewWrap gravityLeftCenter() {
		gravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		return this;
	}

	public ViewWrap gravityRight() {
		gravity(Gravity.RIGHT);
		return this;
	}

	// right and center-vertical
	public ViewWrap gravityRightCenter() {
		gravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		return this;
	}

	public ViewWrap gravityTop() {
		gravity(Gravity.TOP);
		return this;
	}

	// top and center-horizontal
	public ViewWrap gravityTopCenter() {
		gravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		return this;
	}

	public ViewWrap gravityBottom() {
		gravity(Gravity.BOTTOM);
		return this;
	}

	// bottom and center-horizontal
	public ViewWrap gravityBottomCenter() {
		gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		return this;
	}

	public ViewWrap text(String text) {
		asText().setText(text);
		return this;
	}

	public ViewWrap lineSpace(float add, float multi) {
		asText().setLineSpacing(add, multi);
		return this;
	}

	public ViewWrap hint(String text) {
		asText().setHint(text);
		return this;
	}

	public ViewWrap textColor(ColorStateList ls) {
		asText().setTextColor(ls);
		return this;
	}

	public ViewWrap textColor(int normal, int pressed) {
		asText().setTextColor(Img.INSTANCE.colorList(normal, pressed));
		return this;
	}

	public ViewWrap textColorMajorFade() {
		asText().setTextColor(Img.INSTANCE.colorList(Colors.TextColor, Colors.Fade));
		return this;
	}

	public ViewWrap textColor(int color) {
		asText().setTextColor(color);
		return this;
	}

	public ViewWrap textColorWhite() {
		return textColor(Color.WHITE);
	}

	public ViewWrap textColorMajor() {
		return textColor(Colors.TextColorMajor);
	}

	public ViewWrap textColorMid() {
		return textColor(Colors.TextColorMid);
	}

	public ViewWrap textColorMinor() {
		return textColor(Colors.TextColorMinor);
	}

	public ViewWrap textSize(int sp) {
		asText().setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
		return this;
	}

	public ViewWrap backColorTansparent() {
		v.setBackgroundColor(Color.TRANSPARENT);
		return this;
	}

	public ViewWrap backColorPage() {
		v.setBackgroundColor(Colors.PageGray);
		return this;
	}

	public ViewWrap backColorFade() {
		v.setBackgroundColor(Colors.Fade);
		return this;
	}

	public ViewWrap backColorWhite() {
		v.setBackgroundColor(Color.WHITE);
		return this;
	}

	public ViewWrap backColorTransFade() {
		v.setBackgroundDrawable(Img.INSTANCE.colorStates(Colors.TRANS, Colors.Fade));
		return this;
	}

	public ViewWrap backColorRisk() {
		v.setBackgroundDrawable(Img.INSTANCE.colorStates(Colors.RedMajor, Colors.Fade));
		return this;
	}

	public ViewWrap backColorWhiteFade() {
		v.setBackgroundDrawable(Img.INSTANCE.colorStates(Colors.WHITE, Colors.Fade));
		return this;
	}

	public ViewWrap backColor(int normalColor, int pressedColor) {
		v.setBackgroundDrawable(Img.INSTANCE.colorStates(normalColor, pressedColor));
		return this;
	}

	public ViewWrap backColor(int color) {
		v.setBackgroundColor(color);
		return this;
	}

	public ViewWrap backDrawable(Drawable d) {
		v.setBackgroundDrawable(d);
		return this;
	}

	public ViewWrap backDrawable(Drawable normal, Drawable pressed) {
		v.setBackgroundDrawable(Img.INSTANCE.normalPressed(normal, pressed));
		return this;
	}

	public ViewWrap backDrawable(String name) {
		v.setBackgroundDrawable(Img.INSTANCE.namedStates(name, true));
		return this;
	}

	public ViewWrap padding(int dp) {
		return padding(dp, dp, dp, dp);
	}

	public ViewWrap padding(int left, int top, int right, int bottom) {
		v.setPadding(App.dp2px(left), App.dp2px(top), App.dp2px(right), App.dp2px(bottom));
		return this;
	}

	public ViewWrap paddingPx(int p) {
		return paddingPx(p, p, p, p);
	}

	public ViewWrap paddingPx(int left, int top, int right, int bottom) {
		v.setPadding(left, top, right, bottom);
		return this;
	}

	public ViewWrap gone() {
		v.setVisibility(View.GONE);
		return this;
	}

	public ViewWrap visiable() {
		v.setVisibility(View.VISIBLE);
		return this;
	}

	public ViewWrap invisiable() {
		v.setVisibility(View.INVISIBLE);
		return this;
	}
}
