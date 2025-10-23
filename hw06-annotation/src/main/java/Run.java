import java.lang.reflect.InvocationTargetException;
import unit.MyUnitFramework;

public class Run {
    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        MyUnitFramework.runAllTests(TestForTest.class);
    }
}
