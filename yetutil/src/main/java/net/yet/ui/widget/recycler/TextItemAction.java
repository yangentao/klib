package net.yet.ui.widget.recycler;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import net.yet.theme.Colors;
import net.yet.theme.Dim;
import net.yet.ui.res.Img;
import net.yet.util.app.App;

import static net.yet.ui.util.XView.linearParam;

/**
 * Created by yet on 2015/10/28.
 */
public class TextItemAction extends TextView {

    public TextItemAction(Context context) {
        super(context);
        miniWidth(60).miniHeight(40).textSize(Dim.textSize).textColor(Colors.TextColorMinor).gravityCenter();
        linearParam().widthWrap().heightFill().set(this);
        padding(10, 0, 10, 0);
    }

    public TextItemAction text(String text) {
        setText(text);
        return this;
    }

    public String text() {
        return this.getText().toString();
    }

    public TextItemAction textSize(int sp) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public TextItemAction textColor(int color) {
        setTextColor(color);
        return this;
    }

    public TextItemAction miniWidth(int dp) {
        setMinWidth(App.dp2px(dp));
        return this;
    }

    public TextItemAction miniHeight(int dp) {
        setMinHeight(App.dp2px(dp));
        return this;
    }

    public TextItemAction backColor(int normal, int pressed) {
        setBackgroundDrawable(Img.INSTANCE.colorStates(normal, pressed));
        return this;
    }

    public TextItemAction gravityCenter() {
        setGravity(Gravity.CENTER);
        return this;
    }

    public TextItemAction gravityCenterVer() {
        setGravity(Gravity.CENTER_VERTICAL);
        return this;
    }

    public TextItemAction gravityCenterHor() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    public TextItemAction gravityLeftCenter() {
        setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        return this;
    }

    public TextItemAction gravityRightCenter() {
        setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        return this;
    }

    public TextItemAction padding(int leftDp, int topDp, int righDp, int bottomDp) {
        setPadding(App.dp2px(leftDp), App.dp2px(topDp), App.dp2px(righDp), App.dp2px(bottomDp));
        return this;
    }
}
