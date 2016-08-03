package net.yet.ui.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import net.yet.R;
import net.yet.ui.page.BaseFragment;
import net.yet.ui.util.FragmentHelper;
import net.yet.util.Util;

/**
 * addTab添加标签
 * onSelectTab执行动作.
 * 可以使用fragmentHelper来更改fragment
 */
public class FragmentPage extends BaseActivity {
    private static Fragment gFragment = null;
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    protected ViewGroup contentLayout;
    protected FragmentHelper fragmentHelper = null;

    public static void open(Context context, Fragment fragment) {
        Util.debugFail(gFragment != null, "gfragment is not null");
        gFragment = fragment;
        context.startActivity(new Intent(context, FragmentPage.class));
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Fragment f = gFragment;
        gFragment = null;
        setContentView(getLayoutId());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentLayout = (ViewGroup) findViewById(R.id.frameLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fragmentHelper = new FragmentHelper(getFragmentManager(), R.id.frameLayout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavClick(v);
            }
        });

        if (f != null) {
            fragmentHelper.replace(f, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void onNavClick(View v) {
        BaseFragment f = getBaseFragment();
        if (f != null) {
//			f.onNavClick(v);
        }
    }

    public void replaceFragment(Fragment fragment) {
        fragmentHelper.replace(fragment, null);
    }

    public BaseFragment getBaseFragment() {
        Fragment f = fragmentHelper.getCurrent();
        if (f instanceof BaseFragment) {
            return (BaseFragment) f;
        }
        return null;
    }

    protected int getLayoutId() {
        return R.layout.fragment_page;
    }

    public Toolbar getToolbar() {
        return this.toolbar;
    }

    public void showFloatingActionButton(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }

    public void snack(String msg, String action, View.OnClickListener onclick) {
        Snackbar.make(contentLayout, msg, Snackbar.LENGTH_LONG).setAction(action, onclick).show();
    }

    public void snack(String msg) {
        Snackbar.make(contentLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return toolbar.startActionMode(callback);
    }

    @Override
    public void finish() {
        super.finish();
        BaseFragment f = getBaseFragment();
        if (f != null) {
            AnimConf ac = f.getActivityAnim();
            if (ac != null) {
                this.overridePendingTransition(ac.getFinishEnter(), ac.getFinishExit());
            }
        }
    }

    protected boolean onBackPressedFragment() {
        BaseFragment f = getBaseFragment();
        if (f != null) {
            return f.onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!onBackPressedFragment()) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseFragment f = getBaseFragment();
        if (f != null && f.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar = null;
        fab = null;
        contentLayout = null;
        fragmentHelper = null;
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
//		CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) contentLayout.getLayoutParams();
//		toolbar.measure(0, 0);
//		int h = toolbar.getMeasuredHeight();
//		p.setMargins(0, 0, 0, h);
//		contentLayout.setLayoutParams(p);
//		contentLayout.requestLayout();
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
//		CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) contentLayout.getLayoutParams();
//		p.setMargins(0, 0, 0, 0);
//		contentLayout.setLayoutParams(p);
//		contentLayout.requestLayout();
    }
}
