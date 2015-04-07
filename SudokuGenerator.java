import java.util.Random;

public class SudokuGenerator {
	public static void main(String[] args) {
		int[][] matrix = new int[9][9];
		int[] numberSet = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		shuffleArray(numberSet);
		for (int j=0; j<9; j++) {
			int[0][j] = numberSet[j];
		}
	}

	public static void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
}
