package yet.util;

import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yet on 2015/10/7.
 */
public class ProgressByteArrayEntity extends ByteArrayEntity {
    private static final int DEFAULT_PIECE_SIZE = 4096;

    private final byte[] mContent;
    private ProgressCallback callback;

    public ProgressByteArrayEntity(byte[] b, ProgressCallback progressCallback) {
        super(b);
        mContent = b;
        this.callback = progressCallback;
    }

    private void postStart(final long total) {
        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onStart(total);
                    callback.onProgress(total, 0, 0);
                }
            }
        });
    }

    private void postProgress(final long total, final long pos) {
        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onProgress(total, pos, (int) (pos * 100 / total));
                }
            }
        });
    }

    private void postEnd(final boolean success) {
        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFinish(success);
                }
            }
        });
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        boolean completed = false;
        try {
            int pos = 0, totalLen = mContent.length;
            if (totalLen > 0) {
                postStart(totalLen);
            }
            while (pos < totalLen) {
                int len = totalLen - pos;
                if (len > DEFAULT_PIECE_SIZE) {
                    len = DEFAULT_PIECE_SIZE;
                }
                outstream.write(mContent, pos, len);
                outstream.flush();
                pos += len;
                if (totalLen > 0) {
                    postProgress(totalLen, pos);
                }
            }
            completed = true;
            if (totalLen > 0) {
                postProgress(totalLen, pos);
                postEnd(true);
            }
        } finally {
            if (!completed) {
                postEnd(false);
            }
        }
    }
}
