package yet.util;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import yet.ui.util.RoundBackgroundSpan;
import yet.util.app.App;

public class SpanUtil {
	private SpannableString ss;

	private SpanUtil() {
	}

	public static SpanUtil str(String s) {
		SpanUtil su = new SpanUtil();
		su.ss = new SpannableString(s);
		return su;
	}

	public SpannableString get() {
		return ss;
	}

	//字体颜色
	public SpanUtil foreColor(int color, int start, int end) {
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//背景色
	public SpanUtil backColor(int color, int start, int end) {
		ss.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//缩放字体
	public SpanUtil scaleX(float scale, int start, int end) {
		ss.setSpan(new ScaleXSpan(scale), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//设置字体大小
	public SpanUtil size(int sizeSp, int start, int end) {
		ss.setSpan(new AbsoluteSizeSpan(App.INSTANCE.sp2px(sizeSp)), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//设置字体"sans"
	public SpanUtil typeface(String family, int start, int end) {
		ss.setSpan(new TypefaceSpan(family), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//设置字体粗体
	public SpanUtil bold(int start, int end) {
		ss.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//设置斜体
	public SpanUtil italic(int start, int end) {
		ss.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//设置粗斜体
	public SpanUtil boldItalic(int start, int end) {
		ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//删除线
	public SpanUtil strikethrough(int start, int end) {
		ss.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//下划线
	public SpanUtil underline(int start, int end) {
		ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//下标
	public SpanUtil subscript(int start, int end) {
		ss.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//上标
	public SpanUtil superscript(int start, int end) {
		ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//url
	public SpanUtil url(String url, int start, int end) {
		ss.setSpan(new URLSpan(url), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//image
	public SpanUtil image(Drawable d, int start, int end) {
		ss.setSpan(new ImageSpan(d), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//image
	public SpanUtil imageBaseline(Drawable d, int start, int end) {
		ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	public SpanUtil roundBackground(int backColor, int textColor, int radius, int start, int end) {
		ss.setSpan(new RoundBackgroundSpan(backColor, textColor, radius), start, end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

	//url
	public SpanUtil clickable(String url, int start, int end,
			final View.OnClickListener clickListener) {
		ClickableSpan sp = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				if (clickListener != null) {
					clickListener.onClick(widget);
				}
			}
		};
		ss.setSpan(sp, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return this;
	}

}
