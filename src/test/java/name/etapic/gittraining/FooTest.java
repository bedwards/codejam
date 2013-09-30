package name.etapic.gittraining;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FooTest {

    private Foo foo;

    @Before
    public void setUp() throws Exception {
        foo = new Foo(1, 2);
    }

    @After
    public void tearDown() throws Exception {
        foo = null;
    }

    @Test
    public void testGetRight() throws Exception {
        assertEquals(2, foo.getRight());
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(3, foo.add());
    }
}
