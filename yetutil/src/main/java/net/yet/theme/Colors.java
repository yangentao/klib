package net.yet.theme;

import android.graphics.Color;

import net.yet.ext.GraphicExtKt;

public class Colors {
	public static final int BLACK = 0xFF000000;
	public static final int DKGRAY = 0xFF444444;
	public static final int GRAY = 0xFF888888;
	public static final int LTGRAY = 0xFFCCCCCC;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int RED = 0xFFFF0000;
	public static final int GREEN = 0xFF00FF00;
	public static final int BLUE = 0xFF0000FF;
	public static final int YELLOW = 0xFFFFFF00;
	public static final int CYAN = 0xFF00FFFF;
	public static final int MAGENTA = 0xFFFF00FF;
	public static final int TRANSPARENT = 0;

	public static final int TRANS = Color.TRANSPARENT;
	public static int LightGray = Color.LTGRAY;
	public static int GrayMajor = 0xFFDDDDDD;
	public static int Disabled = 0xFFDDDDDD;


	public static int GreenMajor = 0xFF00DD00;
	public static int RedMajor = 0xFFDD0000;

	public static int Theme = 0xFF34C4AA;
	public static int PageGray = 0xFFDDDDDD;
	public static int Title = Color.WHITE;
	public static int Fade = 0xFFFF8800;

	public static int TextColorMajor = 0xFF333333;
	public static int TextColorMid = 0xFF555555;
	public static int TextColorMinor = 0xFF777777;
	public static int TextColor = TextColorMajor;
	public static int EditBorder = 0xFF53A5CA;



	public static int Risk = 0xFFCF2C40;
	public static int Safe = 0xFF199055;
	public static int Progress = 0xFF00C864;

	public static int LineGray = Color.LTGRAY;


	public static class Green {
		public static int Light = 0xFF54792A;
		public static int Dark = 0xFF54792A;
	}

	public static int color(String value) {
		return GraphicExtKt.argb(value);
	}


}


