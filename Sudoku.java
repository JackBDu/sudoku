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
	public static final String ANSI_HIGHLIGHT = "\033[43m";

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
		int[] currentPos = {0, 0};
		verifier(matrix, matrixToPrint); // verify whether the sudoku is solved or not
		printer(matrixToPrint, initMatrix, currentPos); // print the current sudoku
		handler(matrix, initMatrix, ansMatrix, matrixToPrint, currentPos);
		clearScreen();
		printer(matrixToPrint, initMatrix, currentPos);
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
		System.out.println("");
		System.out.println("Do you want to enter a new sudoku (Enter \"(N)ew\")");
		System.out.println("or open an exisiting one? (Enter \"(O)pen\")");
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

	// handle navigation and replacement
	private static void handler(int[][] matrix, int[][] initMatrix, int[][] ansMatrix, String[][] matrixToPrint, int[] currentPos) {
		Scanner sc = new Scanner(System.in);
		int row;
		int column;
		boolean solved = false;
		String statusBar = "New game loaded";
		while (!solved) {
			row = currentPos[0];
			column = currentPos[1];
			System.out.println(statusBar);
			System.out.println("(L)eft (R)ight (U)p (D)own (A)nswer (Q)uit");
			System.out.println("Enter a value to replace");
			System.out.print(":");
			String input = sc.next();
			// moving heighting according to input
			if (input.toLowerCase().charAt(0) == 'l') {
				column--;
				statusBar = "Moved left";
			} else if (input.toLowerCase().charAt(0) == 'r') {
				column++;
				statusBar = "Moved right";
			} else if (input.toLowerCase().charAt(0) == 'u') {
				row--;
				statusBar = "Moved down";
			} else if (input.toLowerCase().charAt(0) == 'd') {
				row++;
				statusBar = "Moved left";
			} else if (input.toLowerCase().charAt(0) == 'a') {
				int value = ansMatrix[row][column];
				if (initMatrix[row][column] == 0){ // if the initial value is 0, this value CAN be changed
					matrix[row][column] = value; // replace the old value with new value
					solved = verifier(matrix, matrixToPrint);
					if (solved) { // return if it's solved
						currentPos[0] = 9;
						return;
					}
				}
				column++;
				statusBar = ANSI_GREEN+"Answer displayed"+ANSI_RESET;
			} else if (input.toLowerCase().charAt(0) == 'q') {
				statusBar = ANSI_RED+"Game quit"+ANSI_RESET;
				System.exit(0);
			} else { // changing a value
				int value = Integer.parseInt(input);
				if (initMatrix[row][column] == 0){ // if the initial value is 0, this value CAN be changed
					matrix[row][column] = value; // replace the old value with new value
					solved = verifier(matrix, matrixToPrint);
					if (solved) { // return if it's solved
						currentPos[0] = 9;
						return;
					}
					statusBar = ANSI_GREEN+"Value changed"+ANSI_RESET;
				} else {
					statusBar = ANSI_BOLD+ANSI_RED+"Intial value cannot be changed!"+ANSI_RESET;
				}
				column++;
			}
			positionVerifier(row, column, currentPos); // avoid currentPos going out of boundary
			clearScreen();
			printer(matrixToPrint, initMatrix, currentPos);
		}
	}

	public static void positionVerifier(int r, int c,int[] currentPos) {
		if (c >= 9 && r<8) { // if column number out of right boundary, move to next row
			c = 0;
			r++;
		} else if (c >=9 && r>=9) { // if it's at the bottom right corner, it stays there
			c = 8;
		} else if (c < 0 && r>0) { // if column number out of left boundary, move to previous row
			c = 8;
			r--;
		} else if (c < 0 && r<=0) { // if it's at the top left corner, it stays there
			c = 0;
		} else if (r >= 9) { // limit row number in boundary at bottom
			r = 8;
		} else if (r < 0) { // limit row number in boundary at top
			r = 0;
		}
		currentPos[0] = r;
		currentPos[1] = c;
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
	public static void printer(String[][] matrixToPrint, int[][] initMatrix, int[] currentPos) {
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
				if (currentPos[0] == r && currentPos[1] == c) {
					System.out.print(ANSI_HIGHLIGHT);
				}
				if (matrixToPrint[r][c].contains("X") && initMatrix[r][c]==0) { // print value in red if it's duplicated and not initial
					System.out.print(ANSI_RED + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else if (initMatrix[r][c]==0) { // print value in green if it's not duplicated
					System.out.print(ANSI_GREEN + matrixToPrint[r][c].charAt(0) + ANSI_RESET);
				} else { // print color in default color if it's an initial value
					System.out.print(matrixToPrint[r][c].charAt(0) + ANSI_RESET);
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
	}

	// verify if it is solved
	public static boolean verifier(int[][] matrix, String[][] matrixToPrint) {
		boolean solved = true; // initialize it to be true
		int[][] rotatedMatrix = new int[9][9]; // matrix rotated by 90 degrees (in order to check column)
		int[][] extractedMatrix = new int[9][9];  // sub-grids extracted to be seperate array

		// check the matrix (each row)
		for (int r=0; r<9; r++) {
			// check value at each column in the row
			for (int c=0; c<9; c++) {
				boolean duplicated = false;
				for (int i=0; i<9; i++) {
					if (matrix[r][i]==matrix[r][c] && i!=c){
						duplicated = true;
					}
				}
				String info;

				// whenever one value is found to be duplicated, set info to "X" and the game is not solved
				if (duplicated) {
					info = "X";
					solved = false;
				} else { // set info to . if it is not duplicated
					info = ".";
				}
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
			// check value at each row in the column
			for (int r=0; r<9; r++) {
				boolean duplicated = false;
				for (int i=0; i<9; i++) {
					if (rotatedMatrix[c][i]==rotatedMatrix[c][r] && i!=r){
						duplicated = true;
					}
				}
				String info;
				if (duplicated) {
					info = "X";
					solved = false;
				} else {
					info = "."; // set info to . if it is not duplicated
				}
				matrixToPrint[r][c] = matrixToPrint[r][c] + info; // update info to matrixToPrint
			}
		}		

		//check extracted matrix (each sub-grid)
		for (int i=0; i<9; i++) {
			// check value at each sub-grid
			for (int j=0; j<9; j++) {
				boolean duplicated = false;
				for (int k=0; k<9; k++) {
					if (extractedMatrix[i][k]==extractedMatrix[i][j] && k!=j){
						duplicated = true;
					}
				}
				String info;
				if (duplicated) {
					info = "X ";
					solved = false;
				} else {
					info = ". "; // set info to . if it is not duplicated
				}
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
