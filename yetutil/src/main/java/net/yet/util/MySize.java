package net.yet.util;

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
public class MySize {

	public int width = 0;
	public int height = 0;

	public MySize() {
		this(0, 0);
	}

	public MySize(int width, int height) {
		this.width = width;
		this.height = height;
	}


	public int area() {
		return width * height;
	}
}
