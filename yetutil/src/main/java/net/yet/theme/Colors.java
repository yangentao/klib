package net.yet.theme;

import android.graphics.Color;

import net.yet.ext.GraphicExtKt;
import net.yet.util.Hex;

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

	public static int EditFocus = 0xFF38C4B0;


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
	public static int rgb(int r, int g, int b) {
		return Color.rgb(r, g, b);
	}

	//0xff8800 --> "#ff8800"
	public static String toStringColor(int color) {
		int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		if (a == 0xff) {
			return "#" + Hex.encode((byte) r) + Hex.encode((byte) g) + Hex.encode((byte) b);
		}
		return "#" + Hex.encode((byte) a) + Hex.encode((byte) r) + Hex.encode((byte) g) + Hex.encode((byte) b);
	}


}


