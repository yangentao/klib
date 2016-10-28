package net.yet.util.app;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;

import net.yet.file.SdAppFile;
import net.yet.util.Util;
import net.yet.util.XMap;
import net.yet.util.log.xlog;

import java.io.File;

public class DownloadTask {
	public static final String FILE = "download_tasks";

	public static final int START = 0;
	public static final int FAILED = 1;
	public static final int SUCCESS = 2;

	public String url;
	public long id = -1;
	public int status = START;
	public boolean actionInstall = false;

	public String fireMsg = null;
	public String filepath = null;

	public DownloadTask() {

	}

	public DownloadTask(String url) {
		this.url = url;
	}

	public void setResult(boolean success) {
		this.status = success ? SUCCESS : FAILED;
		if (success) {
			File file = new File(filepath);
			xlog.INSTANCE.d("下载成功:", filepath);
			if (actionInstall) {
				Util.installApk(file);
			}
		} else {
			Util.toast("下载文件失败!");
		}
		if (fireMsg != null) {
			// MsgCenter.fireN(fireMsg, this);
		}
	}

	/**
	 * @param title
	 *            "更新"
	 * @param extName
	 *            ".apk"
	 */
	public void startAndInstall(String title, String extName) {
		this.actionInstall = true;
		File file = SdAppFile.INSTANCE.tempFile(extName);
		this.filepath = file.getAbsolutePath();

		DownloadManager dm = (DownloadManager) App.get().getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request req = new Request(Uri.parse(url));
		req.setTitle(title);
		req.setDestinationUri(Uri.fromFile(file));

		this.id = dm.enqueue(req);

		XMap map = XMap.load(FILE);
		map.put("" + id, this);
		map.save();
	}
}