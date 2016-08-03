package net.yet.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;

import net.yet.util.app.App;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class AssetUtil {
	public static AssetManager mgr() {
		return App.get().getAssets();
	}

	public static InputStream read(String name) throws IOException {
		return readBuffer(name);
	}

	public static InputStream readBuffer(String name) throws IOException {
		return mgr().open(name, AssetManager.ACCESS_BUFFER);
	}

	public static InputStream readStream(String name) throws IOException {
		return mgr().open(name, AssetManager.ACCESS_STREAMING);
	}

	public static ZipInputStream readZip(String name) throws IOException {
		InputStream is = readStream(name);
		BufferedInputStream bis = new BufferedInputStream(is, 32 * 1024);
		return new ZipInputStream(bis);
	}

	public static Uri uri(String filename) {
		return Uri.parse("file:///android_asset/" + filename);
	}

	public static String[] list(String path) {
		try {
			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}
			return mgr().list(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[] {};
	}

	// 读取assets目录下的utf8文件
	public static String textUTF8(String path) {
		try {
			InputStream is = readBuffer(path);
			String s = StreamUtil.readString(is, Util.UTF8);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 读取图片 , 适合小图片!! 设置密度是hdpi, 不支持9png!!!!
	public static Bitmap bitmap(String path) {
		InputStream is = null;
		try {
			is = readBuffer(path);
			if (is != null) {
				Bitmap bmp = BitmapFactory.decodeStream(is);
				if (bmp != null) {
					bmp.setDensity(DisplayMetrics.DENSITY_HIGH);
				}
				return bmp;
			}
		} catch (IOException e) {
			e.printStackTrace();
			xlog.e(e);
		} finally {
			Util.close(is);
		}
		return null;
	}

	// //读取图片 , 适合小图片!! 设置密度是hdpi, 支持9png
	public static Drawable drawable(String path) {
		InputStream is = null;
		try {
			is = readBuffer(path);
			if (is != null) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inScreenDensity = DisplayMetrics.DENSITY_HIGH;
				return Drawable.createFromResourceStream(App.getResources(), null, is, null, opts);
			}
			// return Drawable.createFromResourceStream(null, null, is, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			xlog.e(e);
		} finally {
			Util.close(is);
		}
		return null;
	}

}
