package tech.yaog.utils.statemachine;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件
 * Created by ygl_h on 2017/7/17.
 */
public class Event<E> {
    protected E id;
    protected List<?> data;

    public Event(E id) {
        this.id = id;
        this.data = new ArrayList<>();
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
