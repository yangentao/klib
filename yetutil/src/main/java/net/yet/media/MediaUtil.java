package net.yet.media;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import net.yet.util.Util;
import net.yet.util.xlog;

import java.io.File;
import java.io.IOException;

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
public class MediaUtil {
	/**
	 * 调用系统播放器
	 *
	 * @param context
	 * @param file
	 */
	public static void playSound(Context context, File file) {
		int n = file.getAbsolutePath().lastIndexOf('.');
		String ext = null;
		if (n > 0) {
			ext = file.getAbsolutePath().substring(n + 1);
		}
		playSound(context, file, ext);
	}

	public static void playSound(Context context, File file, String extType) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Util.empty(extType)) {
			extType = "*";
		}
		intent.setDataAndType(Uri.fromFile(file), "audio/" + extType);
		context.startActivity(intent);
	}

	public static MediaPlayer playSound(File file, final MediaPlayer.OnCompletionListener callback) {
		if (!file.exists()) {
			return null;
		}
		MediaPlayer player = new MediaPlayer();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				if (callback != null) {
					callback.onCompletion(mp);
				}
			}
		});
		try {
			player.setDataSource(file.getAbsolutePath());
			player.prepare();
			player.start();
			return player;
		} catch (IOException e) {
			e.printStackTrace();
			xlog.e(e);
			player.release();
			if (callback != null) {
				callback.onCompletion(null);
			}
		}
		return null;
	}
}
