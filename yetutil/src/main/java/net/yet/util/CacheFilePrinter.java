package net.yet.util;

import net.yet.file.SdAppFile;

import java.io.File;

public class CacheFilePrinter implements xlog.XPrinter {
	xlog.FilePrinter p = null;

	public CacheFilePrinter() {
		File file = SdAppFile.INSTANCE.log(DateUtil.date() + ".txt");
		p = new xlog.FilePrinter(file, Util.K * 8, Util.M * 4);
	}

	@Override
	public void flush() {
		if (p != null) {
			p.flush();
		}
	}

	@Override
	public void println(int priority, String tag, String msg) {
		if (p != null) {
			p.println(priority, tag, msg);
		}
	}
}
