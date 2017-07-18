package tech.yaog.utils.statemachine;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ygl_h on 2017/7/17.
 */
public class StateMachineTest {
    private StateMachine<Integer, Integer> tradeStateMachine;

    private enum STATE {
        IDEL(0),
        WAIT_TO_CONFIRM(1),
        CONFIRMED(2),
        QR_GETED(3),
        WAIT_TO_PAY(4),
        PAYED(5),
        WAIT_CHECK(6),
        SUCCEED(7),
        TO_RESET(8)
        ;

        int val;
        STATE(int val) {
            this.val = val;
        }
    }

    enum EVENT {
        SELECT(0),
        CANCEL(1),
        SELECTED(2),
        SELECT_FAILED(52),
        CONFIRMED(3),
        CONFIRM_FAILED(53),
        U_PAYED(4),
        U_PAY_FAILED(54),
        RESETED(5),
        TIMEOUT(6),
        QR_GETED(7),
        QR_GETFAILED(57),
        W_PAYED(8),
        W_PAYFAILED(9),
        CAN_OUT(10),
        CANNOT_OUT(11)
        ;

        int val;
        EVENT(int val) {
            this.val = val;
        }
    }

    private String output = "";

    @Before
    public void setUp() throws Exception {
        tradeStateMachine = new StateMachine<>();
        State<Integer, Integer> idle = new State<Integer, Integer>(STATE.IDEL.val)
                .onEntry(new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+="IDEL;";
                    }
                })
                .onEvent(EVENT.SELECT.val, STATE.WAIT_TO_CONFIRM.val, new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+=String.valueOf(data[0])+";";
                    }
                });
        State<Integer, Integer> selected = new State<Integer, Integer>(STATE.WAIT_TO_CONFIRM.val)
                .onEntry(new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+="TO_CONFIRM;CONFIRM_SEND;";
                    }
                })
                .onEvent(EVENT.CANCEL.val, STATE.TO_RESET.val, new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+="CANCELED;";
                    }
                })
                .onEvent(EVENT.TIMEOUT.val, STATE.TO_RESET.val);
        State<Integer, Integer> toRest = new State<Integer, Integer>(STATE.TO_RESET.val)
                .onEntry(new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+="TO_RESET;";
                    }
                })
                .onEvent(EVENT.RESETED.val,STATE.IDEL.val)
                .onExit(new State.Handler() {
                    @Override
                    public void handle(Object... data) {
                        output+="TO_RESET_EXIT;";
                    }
                });

        tradeStateMachine.setStates(idle, selected, toRest);
    }

    @Test
    public void testSelectAndCancel() {
        output = "";
        tradeStateMachine.start();
        Event<Integer> selectEvent = new Event<>(EVENT.SELECT.val);
        selectEvent.setData(1);
        tradeStateMachine.event(selectEvent);
        tradeStateMachine.event(new Event<>(EVENT.CANCEL.val));
        tradeStateMachine.event(new Event<>(EVENT.CANCEL.val));
        tradeStateMachine.event(new Event<>(EVENT.RESETED.val));
        assertEquals("IDEL;1;TO_CONFIRM;CONFIRM_SEND;CANCELED;TO_RESET;TO_RESET_EXIT;IDEL;", output);
    }

}