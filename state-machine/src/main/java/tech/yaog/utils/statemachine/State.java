package tech.yaog.utils.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 状态
 * @param <T> 状态ID的类型
 * @param <E> 事件ID的类型
 * Created by ygl_h on 2017/7/17.
 */
public class State<T,E> {
    /**
     * 状态ID
     */
    T id;
    /**
     * 通知状态机用回调接口
     */
    Notify<T> notify;
    /**
     * 事件处理映射表
     */
    Map<E, Handler> handlerMap;
    /**
     * 进入事件
     */
    Handler onEntry;
    /**
     * 离开事件
     */
    Handler onExit;

    /**
     * @param id 状态ID
     */
    public State(T id) {
        this.id = id;
        handlerMap = new HashMap<>();
    }

    /**
     * 取得本状态ID
     * @return 本状态ID
     */
    public T getId() {
        return id;
    }

    /**
     * 设置本状态ID
     * @param id 状态ID
     */
    public void setId(T id) {
        this.id = id;
    }

    /**
     * 绑定事件处理
     * @param eventId 事件ID
     * @param handler 处理
     * @return 本对象
     */
    public State<T,E> onEvent(final E eventId, final Handler handler) {
        handlerMap.put(eventId, handler);
        return this;
    }

    /**
     * 绑定状态跳转事件
     * @param eventId 事件ID
     * @param nextState 要跳转的下一个状态ID
     * @return 本对象
     */
    public State<T,E> onEvent(final E eventId, final T nextState) {
        return onEvent(eventId, nextState, null);
    }

    /**
     * 绑定状态跳转事件处理
     * @param eventId 事件ID
     * @param nextState 要跳转的下一个状态ID
     * @param handler 处理
     * @return 本对象
     */
    public State<T,E> onEvent(final E eventId, final T nextState, final Handler handler) {
        handlerMap.put(eventId, new Handler() {
            @Override
            public void handle(List<?> data) {
                if (handler != null) {
                    handler.handle(data);
                }
                notify.nextState(nextState);
            }
        });
        return this;
    }

    /**
     * 绑定进入本状态的处理
     * @param handler 处理
     * @return 本对象
     */
    public State<T,E> onEntry(final Handler handler) {
        this.onEntry = handler;
        return this;
    }

    /**
     * 绑定离开本状态时的处理
     * @param handler 处理
     * @return 本对象
     */
    public State<T,E> onExit(final Handler handler) {
        this.onExit = handler;
        return this;
    }

    void entry() {
        if (onEntry != null) {
            onEntry.handle(new ArrayList<>());
        }
    }

    void exit() {
        if (onExit != null) {
            onExit.handle(new ArrayList<>());
        }
    }

    void handle(Event<E> event) {
        Handler handler = handlerMap.get(event.id);
        if (handler != null) {
            handler.handle(event.data);
        }
    }

    interface Notify<T> {
        void nextState(T nextState);
    }

    /**
     * 事件处理
     */
    public interface Handler {
        /**
         * 处理方法实现
         * @param data 事件参数
         */
        void handle(List<?> data);
    }
}
