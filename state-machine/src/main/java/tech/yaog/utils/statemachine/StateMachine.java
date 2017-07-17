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
     */
    public void event(Event<E> event) {
        currentState.handle(event);
    }

    @Override
    public void nextState(T nextState) {
        if (currentState != null) {
            currentState.exit();
        }
        for (State<T,E> state : states) {
            if (state.id.equals(nextState)) {
                currentState = state;
                break;
            }
        }
        if (currentState != null) {
            currentState.entry();
        }
    }
}
