package net.yet.ui.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import net.yet.ui.page.BaseFragment;
import net.yet.util.xlog;

/**
 * Created by yet on 2015/10/10.
 */
public class FragmentHelper {
    private final FragmentManager fm;
    private final int frameLayoutId;
    private Fragment currentFragment;

    public FragmentHelper(FragmentManager fm, int frameLayoutId) {
        this.fm = fm;
        this.frameLayoutId = frameLayoutId;
    }

    public BaseFragment getCurrentBaseFragment() {
        if (currentFragment instanceof BaseFragment) {
            return (BaseFragment) currentFragment;
        }
        return null;
    }

    public Fragment getCurrent() {
        return currentFragment;
    }

    public Fragment find(String tag) {
        return fm.findFragmentByTag(tag);
    }

    public void replace(Fragment fragment, String tag) {
        this.currentFragment = fragment;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameLayoutId, fragment, tag).commit();
    }

    public void replace(Fragment fragment, String tag, int inAnim, int outAnim) {
        this.currentFragment = fragment;
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(inAnim, outAnim);
        ft.replace(frameLayoutId, fragment, tag).commit();
    }

    public void add(Fragment fragment, String tag) {
        xlog.d("addFragment: ", fragment.getClass().getSimpleName());

        Fragment old = find(tag);
        if (old == fragment) {//已经添加过了, 不再添加, tag相同且fragment相同
            fm.beginTransaction().show(fragment).commit();
            currentFragment = fragment;
            return;
        }
        if (old != null) {
            xlog.e("already exist tag ", tag);
            return;
        }

        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        ft.add(frameLayoutId, fragment, tag);
        ft.commit();
        currentFragment = fragment;
    }

    public void showFragment(Fragment fragment, String tag) {
        Fragment old = find(tag);
        if (old == null) {
            add(fragment, tag);
        } else {
            show(tag);
            if (old != fragment) {
                xlog.e("show fragment with different fragment ");
            }
        }
    }

    public void show(String tag) {
        Fragment f = find(tag);
        if (f == null) {
            xlog.e("fragment no found by tag ", tag);
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null && currentFragment != f) {
            ft.hide(currentFragment);
        }
        ft.show(f);
        ft.commit();
        currentFragment = f;
    }

    public void hide(String tag) {
        Fragment f = find(tag);
        if (f == null) {
            xlog.e("fragment not found by tag", tag);
            return;
        }
        fm.beginTransaction().hide(f).commit();
    }

    public void remove(String tag) {
        Fragment fragment = find(tag);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
            if (currentFragment == fragment) {
                currentFragment = null;
            }
        }
    }
}
