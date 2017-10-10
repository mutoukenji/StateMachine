package tech.yaog.utils.statemachine;

import android.util.Log;

/**
 * Android Logcat 默认Log输出
 * Created by ygl_h on 2017/9/10.
 */
public class LogcatLogger implements Logger {

    protected String getMessage(String format, Object... args) {
        String message;
        if (args.length > 0) {
            message = String.format(format, args);
        }
        else {
            message = format;
        }
        return message;
    }

    @Override
    public void v(String tag, String format, Object... args) {
        Log.v(tag, getMessage(format, args));
    }

    @Override
    public void d(String tag, String format, Object... args) {
        Log.d(tag, getMessage(format, args));
    }

    @Override
    public void i(String tag, String format, Object... args) {
        Log.i(tag, getMessage(format, args));
    }

    @Override
    public void w(String tag, String format, Object... args) {
        Log.w(tag, getMessage(format, args));
    }

    @Override
    public void e(String tag, String format, Object... args) {
        Log.e(tag, getMessage(format, args));
    }
}
