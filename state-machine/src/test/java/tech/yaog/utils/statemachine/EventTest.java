package tech.yaog.utils.statemachine;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by mutoukenji on 17-7-18.
 */
public class EventTest {
    @Test
    public void getId() throws Exception {
        Event<Integer> event = new Event<>(1);
        event.setId(2);
        assertEquals(true, 2 == event.getId());
    }

    @Test
    public void getData() throws Exception {
        Event<String> event = new Event<>("id");
        ArrayList<String> p = new ArrayList<>();
        p.add("1");
        event.setData(1, "1", 1L, true, '1', p, new String[]{"1"});
        assertEquals(7, event.getData().length);
        assertEquals(1, event.getData()[0]);
        assertEquals("1", event.getData()[1]);
        assertEquals(1L, event.getData()[2]);
        assertEquals(true, event.getData()[3]);
        assertEquals('1', event.getData()[4]);
        assertEquals("1", ((ArrayList<String>)event.getData()[5]).get(0));
        assertEquals("1", ((String[])event.getData()[6])[0]);
    }

}