package net.yet.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.View;

import net.yet.R;

/**
 * 调用setPagerAdapter设置adapter, 主意,adapter要提供getTitle
 * 带有toolbar和tabbar, ViewPager的Activity
 */
public class ViewPagerContainerActivity extends BaseActivity {
	protected Toolbar toolbar;
	protected FloatingActionButton fab;
	protected TabLayout tabLayout;
	protected ViewPager viewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(getLayoutId());
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		fab = (FloatingActionButton) findViewById(R.id.fab);
		tabLayout = (TabLayout) findViewById(R.id.tabLayout);

	}

	public void setPagerAdapter(PagerAdapter adapter) {
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	protected int getLayoutId() {
		return R.layout.viewpager_container;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		toolbar = null;
		fab = null;
		viewPager = null;
		tabLayout = null;
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
		Snackbar.make(viewPager, msg, Snackbar.LENGTH_LONG).setAction(action, onclick).show();
	}

	public void snack(String msg) {
		Snackbar.make(viewPager, msg, Snackbar.LENGTH_LONG).show();
	}

	@Nullable
	@Override
	public ActionMode startActionMode(ActionMode.Callback callback) {
		return toolbar.startActionMode(callback);
	}

}
