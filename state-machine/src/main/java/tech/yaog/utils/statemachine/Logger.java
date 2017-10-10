package tech.yaog.utils.statemachine;

/**
 * Log接口
 * Created by ygl_h on 2017/9/10.
 */
public interface Logger {
    void v(String tag, String format, Object... args);
    void d(String tag, String format, Object... args);
    void i(String tag, String format, Object... args);
    void w(String tag, String format, Object... args);
    void e(String tag, String format, Object... args);
}
