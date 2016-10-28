package net.yet.util;

import android.media.MediaRecorder;

import net.yet.util.log.xlog;

import java.io.File;
import java.util.ArrayList;

public class AmrRecord {
	private int DELAY = 30;//每xx毫秒记录一次
	private int QUEUE_SIZE = 50;//每xx毫秒记录一次
	private MediaRecorder recorder = null; // 录音变量
	private boolean recording = false;
	private long start_time;// 记录录音开始时间
	private long end_time;// 记录录音开始时间
	private File recordFile;

	private FixedQueue<Integer> queue = new FixedQueue<>(QUEUE_SIZE);

	public interface AMRRecordCallback {
		void onAmp(ArrayList<Integer> amps);
	}

	public void setQueueMax(int max) {
		QUEUE_SIZE = max;
		queue.setMax(max);
	}

	public void setDelay(int delay) {
		DELAY = delay;
	}

	public boolean start(File file) {
		return start(file, null);
	}

	public boolean start(File file, final AMRRecordCallback callback) {
		for (int i = 0; i < QUEUE_SIZE; ++i) {
			queue.add(0);
		}
		recording = true;
		recordFile = file;
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
		recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);// RAW_AMR
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 设置音频编码Encoder
		recorder.setOutputFile(recordFile.getAbsolutePath());
		try {
			recorder.prepare();// 准备录制
			recorder.start();// 开始录制
			start_time = System.currentTimeMillis();
			if (callback != null) {
				TaskUtil.repeatBack(DELAY, new IRepeatCallback() {
					@Override
					public boolean onRepeat(int index, long value) {
						if (recording) {
							int n = maxAmplitude();
							queue.add(n);
							callback.onAmp(queue.toList());
						}
						return recording;
					}

					@Override
					public void onRepeatEnd() {

					}
				});
			}
			return true;
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
			release();
		}
		return false;
	}

	public File getFile() {
		return recordFile;
	}

	//毫秒
	public long duration() {
		return end_time - start_time;
	}

	private void release() {
		recording = false;
		if (recorder != null) {
			recorder.release();
			recorder = null;
		}
	}

	private int maxAmplitude() {
		if (recorder != null) {
			return recorder.getMaxAmplitude();
		}
		return 0;
	}

	public void stop() {
		end_time = System.currentTimeMillis();
		if (recorder != null) {
			recorder.stop();
		}
		release();
		queue.clear();
	}

	public boolean isRecording() {
		return recording;
	}
}
