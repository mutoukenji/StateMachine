package tech.yaog.utils.statemachine;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mutoukenji on 17-7-18.
 */
public class StateTest {
    @Test
    public void getId() throws Exception {
        State<Integer,Integer> state = new State<>(1);
        state.setId(2);
        assertEquals(true, 2 == state.getId());
    }

    @Test
    public void onEvent() throws Exception {
        State<String, String> state = new State<>("1");
        final String[] a = {""};
        state.onEvent("2", new State.Handler() {
            @Override
            public void handle(Object... data) {
                a[0] = "a"+data[0].toString();
            }
        });
        state.handle(new Event<>("2","4","5"));
        assertEquals("a4", a[0]);
    }

}