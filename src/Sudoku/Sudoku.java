package Sudoku;
import java.awt.*;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");

	public static final int[][] myGrid = Sudoku.stringsToGrid(
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0");
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	private int[][] board;
	private ArrayList<Spot> spots;
	private int numEmptySpots;
	private long timeElapsed;
	private int numSolutions = 0;
	private String ansText = "";

	public class Spot implements  Comparable<Spot> {
		private int x;
		private int y;
		int value;
		private HashSet<Integer> possibleNums;

		public Spot(int x, int y, int value) {
			this.x = x;
			this.y = y;
			this.value = value;
			possibleNums = new HashSet<>();
			getPossibleNums();
		}

		public void set(int value) {
			board[x][y] = value;
		}

		@Override
		public int compareTo(Spot toCompare) {
			return this.possibleNums.size() - toCompare.possibleNums.size();
		}

		public void getPossibleNums() {
			possibleNums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			for (int i = 0; i < SIZE; i++) {
				possibleNums.remove(board[x][i]);
				possibleNums.remove(board[i][y]);
				checkSquare();
			}
		}

		private void checkSquare() {
			int x0 = (x / 3) * 3;
			int y0 = (y / 3) * 3;
			for (int i = 0; i < PART; i++) {
				for (int j = 0; j < PART; j++) {
					possibleNums.remove(board[x0+i][y0+j]);
				}
			}
		}

	}

	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}

	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
		System.out.println(sudoku);
	}
	
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		board = new int[SIZE][SIZE];
		spots = new ArrayList<>();
		numEmptySpots = 0;
		timeElapsed = 0;
		numSolutions = 0;

		for(int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				board[i][j] = ints[i][j];
			}
		}
	}

	public  Sudoku(String text) {
		this(textToGrid(text));
	}


	@Override
	public String toString() {
		String res = "";
		for(int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				res +=(String.valueOf(board[i][j]) + " ");
			}
			res+= "\n";
		}
		return res;
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long start =  System.currentTimeMillis();
		getEmptySpots();
		numEmptySpots = spots.size();
		Collections.sort(spots);
		solveRec(0);
		long end =  System.currentTimeMillis();
		timeElapsed = end - start;
		return numSolutions;
	}


	private void solveRec(int spotIndex) {
		if (spotIndex >= numEmptySpots) {
			if (numSolutions == 0)
				ansText = toString();
			numSolutions++;
		} else {
			Spot curr = spots.get(spotIndex);
			curr.getPossibleNums();
			for (int possNum : curr.possibleNums) {
				//choose
				curr.set(possNum);

				//explore
				solveRec(spotIndex + 1);

				//unchoose
				curr.set(0);

				if (numSolutions >= MAX_SOLUTIONS)
					return;
			}
		}
	}

	private void getEmptySpots() {
		for(int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if(board[i][j] == 0)
					spots.add(new Spot(i, j, board[i][j]));
			}
		}
	}


	public String getSolutionText() {
		return ansText;
	}
	
	public long getElapsed() {
		return timeElapsed;
	}

}
