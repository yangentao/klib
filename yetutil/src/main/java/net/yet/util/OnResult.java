package net.yet.util;

public interface OnResult<T> {

    /**
     * @param code
     * @param data
     * @param code 0表示成功
     */
    void onResult(boolean success, int code, String msg, T data);

}
