package person.ditunes.example.mock;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Created by linhan on 16/9/21.
 */
public class MockTest {

    @Test
    public void what_is_the_spy(){
        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls real methods
        spy.add("one");
        spy.add("two");

        Assert.assertEquals("spy invoke real word and the first item should be one","one", spy.get(0));
        Assert.assertEquals("spy alseo can be stubbed and return expected", 100,spy.size());

        verify(spy).add("one");
        verify(spy).add("two");
    }

    @Test
    public void what_is_the_mock(){
        List list = new LinkedList();
        List spy = spy(list);
        List mock = mock(List.class);

        mock.add("1");
        Assert.assertEquals("if you not ndefined the action, mock obj can't do anything",0, mock.size());

        doReturn(3).when(mock).size();
        Assert.assertEquals("mock just do stub and return what you defined",3, mock.size());

    }


}
