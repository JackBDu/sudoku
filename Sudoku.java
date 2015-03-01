/*
 * Sudoku.java
 *
 * Created on Febuary 12, 2015
 *
 * Copyright(c) {2015} Jack B. Du (Jiadong Du) All Rights Reserved.
 *
 */

import java.util.Arrays;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

/*
 * @ version 0.0.1
 * @ author Jack B. Du (Jiadong Du)
 */


public class Sudoku {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_GRAY = "\u001B[37m";
	public static final String ANSI_WHITE = "\u001B[37;1m"; 
	public static final String ANSI_BOLD = "\033[1m";
	public static final String ANSI_LIGHT = "\033[2m";
	public static final String ANSI_UNDERLINE = "\033[4m";
	public static final String ANSI_NORMAL = "\033[0m";
	public static void main(String[] args) throws Exception {
		welcome();
		String playerName = getPlayerName();
		introduceGame(playerName);
		int[][] initMatrix = reader(1); // read initial matrix
		int[][] matrix = reader(11); // read current status
		int[][] ansMatrix = reader(21); // read the answer matrix
		boolean solved = false;
		String[][] matrixToPrint = new String[9][9];
		solved = verifier(matrix, matrixToPrint);
		printer(matrixToPrint, initMatrix);
		while (!solved) {
			replacer(matrix, initMatrix);
			solved = verifier(matrix, matrixToPrint);
			printer(matrixToPrint, initMatrix);
		}
		System.out.println("Congratulations, you solved it!");
	}

	/* 
	 * print welcome screen, containing game name, version and author name
	 * and resizing instructions
	 * ascii arts are generted using
	 * http://patorjk.com/software/taag/
	 * http://www.network-science.de/ascii/
	 */
	
	public static void welcome() {
		for (int i = 0; i<100; i++) {
			System.out.println();
		}
		clearScreen();
		System.out.println("Resize the window width so that this line is in one line");
		System.out.println("                                     but this line in two");
		System.out.println("Resize the window height so that you CAN'T see this line");
		System.out.println("                               but you CAN see this line");
		System.out.println("  _______  __   __  ______   _______  ___   _  __   __ ");
		System.out.println(" |       ||  | |  ||      | |       ||   | | ||  | |  |");
		System.out.println(" |  _____||  | |  ||  _    ||   _   ||   |_| ||  | |  |");
		System.out.println(" | |_____ |  |_|  || | |   ||  | |  ||      _||  |_|  |");
		System.out.println(" |_____  ||       || |_|   ||  |_|  ||     |_ |       |");
		System.out.println("  _____| ||       ||       ||       ||    _  ||       |");
		System.out.println(" |_______||_______||______| |_______||___| |_||_______|");
		System.out.println("       _   __            _             ___   ___   ___");
		System.out.println("      | | / /__ _______ (_)__  ___    / _ \\ / _ \\ <  /");
		System.out.println("      | |/ / -_) __(_-</ / _ \\/ _ \\  / // // // / / / ");
		System.out.println("      |___/\\__/_/ /___/_/\\___/_//_/  \\___(_)___(_)_/  ");
		System.out.println();
		System.out.println("      _              _         _     ___     ___       ");
		System.out.println("     | |__ _  _   _ | |__ _ __| |__ | _ )   |   \\ _  _ ");
		System.out.println("     | '_ \\ || | | || / _` / _| / / | _ \\_  | |) | || |");
		System.out.println("     |_.__/\\_, |  \\__/\\__,_\\__|_\\_\\ |___(_) |___/ \\_,_|");
		System.out.println("           |__/                                        ");
		System.out.println(ANSI_RED+"Please resize the console window according to the instructions at the top to get the best gaming experience!"+ANSI_RESET);
	}

	// get player name and return it
	public static String getPlayerName() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String playerName = sc.next();
		return playerName;
	}

	public static void introduceGame(String playerName) {
		Scanner sc = new Scanner(System.in);
		clearScreen();
		System.out.println("Hello, "+playerName+"!");
		System.out.println("");
		System.out.println("Welcome to Sudoku!");
		System.out.println("");
		System.out.println("");
		System.out.println("   /###############################################\\");
		System.out.println("  (                                                 )");
		System.out.println("  ( "+ANSI_BOLD+"Sudoku"+ANSI_NORMAL+" is a puzzle where you need to fill a 9×9"+" )");
		System.out.println("  ( "+"grid with digits so that each column, each row,"+" )");
		System.out.println("  ( "+"and each of the nine 3×3 sub-grids that compose"+" )");
		System.out.println("  ( "+"the grid contains ALL of the digits from 1 to 9"+" )");
		System.out.println("  (                                                 )");
		System.out.println("   \\###############################################/");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		sc.next();
	}

	// repace values at speficified row and column
	private static void replacer(int[][] matrix, int[][] initMatrix) {
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
			if (initMatrix[r][c] == 0){ // if the initial value is 0, this value CAN be changed
				matrix[r][c] = value;
				System.out.println(ANSI_GREEN + "Success! You have changed the value." + ANSI_RESET);
			} else { // if the initial value isn't 0, this value CANNOT be changed
				System.out.println(ANSI_BOLD+ANSI_RED + "Error! You cannot change this value." + ANSI_RESET);
			}
			System.out.println();
		}
	}

	// read from file begin with n-th line and return the matrix
	public static int[][] reader(int n) throws Exception {
		int[][] matrix = new int[9][9];
		try {
			FileReader fr = new FileReader("sudoku001.dat");
			BufferedReader br = new BufferedReader(fr);
			System.out.println();
			// skip the lines before n-th line
			for (int i = 0; i < n-1; i++) {
				br.readLine();
			}
			// read the 9 lines starting with n-th line
			for (int i = 0; i < 9; i++) {
				String[] row = br.readLine().split("\\s");
				for (int j = 0; j < 9; j++) {
					matrix[i][j] = Integer.parseInt(row[j]);	
				}
			}
		} catch (Exception e) {
			matrix = null;
		}

		return matrix;
	}

	// read from system input and return the matrix
	// private static int[][] reader() {
	// 	int[][] matrix = new int[9][9];
	// 	Scanner sc = new Scanner(System.in); 
	// 	System.out.println("Enter the values row by row, seperate values with a space and enter 0 for missing values.");
	// 	for (int r=0; r<9; r++) {
	// 		for (int c=0; c<9; c++) {
	// 			matrix[r][c] = sc.nextInt();
	// 		}
	// 	}
	// 	return matrix;
	// }

	// print the current matrix status
	private static void printer(String[][] matrixToPrint, int[][] initMatrix) {
		System.out.println("                  "+"  "+ANSI_BOLD+ANSI_BLUE+"1 2 3  4 5 6  7 8 9"+ANSI_RESET+ANSI_NORMAL);
		// System.out.println();
		for (int r=0; r<9; r++) {
			System.out.print("                  "+ANSI_BLUE+ANSI_BOLD+(r+1)+" "+ANSI_RESET);
			for (int c=0; c<9; c++) {
				String decoration = "";
				if (c%3==2 && c!=8) {
					// decoration = "| ";
					decoration = " ";
				}
				if (matrixToPrint[r][c].contains("X")) {
					System.out.print(ANSI_RED + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else if (initMatrix[r][c]==0) {
					System.out.print(ANSI_GREEN + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else {
					System.out.print(matrixToPrint[r][c].charAt(0));
				}
				System.out.print(" "+decoration);
			}
			System.out.println();
			if (r%3==2 && r!=8) {
				// System.out.println("––––––+–––––––+––––––"); 
				System.out.println();
			}
		}
		System.out.println();
		// for (int r=0; r<9; r++) {
		// 	for (int c=0; c<9; c++) {
		// 		int row = r + 1;
		// 		int column = c + 1;
		// 		if (matrixToPrint[r][c].charAt(0)=='0') {
		// 			System.out.println("The value at row "+row+" column "+column+" is missing.");
		// 		} else {
		// 			if (matrixToPrint[r][c].charAt(1)=='X') {
		// 				System.out.println("The value at row "+row+" column "+column+" is duplicated in the row.");
		// 			}		
		// 			if (matrixToPrint[r][c].charAt(2)=='X') {
		// 				System.out.println("The value at row "+row+" column "+column+" is \nduplicated in the column.");
		// 			}
		// 			if (matrixToPrint[r][c].charAt(3)=='X') {
		// 				System.out.println("The value at row "+row+" column "+column+" is \nduplicated in the block.");
		// 			}
		// 		}
		// 	}

		// }
	}

	// verify if it is solved
	private static boolean verifier(int[][] matrix, String[][] matrixToPrint) {
		boolean solved = true;
		int[][] rotatedMatrix = new int[9][9];
		int[][] extractedMatrix = new int[9][9];
		for (int r=0; r<9; r++) {
			int[] row = new int[9];
			for (int c=0; c<9; c++) {
				boolean check = false;
				for (int i=0; i<9; i++) {
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
		for (int c=0; c<9; c++) {
			int[] column = new int[9];
			for (int r=0; r<9; r++) {
				boolean check = false;
				for (int i=0; i<9; i++) {
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
		for (int i=0; i<9; i++) {
			int[] block = new int[9];
			for (int j=0; j<9; j++) {
				boolean check = false;
				for (int k=0; k<9; k++) {
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

	// clear the screen
	public static void clearScreen() {
		System.out.print("\u001b[2J");
		System.out.flush();
	}

}
