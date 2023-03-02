package tech.yaog.utils.statemachine;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 状态机
 * @param <T> 状态ID的类型
 * @param <E> 事件ID的类型
 * Created by ygl_h on 2017/7/17.
 */
public class StateMachine<T,E> implements State.Notify<T> {
    private static final String TAG = StateMachine.class.getSimpleName();
    private String tag = TAG;

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

    /**
     * 工作线程
     */
    protected ExecutorService executor = Executors.newSingleThreadExecutor();

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
    public boolean event(final Event<E> event) {
        logger.i(tag, currentState.id + "状态下触发事件："+event.id);
        return currentState.handle(event);
    }

    /**
     * 异步输入事件
     * @param event 事件
     */
    public void eventAsync(final Event<E> event) {
        logger.i(tag, currentState.id + "状态下异步触发事件："+event.id);
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return currentState.handle(event);
            }
        });
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

    /**
     * 异步输入事件
     * @param eventId 事件 ID
     * @param data 数据
     */
    public void eventAsync(E eventId, Object... data) {
        eventAsync(new Event<E>(eventId, data));
    }

    @Override
    public void nextState(T nextState, Object... data) {
        if (currentState != null) {
            logger.i(tag, "状态变化，离开："+currentState.id);
            currentState.exit();
        }
        for (State<T,E> state : states) {
            if (state.id.equals(nextState)) {
                currentState = state;
                break;
            }
        }
        if (currentState != null) {
            logger.i(tag, "状态变化，进入："+currentState.id);
            currentState.entry(data);
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setName(String name) {
        this.tag = name;
    }
}
