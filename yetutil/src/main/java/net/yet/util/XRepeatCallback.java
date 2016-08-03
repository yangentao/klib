package net.yet.util;

public class XRepeatCallback implements IRepeatCallback {
    public boolean running = false;
    public boolean canceled = false;

    @Override
    public boolean onRepeat(int index, long value) {
        running = true;
        return !canceled;
    }

    @Override
    public void onRepeatEnd() {
        running = false;
    }

}
