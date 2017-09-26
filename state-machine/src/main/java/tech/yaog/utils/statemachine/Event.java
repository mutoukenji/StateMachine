package tech.yaog.utils.statemachine;

/**
 * 事件
 * Created by ygl_h on 2017/7/17.
 */
public class Event<E> {
    protected E id;
    protected Object[] data;

    public Event(E id) {
        this.id = id;
        this.data = new Object[0];
    }

    public Event(E id, Object... data) {
        this.id = id;
        this.data = data;
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object... data) {
        this.data = data;
    }
}
