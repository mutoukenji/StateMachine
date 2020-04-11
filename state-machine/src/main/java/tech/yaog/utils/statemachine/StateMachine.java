package tech.yaog.utils.statemachine;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 状态机
 * @param <T> 状态ID的类型
 * @param <E> 事件ID的类型
 * Created by ygl_h on 2017/7/17.
 */
public class StateMachine<T,E> implements State.Notify<T> {
    private static final String TAG = StateMachine.class.getName();

    private Logger logger;
    /**
     * 状态列表
     */
    protected List<State<T,E>> states;
    /**
     * 初始状态
     */
    protected T initStateId;
    /**
     * 当前状态
     */
    protected State<T,E> currentState;

    public StateMachine() {
        this.states = new CopyOnWriteArrayList<>();
        this.logger = new LogcatLogger();
    }

    /**
     * 设置状态表
     * @param initState 初始状态
     * @param otherStates 其他各状态
     */
    public void setStates(State<T,E> initState, State<T,E>... otherStates) {
        initStateId = initState.id;
        states.add(initState);
        Collections.addAll(states, otherStates);
        for (State<T,E> state : states) {
            state.notify = this;
        }
    }

    /**
     * 启动状态机
     */
    public void start() {
        nextState(initStateId);
    }

    /**
     * 输入事件
     * @param event 事件
     * @return 是否处理了此事件
     */
    public boolean event(Event<E> event) {
        logger.i(TAG, currentState.id + "状态下触发事件："+event.id);
        return currentState.handle(event);
    }

    /**
     * 输入事件
     * @param eventId 事件 ID
     * @param data 数据
     * @return 是否处理了此事件
     */
    public boolean event(E eventId, Object... data) {
        return event(new Event<E>(eventId, data));
    }

    @Override
    public void nextState(T nextState, Object... data) {
        if (currentState != null) {
            logger.i(TAG, "状态变化，离开："+currentState.id);
            currentState.exit();
        }
        for (State<T,E> state : states) {
            if (state.id.equals(nextState)) {
                currentState = state;
                break;
            }
        }
        if (currentState != null) {
            logger.i(TAG, "状态变化，进入："+currentState.id);
            currentState.entry(data);
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
