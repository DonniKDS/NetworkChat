import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HomeworkCheckAvailabilityOneAndFourTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 2, 4, true},
                {2, 3, 4, false},
                {1, 6, 2, false},
                {3, 6, 2, false}
        });
    }

    private int[] input;
    private boolean check;

    public HomeworkCheckAvailabilityOneAndFourTest(int a, int b, int c, boolean check) {
        this.input = new int[]{a, b, c};
        this.check = check;
    }

    private Homework6 home;

    @Before
    public void init() {
        home = new Homework6();
    }

    @Test
    public void massTestArrayAfterFour() {
        Assert.assertEquals(check, home.checkAvailabilityOneAndFour(input));
    }
}
