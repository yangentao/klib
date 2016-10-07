//package net.yet.ui.activities.drawer;
//
//import android.app.FragmentTransaction;
//import android.os.Bundle;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import net.yet.R;
//import net.yet.ui.activities.BaseActivity;
//import net.yet.ui.page.BaseFragment;
//import net.yet.ui.util.FragmentHelper;
//import net.yet.ui.util.XView;
//import net.yet.ui.widget.Action;
//import net.yet.ui.widget.TabBar;
//import net.yet.util.xlog;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class DrawerTabBarContainerActivity extends BaseActivity {
//	private DrawerLayout drawerLayout = null;
//	//	protected NavigationView navView = null;
//	private LinearLayout rootView;
//	private FrameLayout fragmentContainerView;
//	private int fragLayoutId = 0;
//	protected TabBar tabBar;
//	private HashMap<String, BaseFragment> pages = new HashMap<>();
//
//	private FragmentHelper fragmentHelper;
//	protected DrawerNavView navView;
//
//	public TabBar getTabBar() {
//		return tabBar;
//	}
//
//	public void selectTab(String tag) {
//		tabBar.select(tag);
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit();
//
//		drawerLayout = new DrawerLayout(this);
//		drawerLayout.setId(XView.genViewId());
//
//
//		rootView = XView.createLinearVertical(this);
//		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		drawerLayout.addView(rootView, lp);
//
//		navView = new DrawerNavView(this);
//		navView.setActionCallback(this);
//		DrawerLayout.LayoutParams lp2 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		lp2.gravity = Gravity.START;
//		drawerLayout.addView(navView, lp2);
//
//		this.setContentView(drawerLayout);
//
//		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//				this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//		drawerLayout.setDrawerListener(toggle);
//		toggle.syncState();
//
////		navView.setNavigationItemSelectedListener(this);
//
//		fragmentContainerView = XView.createFrameLayout(this);
//		XView.linearParam().widthFill().height(0).weight(1).set(fragmentContainerView);
//		rootView.addView(fragmentContainerView);
//		fragLayoutId = fragmentContainerView.getId();
//
//		fragmentHelper = new FragmentHelper(getFragmentManager(), fragLayoutId);
//
//		tabBar = new XTabBar(this);
//		tabBar.setCallback(this);
//		rootView.addView(tabBar.getView());
//	}
//
//	public void setNavHeaderView(View view) {
//		navView.setHeader(view);
//	}
//
//	public View getNavHeaderView() {
//		return navView.getHeader();
//	}
//
//	private List<Action> actions = new ArrayList<>();
//
//	public void addDrawerAction(Action a) {
//		actions.add(a);
//	}
//
//	public Action addDrawerAction(String titleAndTag) {
//		Action a = new Action(titleAndTag);
//		addDrawerAction(a);
//		return a;
//	}
//
//	public Action addDrawerAction(String titleAndTag, String icon) {
//		Action a = new Action(titleAndTag);
//		a.icon(icon);
//		addDrawerAction(a);
//		return a;
//	}
//
//	public void commitDrawerActions() {
//		navView.setActions(actions);
//		actions.clear();
//	}
//
//
//	public LinearLayout getRootView() {
//		return rootView;
//	}
//
//	public FrameLayout getContainerView() {
//		return fragmentContainerView;
//	}
//
//	public BaseFragment getCurrentPage() {
//		Action a = tabBar.getSelectedAction();
//		if (a != null) {
//			return pages.get(a.tag());
//		}
//		return null;
//	}
//
//	public Action addTab(Action action, BaseFragment page) {
//		tabBar.add(action);
//		pages.put(action.tag(), page);
//		return action;
//	}
//
//	@Override
//	public void onXTabBarUnselect(XTabBar bar, Action action) {
//		xlog.d("unselect ", action.tag());
//	}
//
//	@Override
//	public void onXTabBarReselect(XTabBar bar, Action action) {
//		xlog.d("reselect ", action.tag());
//	}
//
//	@Override
//	public void onXTabBarSelect(XTabBar bar, Action action) {
//		xlog.d("select ", action.tag());
//		BaseFragment page = pages.get(action.tag());
//		fragmentHelper.showFragment(page, action.tag());
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		BaseFragment currentFragment = fragmentHelper.getCurrentBaseFragment();
//		if (currentFragment != null && currentFragment.onKeyDown(keyCode, event)) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	public void finish() {
//		BaseFragment currentFragment = fragmentHelper.getCurrentBaseFragment();
//		super.finish();
//		if (currentFragment != null) {
//			ActivityAnimConf ac = currentFragment.getActivityAnim();
//			if (ac != null) {
//				this.overridePendingTransition(ac.finishEnter, ac.finishExit);
//			}
//		}
//	}
//
//	public void openDrawer() {
//		drawerLayout.openDrawer(GravityCompat.START);
//	}
//
//	public void closeDrawer() {
//		drawerLayout.closeDrawer(GravityCompat.START);
//	}
//
//	public boolean isDrawerOpen() {
//		return drawerLayout.isDrawerOpen(GravityCompat.START);
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (isDrawerOpen()) {
//			closeDrawer();
//			return;
//		}
//		BaseFragment currentFragment = fragmentHelper.getCurrentBaseFragment();
//		if (currentFragment != null) {
//			if (currentFragment.onBackPressed()) {
//				return;
//			}
//		}
//		super.onBackPressed();
//	}
//
//
//	@Override
//	public void onDrawerAction(Action action) {
//		closeDrawer();
//
//	}
//}
