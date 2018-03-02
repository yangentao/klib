package yet.util;

public class DelayUtil {
	public int delay = 200;
	long pretime = 0;
	long current = System.currentTimeMillis();

	public DelayUtil() {

	}

	public DelayUtil(int delay) {
		this.delay = delay;
	}

	public boolean need() {
		current = System.currentTimeMillis();
		if (current - pretime > delay) {
			pretime = current;
			return true;
		}
		return false;
	}

}
