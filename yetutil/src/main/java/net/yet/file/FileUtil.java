package net.yet.file;

import net.yet.util.StreamUtil;
import net.yet.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static long size(File file ){
		return file.length();
	}

	public static boolean writeUtf8(File file, String data) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(data.getBytes(Util.UTF8));
			os.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Util.close(os);
		}
		return false;
	}

	public static String readUtf8(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return StreamUtil.readString(fis, Util.UTF8);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Util.close(fis);
		}

		return null;
	}

	public static void append(File fromFile, File toFile) throws IOException {
		File p = toFile.getParentFile();
		if (p != null && !p.exists()) {
			p.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(toFile, true);
		FileInputStream fis = new FileInputStream(fromFile);
		StreamUtil.copyStream(fis, fos);

	}

	public static void copy(File fromFile, File toFile) throws IOException {
		if (toFile.exists()) {
			toFile.delete();
		} else {
			File p = toFile.getParentFile();
			if (p != null && !p.exists()) {
				p.mkdirs();
			}
		}

		FileOutputStream fos = new FileOutputStream(toFile, false);
		FileInputStream fis = new FileInputStream(fromFile);
		StreamUtil.copyStream(fis, fos);
	}

	public static boolean moveTo(File fromFile, File toFile) {
		if (toFile.exists()) {
			toFile.delete();
		}
		File p = toFile.getParentFile();
		if (p != null && !p.exists()) {
			p.mkdirs();
		}
		return fromFile.renameTo(toFile);
	}

}
