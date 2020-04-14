public class Homework6 {

    public int[] arrayAfterFour(int[] array){
        int index = -1;
        for (int i = 0; i < array.length; i++){
            if(array[i] == 4){
                index = i;
            }
        }
        if (index == -1){
            throw new RuntimeException();
        }
        int count = 0;
        for (int i = index; i < array.length; i++){
            count++;
        }
        int[] arrayAfterFour = new int[count];
        for (int i = index, j = 0; i < array.length; i++, j++){
            arrayAfterFour[j] = array[i];
        }
        return arrayAfterFour;
    }

    // под этим методом - метод для проверки наличия любого числа или массива чисел в массиве
    public boolean checkAvailabilityOneAndFour (int[] array){
        boolean[] checks = new boolean[2];
        for (int value : array) {
            if (value == 1) {
                checks[0] = true;
            }
            if (value == 4){
                checks[1] = true;
            }
        }
        for (boolean b : checks) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAvailabilityForDifferentNumbers (int[] array, int[] numbers){
        boolean[] checks = new boolean[numbers.length];
        for(int i = 0; i < numbers.length; i++){
            for(int j : array){
                if (numbers[i] == j) {
                    checks[i] = true;
                }
            }
        }
        for (boolean b : checks) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
