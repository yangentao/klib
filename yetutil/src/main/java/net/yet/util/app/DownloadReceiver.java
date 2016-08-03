package net.yet.util.app;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import net.yet.util.XMap;

public class DownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		if (id == -1) {
			return;
		}
		XMap map = XMap.load(DownloadTask.FILE);
		DownloadTask task = (DownloadTask) map.getAs("" + id, DownloadTask.class);
		if (task != null) {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(id);
			query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_FAILED);
			DownloadManager dm = (DownloadManager) App.get().getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor c = dm.query(query);
			if (c.moveToFirst()) {
				map.remove("" + id);
				map.save();
				int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
				task.setResult(DownloadManager.STATUS_SUCCESSFUL == status);
			}
		}
	}
}
