package net.yet.ui.util;

import android.view.ActionMode;
import android.view.Menu;

/**
 * Created by yet on 2015/10/10.
 */
public abstract class ActionModeCallback implements ActionMode.Callback {

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {

	}
}
