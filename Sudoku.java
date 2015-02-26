import java.util.Arrays;
import java.util.Scanner;

public class Sudoku {
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static void main(String[] args) {
		int[][] matrix = reader();
		boolean solved = false;
		String[][] matrixToPrint = new String[matrix.length][matrix[0].length];
		solved = verifier(matrix, matrixToPrint);
		printer(matrixToPrint);
		while (!solved) {
			replacer(matrix);
			solved = verifier(matrix, matrixToPrint);
			printer(matrixToPrint);
		}
		System.out.println("Congratulations, you solved it!");
	}
	
	private static void replacer(int[][] matrix) {
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter the number of values you want to change: ");
		int n = sc.nextInt();
		for (int i=0; i<n; i++) {
			System.out.print("Enter the row number of the value you want to replace: ");
			int r = sc.nextInt() - 1;
			System.out.print("Enter the column number of the value you want to replace: ");
			int c = sc.nextInt() - 1;
			System.out.print("Enter the value you want to replace \"" + matrix[r][c] + "\" with: ");
			int value = sc.nextInt();
			matrix[r][c] = value;
			System.out.println("Success!");
		}
	}

	private static int[][] reader() {
		int[][] matrix = new int[9][9];
		Scanner sc = new Scanner(System.in); 
		System.out.println("Enter the values row by row, seperate values with a space and enter 0 for missing values.");
		for (int r=0; r<matrix.length; r++) {
			for (int c=0; c<matrix[r].length; c++) {
				matrix[r][c] = sc.nextInt();
			}
		}
		return matrix;
	}

	private static void printer(String[][] matrixToPrint) {
		System.out.println();
		for (int r=0; r<matrixToPrint.length; r++) {
			for (int c=0; c<matrixToPrint[r].length; c++) {
				String decoration = "";
				if (c%3==2 && c!=8) {
					decoration = "| ";
				}
				if (matrixToPrint[r][c].contains("X")) {
					System.out.print(ANSI_RED + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else {
					System.out.print(matrixToPrint[r][c].charAt(0));
				}
				System.out.print(" "+decoration);
			}
			System.out.println();
			if (r%3==2 && r!=8) {
				System.out.println("––––––+–-–––––+––––––");
			}
		}
		System.out.println();
		for (int r=0; r<matrixToPrint.length; r++) {
			for (int c=0; c<matrixToPrint[r].length; c++) {
				int row = r + 1;
				int column = c + 1;
				if (matrixToPrint[r][c].charAt(0)=='0') {
					System.out.println("The value at row "+row+" column "+column+" is missing.");
				} else {
					if (matrixToPrint[r][c].charAt(1)=='X') {
						System.out.println("The value at row "+row+" column "+column+" is duplicated in the row.");
					}		
					if (matrixToPrint[r][c].charAt(2)=='X') {
						System.out.println("The value at row "+row+" column "+column+" is \nduplicated in the column.");
					}
					if (matrixToPrint[r][c].charAt(3)=='X') {
						System.out.println("The value at row "+row+" column "+column+" is \nduplicated in the block.");
					}
				}
			}

		}
	}

	private static boolean verifier(int[][] matrix, String[][] matrixToPrint) {
		boolean solved = true;
		int[][] rotatedMatrix = new int[matrix[0].length][matrix.length];
		int[][] extractedMatrix = new int[9][9];
		for (int r=0; r<matrix.length; r++) {
			int[] row = new int[matrix[r].length];
			for (int c=0; c<matrix[r].length; c++) {
				boolean check = false;
				for (int i=0; i<matrix[r].length; i++) {
					if (row[i]==matrix[r][c]){
						check = true;
					}
				}
				String info;
				if (check) {
					info = "X";
					solved = false;
				} else {
					info = ".";
				}
				row[c] = matrix[r][c];
				matrixToPrint[r][c] = Integer.toString(matrix[r][c]) + info;
				rotatedMatrix[c][r] = matrix[r][c];
				int cn = r/3;
				int rn = c/3;
				int i = cn + rn * 3;
				int smallCN = r%3;
				int smallRN = c%3;
				int j = smallCN + smallRN * 3;
				extractedMatrix[i][j] = matrix[r][c];
			}
		}

		// check rotated matrix
		for (int c=0; c<rotatedMatrix.length; c++) {
			int[] column = new int[rotatedMatrix[c].length];
			for (int r=0; r<rotatedMatrix[c].length; r++) {
				boolean check = false;
				for (int i=0; i<rotatedMatrix[c].length; i++) {
					if (column[i]==rotatedMatrix[c][r]){
						check = true;
					}
				}
				String info;
				if (check) {
					info = "X";
					solved = false;
				} else {
					info = ".";
				}
				column[r] = rotatedMatrix[c][r];
				matrixToPrint[r][c] = matrixToPrint[r][c] + info;
			}
		}		

		//check extracted matrix
		for (int i=0; i<extractedMatrix.length; i++) {
			int[] block = new int[extractedMatrix[i].length];
			for (int j=0; j<extractedMatrix[i].length; j++) {
				boolean check = false;
				for (int k=0; k<extractedMatrix[i].length; k++) {
					if (block[k]==extractedMatrix[i][j]){
						check = true;
					}
				}
				String info;
				if (check) {
					info = "X ";
					solved = false;
				} else {
					info = ". ";
				}
				block[j] = extractedMatrix[i][j];
				int r = i%3 * 3 + j%3;
				int c = i/3 * 3 + j/3;
				matrixToPrint[r][c] = matrixToPrint[r][c] + info;
			}
		}
		return solved;
	}

}
