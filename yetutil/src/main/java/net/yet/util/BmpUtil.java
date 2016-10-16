package net.yet.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.DisplayMetrics;

import net.yet.util.app.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BmpUtil {


	public static Bitmap fromAsset(String path) {
		return AssetUtil.bitmap(path);
	}

	public static Bitmap fromRes(int resId) {
		return BitmapFactory.decodeResource(App.getResources(), resId);
	}

	public static Bitmap scaleTo(int resId, int newWidth, int newHeight) {
		return scaleTo(fromRes(resId), newWidth, newHeight);
	}

	/**
	 * @param old       非空, 高宽都大于0
	 * @param newWidth  >0
	 * @param newHeight >0
	 * @return
	 */
	public static Bitmap scaleTo(Bitmap old, int newWidth, int newHeight) {
		if (old == null) {
			return null;
		}
		int width = old.getWidth();
		int height = old.getHeight();
		if (width == 0) {
			width = 1;
		}
		if (height == 0) {
			height = 1;
		}
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true);
	}

	/**
	 * 按比例缩小图片
	 *
	 * @param old
	 * @param scale 0-1.0之间, 0.5缩小 1倍
	 * @return
	 */
	public static Bitmap scale(Bitmap old, float scale) {
		if (old == null) {
			return null;
		}
		int width = old.getWidth();
		int height = old.getHeight();
		if (width == 0 || height == 0) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true);
	}

	public static Bitmap rotate(Bitmap old, int degree) {
		if (old == null) {
			return null;
		}
		int width = old.getWidth();
		int height = old.getHeight();
		if (width == 0 || height == 0) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true);
	}

	public static int degress(File file) {
		String path = file.getAbsolutePath();
		int degree = 0;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap line(int width, int height, int color) {
		Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		target.setDensity(DisplayMetrics.DENSITY_HIGH);
		Canvas canvas = new Canvas(target);
		canvas.drawColor(color);
		return target;
	}

	public static BitmapDrawable lineDraw(int width, int height, int color) {
		return new BitmapDrawable(App.getResources(), line(width, height, color));
	}

	//指定圆角
	public static Bitmap round(Bitmap source, int corner) {
		if (source == null) {
			return null;
		}
		int w = source.getScaledWidth(App.getResources().getDisplayMetrics());
		int h = source.getScaledHeight(App.getResources().getDisplayMetrics());

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, w, h);
		canvas.drawRoundRect(rect, corner, corner, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	//高宽的一半做圆角
	public static Bitmap round(Bitmap source) {
		if (source == null) {
			return null;
		}
		int w = source.getScaledWidth(App.getResources().getDisplayMetrics());
		int h = source.getScaledHeight(App.getResources().getDisplayMetrics());

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, w, h);
		canvas.drawRoundRect(rect, w / 2, h / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	//圆,  高和宽较最小的值做直径,
	public static Bitmap oval(Bitmap source) {
		if (source == null) {
			return null;
		}
		int w = source.getScaledWidth(App.getResources().getDisplayMetrics());
		int h = source.getScaledHeight(App.getResources().getDisplayMetrics());
		int d = Math.min(w, h);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(d, d, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, d, d);
		canvas.drawRoundRect(rect, d / 2, d / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, (w - d) / 2, (h - d) / 2, paint);
		return target;
	}

	public static MySize sizeOf(Uri uri) throws FileNotFoundException {
		return sizeOfStream(App.openStream(uri));
	}

	public static MySize sizeOf(File file) throws FileNotFoundException {
		return sizeOfStream(new FileInputStream(file));
	}

	//会关闭流
	private static MySize sizeOfStream(InputStream inputStream) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream, null, opts);
			return new MySize(opts.outWidth, opts.outHeight);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Util.close(inputStream);
		}
		return null;
	}

	//会关闭流
	private static Bitmap decodeStream(InputStream inputStream, int inSampleSize, Bitmap.Config config) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = inSampleSize;
		opts.inPreferredConfig = config;
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, opts);
		Util.close(inputStream);
		return bmp;
	}

	private static Bitmap decodeStream(InputStream inputStream) {
		Bitmap bmp = BitmapFactory.decodeStream(inputStream);
		Util.close(inputStream);
		return bmp;
	}

	/**
	 * 图片高*宽不超过maxSize
	 *
	 * @param uri
	 * @param maxSize newWidth*newHeight
	 * @return
	 */
	public static Bitmap fromUri(Uri uri, int maxSize, Bitmap.Config config) {
		if (uri == null) {
			return null;
		}
		try {
			MySize size = sizeOf(uri);
			if (size.area() > maxSize && maxSize > 0) {
				// 300 * 200 /(100*100) ==> 6
				float scale = size.area() * 1.0f / maxSize;
				scale = (float) Math.sqrt(scale);// 2.4
				int n = Math.round(scale);// 2
				return decodeStream(App.openStream(uri), n, config);
			} else {
				return decodeStream(App.openStream(uri));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Bitmap fromUri(Uri uri) {
		if (uri == null) {
			return null;
		}
		try {
			return decodeStream(App.openStream(uri));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//maxSize是指的高*宽, 最终bitmap占用的内存是  maxSize*每个像素字节数
	//hiQulity是true, 每像素占用4字节,  false:2字节
	public static Bitmap fromFile(File imageFile, int maxSize, Bitmap.Config config) {
		if (null == imageFile || !imageFile.exists()) {
			return null;
		}
		try {
			MySize size = sizeOf(imageFile);
			if (size.area() > maxSize && maxSize > 0) {
				// 300 * 200 /(100*100) ==> 6
				float scale = size.area() * 1.0f / maxSize;
				scale = (float) Math.sqrt(scale);// 2.4
				int n = Math.round(scale);// 2
				return decodeStream(new FileInputStream(imageFile), n, config);
			} else {
				return decodeStream(new FileInputStream(imageFile));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean savePng(Bitmap bmp, File saveTo) {
		try {
			FileOutputStream fos = new FileOutputStream(saveTo);
			return savePng(bmp, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean savePng(Bitmap bmp, OutputStream saveTo) {
		try {
			return bmp.compress(CompressFormat.PNG, 100, saveTo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Util.close(saveTo);
		}
		return false;
	}

	public static boolean saveJpg(Bitmap bmp, File saveTo) {
		try {
			FileOutputStream fos = new FileOutputStream(saveTo);
			return saveJpg(bmp, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean saveJpg(Bitmap bmp, OutputStream saveTo) {
		try {
			return bmp.compress(CompressFormat.JPEG, 100, saveTo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Util.close(saveTo);
		}
		return false;
	}


	//返回"image/png", "image/jpeg",或null
	public static String mimeType(Uri uri) {
		InputStream is = null;
		try {
			is = App.openStream(uri);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opts);
			return opts.outMimeType;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Util.close(is);
		}
		return null;
	}
}
