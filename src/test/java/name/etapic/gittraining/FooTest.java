package name.etapic.gittraining;

public class FooTest {

    private Foo foo;

    @org.junit.Before
    public void setUp() throws Exception {
        foo = new Foo(1, 2);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        foo = null;
    }

    @org.junit.Test
    public void testGetRight() throws Exception {
        assertEqual(2, foo.getRight());
    }
}
