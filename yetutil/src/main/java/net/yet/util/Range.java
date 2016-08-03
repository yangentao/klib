package net.yet.util;

//[start, end)
public class Range {
	public int start = 0;
	public int end = 0;

	public Range() {

	}

	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public void setLength(int len) {
		end = start + len;
	}

	public int length() {
		return end - start;
	}

	@Override
	public String toString() {
		return "[" + String.valueOf(start) + ", " + String.valueOf(end) + ")";
	}
}
