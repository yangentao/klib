package net.yet.ui.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.yet.util.AmrRecord;
import net.yet.util.ToastUtil;
import net.yet.util.Util;
import net.yet.util.xlog;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
public class VoicePanel implements AmrRecord.AMRRecordCallback {
	public static final String TEXT_IN = "手指上滑, 取消发送";
	public static final String TEXT_OUT = "松开取消";
	private static int MAX = 30000;
	private RelativeLayout parent;
	private AmrRecord amrRecord;
	private LinearLayout infoPanel;
	private WaveView waveView;
	private TextView textView;
	private VoicePanelCallback callback;
	private File lastFile;

	public void init(RelativeLayout parent) {
		Context context = parent.getContext();
		this.parent = parent;
		amrRecord = new AmrRecord();

		infoPanel = new LinearLayout(context);
		Drawable bg = ShapeUtil.round(10, Util.argb("#8000"), 1, Util.argb("#ccc"));
		XView.view(infoPanel).orientationVertical().backDrawable(bg).padding(5).gone();

		waveView = new WaveView(context);
		waveView.setColor(Color.WHITE);
		waveView.setMaxValue(MAX);
		infoPanel.addView(waveView, XView.linearParam().widthFill().height(100).get());

		textView = XView.createTextViewC(context);
		XView.view(textView).gravityCenterHorizontal().textColorWhite();
		infoPanel.addView(textView, XView.linearParam().widthFill().heightWrap().get());
		parent.addView(infoPanel, XView.relativeParam().centerInParent().width(200).heightWrap().get());
	}

	public void setCallback(VoicePanelCallback callback) {
		this.callback = callback;
	}

	public void setText(String text) {
		textView.setText(text);
	}

	public void start() {
		xlog.d("开始录音");
		File file = parent.getContext().getFileStreamPath("audio_" + System.currentTimeMillis() + ".amr");
		amrRecord.start(file, this);
		infoPanel.setVisibility(View.VISIBLE);
		waveView.clearData();
		setText(TEXT_IN);
	}

	public void end() {
		xlog.d("结束录音");
		amrRecord.stop();
		waveView.clearData();
		infoPanel.setVisibility(View.GONE);
		long duration = amrRecord.duration();
		File file = amrRecord.getFile();
		if (duration < 2000) {
			file.delete();
			ToastUtil.show("小于2秒, 取消发送");
		} else {
			if (file.exists() && callback != null) {
				lastFile = file;
				callback.onRecordOK(file);
			}
		}
	}

	public File getLastFile() {
		return lastFile;
	}

	public void cancel() {
		xlog.d("取消录音");
		amrRecord.stop();
		File file = amrRecord.getFile();
		boolean b = file.delete();
		xlog.dIf(!b, "删除文件失败");
		waveView.clearData();
		infoPanel.setVisibility(View.GONE);
	}

	@Override
	public void onAmp(ArrayList<Integer> amps) {
		waveView.setValue(amps);
	}

	public interface VoicePanelCallback {
		void onRecordOK(File file);
	}
}
