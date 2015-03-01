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
	// initializing some ANSI values for text formating
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

	// the main method
	public static void main(String[] args) throws Exception {
		welcome(); // print welcome screen
		String playerName = getPlayerName(); // get the player's name
		char newGameType = introduceGame(playerName); // get the type: either open file or entering
		int[][] initMatrix = null; // the 2d array that the initial sudoku is stored in
		int[][] matrix = null; // the 2d array that the current values are stored in
		int[][] ansMatrix = null; // the 2d array that the answer of the sudoku, if any, is stored in

		if (newGameType == 'o') {
			initMatrix = reader(1); // read initial matrix
			matrix = reader(11); // read current status
			ansMatrix = reader(21); // read the answer matrix
		} else {
			initMatrix = readInConsole(); // read from user input
			matrix = new int[9][9];
			for (int i = 0; i < 9; i++) {
			  matrix[i] = Arrays.copyOf(initMatrix[i], 9);
			}
		}
		boolean solved = false; // the variable that stores whether the current sudoku is solved or not
		String[][] matrixToPrint = new String[9][9];
		solved = verifier(matrix, matrixToPrint); // verify whether the sudoku is solved or not
		printer(matrixToPrint, initMatrix); // print the current sudoku
		while (!solved) {
			replacer(matrix, initMatrix, matrixToPrint);
			solved = verifier(matrix, matrixToPrint);
		}
		clearScreen();
		printer(matrixToPrint, initMatrix);
		System.out.println();
		System.out.println();
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

	// print the introduction of the game and return the type of new game the user chooses
	public static char introduceGame(String playerName) {
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
		System.out.println("Do you want to enter a new sudoku (Enter \"n(ew)\")");
		System.out.println("or open an exisiting one? (Enter \"o(pen)\")");
		System.out.println("");
		System.out.println("");
		System.out.print(":");
		String newGameType = sc.next();

		// convert user input from string to char
		// any input not starting with "n" is considered to be opening
		if (newGameType.toLowerCase().charAt(0)=='n') {
			return 'n';
		} else {
			return 'o';
		}
	}

	// repace values at speficified row and column
	private static void replacer(int[][] matrix, int[][] initMatrix, String[][] matrixToPrint) {
		Scanner sc = new Scanner(System.in);
		// get the number of values the user wants to change
		System.out.println("\n\nEnter the number of values you want to change");
		System.out.print(":");
		int n = sc.nextInt();
		clearScreen();
		printer(matrixToPrint, initMatrix);
		System.out.println();
		System.out.println();
		for (int i=0; i<n; i++) {
			// get the row number of value the user wants to change
			System.out.println("Enter the row number of the value you want to replace");
			System.out.print(":");
			int r = sc.nextInt() - 1;
			clearScreen();
			printer(matrixToPrint, initMatrix);
			// get the column number of value the user wants to change
			System.out.println("\n\nEnter the column number of the value you want to replace");
			System.out.print(":");
			int c = sc.nextInt() - 1;
			clearScreen();
			printer(matrixToPrint, initMatrix);
			// get the new value
			System.out.println("\n\nEnter the value you want to replace \"" + matrix[r][c] + "\" with");
			System.out.print(":");
			int value = sc.nextInt();
			clearScreen();
			printer(matrixToPrint, initMatrix);
			clearScreen();
			if (initMatrix[r][c] == 0){ // if the initial value is 0, this value CAN be changed
				matrix[r][c] = value; // replace the old value with new value
				verifier(matrix, matrixToPrint);
				printer(matrixToPrint, initMatrix);
				// print the whether the change is successful
				System.out.println(ANSI_GREEN + "Success! You have changed the value." + ANSI_RESET);
			} else { // if the initial value isn't 0, this value CANNOT be changed
				verifier(matrix, matrixToPrint);
				printer(matrixToPrint, initMatrix);
				// print the whether the change is successful
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

	// read in from user input and return the matrix
	public static int[][] readInConsole() {
		int[][] matrix = new int[9][9];
		Scanner sc = new Scanner(System.in);
		// get the sudoku line by line
		for (int r=0; r<9; r++) {
			clearScreen();
			// get one line of the sudoku
			System.out.println("Enter row "+(r+1)+" of the sudoku (seperate values with a space and enter 0 for missing values)");
			System.out.print(":");
			for (int c=0; c<9; c++) {
				// get one value of the sudoku
				matrix[r][c] = sc.nextInt();
			}
		}
		return matrix;
	}

	// print the current matrix status
	public static void printer(String[][] matrixToPrint, int[][] initMatrix) {
		clearScreen();
		System.out.println("               "+"#########################");
		// print the column numbers in blue and bold
		System.out.println("               # "+"  "+ANSI_BOLD+ANSI_BLUE+"1 2 3  4 5 6  7 8 9"+ANSI_RESET+ANSI_NORMAL+" #");
		for (int r=0; r<9; r++) {
			System.out.print("               # "+ANSI_BLUE+ANSI_BOLD+(r+1)+" "+ANSI_RESET);
			for (int c=0; c<9; c++) {
				String decoration = "";
				// adding an extra space for the sub-grid
				if (c%3==2 && c!=8) {
					decoration = " ";
				}
				if (matrixToPrint[r][c].contains("X")) { // print value in red if it's wrong
					System.out.print(ANSI_RED + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else if (initMatrix[r][c]==0) { // print value in green if it's right
					System.out.print(ANSI_GREEN + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else { // print color in default color if it's an initial value
					System.out.print(matrixToPrint[r][c].charAt(0));
				}
				System.out.print(" "+decoration);
			}
			System.out.println("#");
			// print extra blank line for the sub-grid
			if (r%3==2 && r!=8) {
				System.out.println("               #                       #");
			}
		}
		System.out.println("               "+"#########################");
		System.out.println();
		// waste test code for printing out each check: row, column and block
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
		boolean solved = true; // initialize it to be true
		int[][] rotatedMatrix = new int[9][9]; // matrix rotated by 90 degrees (in order to check column)
		int[][] extractedMatrix = new int[9][9];  // sub-grids extracted to be seperate array

		// check the matrix (each row)
		for (int r=0; r<9; r++) {
			int[] row = new int[9];
			// check value at each column in the row
			for (int c=0; c<9; c++) {
				boolean check = false;
				for (int i=0; i<9; i++) {
					if (row[i]==matrix[r][c]){
						check = true;
					}
				}
				String info;

				// whenever one value is found to be duplicated (check), set info to "X" and the game is not solved
				if (check) {
					info = "X";
					solved = false;
				} else { // set info to . if it is not duplicated
					info = ".";
				}
				row[c] = matrix[r][c];
				matrixToPrint[r][c] = Integer.toString(matrix[r][c]) + info; // update info to matrixToPrint
				rotatedMatrix[c][r] = matrix[r][c]; // update values in rotatedMatrix
				int cn = r/3;
				int rn = c/3;
				int i = cn + rn * 3;
				int smallCN = r%3;
				int smallRN = c%3;
				int j = smallCN + smallRN * 3;
				extractedMatrix[i][j] = matrix[r][c]; // update values in extractedMatrix
			}
		}

		// check rotated matrix (each column)
		for (int c=0; c<9; c++) {
			int[] column = new int[9];
			// check value at each row in the column
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
					info = "."; // set info to . if it is not duplicated
				}
				column[r] = rotatedMatrix[c][r];
				matrixToPrint[r][c] = matrixToPrint[r][c] + info; // update info to matrixToPrint
			}
		}		

		//check extracted matrix (each sub-grid)
		for (int i=0; i<9; i++) {
			int[] block = new int[9];
			// check value at each sub-grid
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
					info = ". "; // set info to . if it is not duplicated
				}
				block[j] = extractedMatrix[i][j];
				int r = i%3 * 3 + j%3;
				int c = i/3 * 3 + j/3;
				matrixToPrint[r][c] = matrixToPrint[r][c] + info; // update info to matrixToPrint
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
