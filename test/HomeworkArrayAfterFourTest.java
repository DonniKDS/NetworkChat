import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HomeworkArrayAfterFourTest {

    @Parameterized.Parameters
    public static Collection<int[][]> data() {
        int[] array1 = {1, 2, 4, 5, 4, 8, 4, 3};
        int[] array2 = {4, 3};
        int[] array3 = {2, 3, 2, 4, 4, 4, 4, 4};
        int[] array4 = {4};
        int[] array5 = {4, 5, 6, 2, 3, 1, 6, 2};
        int[] array6 = {4, 5, 6, 2, 3, 1, 6, 2};
        int[] array7 = {2, 5, 6, 2, 1, 5, 3, 1};
        int[] array8 = {};

        return Arrays.asList(new int[][][] {
                {array1, array2},
                {array3, array4},
                {array5, array6},
                {array7, array8}
        });
    }

    private int[] input, output;

    public HomeworkArrayAfterFourTest(int[] input, int[] output) {
        this.input = input;
        this.output = output;
    }

    private Homework6 home;

    @Before
    public void init() {
        home = new Homework6();
    }

    @Test
    public void massTestArrayAfterFour() {
        Assert.assertArrayEquals(output, home.arrayAfterFour(input));
    }

}
