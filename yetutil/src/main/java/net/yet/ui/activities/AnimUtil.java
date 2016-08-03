package net.yet.ui.activities;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

//AnimUtil.rotate(editText, 1000, 0, 360);
//AnimUtil.alpha(editText, 1000, 1.0f, 0.2f);
//AnimUtil.translateY(editText, 1000, 0, 100);
//AnimUtil.scale(editText, 1000, 1, 0.5f, 1, 0.5f);
//AnimUtil au = new AnimUtil();
//au.alpha(1, 0.2f).scale(1, 0.2f, 1, 1).start(editText, 2000);
public class AnimUtil {

    private AnimationSet animSet = new AnimationSet(true);

    public AnimUtil trans(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        animSet.addAnimation(anim);
        return this;
    }

    public AnimUtil alpha(float fromAlpha, float toAlpha) {
        animSet.addAnimation(new AlphaAnimation(fromAlpha, toAlpha));
        return this;
    }

    public AnimUtil scale(float fromX, float toX, float fromY, float toY) {
        animSet.addAnimation(new ScaleAnimation(fromX, toX, fromY, toY));
        return this;
    }

    public AnimUtil rotate(float fromDegrees, float toDegrees) {
        animSet.addAnimation(new RotateAnimation(fromDegrees, toDegrees));
        return this;
    }

    public AnimationSet get() {
        return animSet;
    }

    public void start(View view, int duration) {
        animSet.setDuration(duration);
        view.startAnimation(animSet);
    }

    public static void translateX(View view, int durationMillis, float fromXDelta, float toXDelta) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
        anim.setDuration(durationMillis);
        view.startAnimation(anim);
    }

    public static TranslateAnimation translateY(View view, int durationMillis, float fromYDelta, float toYDelta) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
        anim.setDuration(durationMillis);
        view.startAnimation(anim);
        return anim;
    }

    public static void alpha(View view, int durationMillis, float fromAlpha, float toAlpha) {
        AlphaAnimation aa = new AlphaAnimation(fromAlpha, toAlpha);
        aa.setDuration(durationMillis);
        view.startAnimation(aa);
    }

    //fromX, toX, fromY, toY是百分比,  0.5f
    //相对于自己的中点
    public static ScaleAnimation scale(View view, int durationMillis, float fromX, float toX, float fromY,
                                       float toY) {
        ScaleAnimation sa = new ScaleAnimation(fromX, toX, fromY, toY,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(durationMillis);
        view.startAnimation(sa);
        return sa;
    }

    //相对于自己的中点,  fromDegrees, toDegrees 是度, 如 180
    public static void rotate(View view, int durationMillis, float fromDegrees, float toDegrees) {
        RotateAnimation ra = new RotateAnimation(fromDegrees, toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(durationMillis);
        view.startAnimation(ra);
    }
}
