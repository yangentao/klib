package yet.ui.util;

import android.view.View;

public abstract class MultiClickListener implements View.OnClickListener {

	private long pre = 0;
	private int count = 0;
	private int expect = 3;

	public MultiClickListener(int expectClickCount) {
		if (expectClickCount < 1) {
			expectClickCount = 1;
		}
		this.expect = expectClickCount;
	}

	@Override
	public void onClick(View v) {
		long c = System.currentTimeMillis();
		if (c - pre < 2000) {
			++count;
		} else {
			count = 0;
		}
		pre = c;
		if (count >= expect - 1) {
			onTrigger(count + 1);
			count = 0;
			pre = 0;
		}
	}

	abstract protected void onTrigger(int count);
}
