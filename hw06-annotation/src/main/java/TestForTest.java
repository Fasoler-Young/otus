import unit.annotation.After;
import unit.annotation.Before;
import unit.annotation.Test;

public class TestForTest {

    @Before
    void beforeEach1() {
        System.out.println("beforeEach1");
    }

    @Before
    void beforeEach2() {
        System.out.println("beforeEach2");
    }

    @Test
    void successTest() {
        System.out.println("successTest");
    }

    @Test
    void errorTest() {
        throw new RuntimeException("errorTest");
    }

    @Test
    void successTest2() {
        System.out.println("successTest2");
    }

    @After
    void afterEach1() {
        System.out.println("afterEach1");
    }

    @After
    void afterEach2() {
        System.out.println("afterEach2");
    }
}
