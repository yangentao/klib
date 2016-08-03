package net.yet.ui.res;

import net.yet.util.NoProguard;

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

@NoProguard
public enum State {
	Pressed(android.R.attr.state_pressed, "pressed"),
	Selected(android.R.attr.state_selected, "selected"),
	Checked(android.R.attr.state_checked, "checked"),
	Focused(android.R.attr.state_focused, "focused"),
	Disabled(-android.R.attr.state_enabled, "disabled");

	private int stateN = 0;
	private String stateS = "";

	State(int stateN, String stateS) {
		this.stateN = stateN;
		this.stateS = stateS;
	}

	public int stateInt() {
		return stateN;
	}

	public String stateString() {
		return stateS;
	}
}
