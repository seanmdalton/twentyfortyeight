package com.dalton.twentyfortyeight;

import java.util.Hashtable;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author github.com/seanmdalton
 *
 */
public class Board extends Composite {		

	private final int BOARD_SIZE = 4; 					// sets the row/column size
	
	private Font buttonFont; 							//global font variable
	private Hashtable<String, Color> buttonColors; 		//keeps a list of colors for the button background according to the value
	private Hashtable<String, Color> buttonFontColors; 	//keeps a list of colors for the button text according to the value
	private Button[][] board; 							//keeps all of our buttons, which make our "board"
	
	private GridLayout layout; 							//for organizing the buttons
	
	private Random random;								//for inserting random new numbers
	
	private int moves = 0;								//keep track of moves, for decision making
	
	private int points = 0;								//overall board score
		
	/**
	 * This is a required function when extending the Composite object
	 * 
	 * @param parent - the parent composite that the board is going to reside in
	 * @param style	 - the style to be applied to the board widget
	 */
	public Board(Composite parent, int style) {
		this(parent, style, SWT.NONE, SWT.BORDER);
	}

	/**
	 * @param parent
	 * @param style
	 * @param labelStyle
	 * @param textStyle
	 */
	private Board(Composite parent, int style, int labelStyle, int textStyle) {	
		super(parent, style);
		
		
		// Initialize all the things
		random = new Random();
		buttonColors = new Hashtable<String, Color>();		
		buttonFontColors = new Hashtable<String, Color>();	
		Display display = Display.getCurrent();		
		layout = new GridLayout(BOARD_SIZE, true);		
		buttonFont = new Font(display, "arial", 30, SWT.BOLD); // set the global font for all buttons
		
		this.buildColorList(display); //get the color assignments
		
		this.setLayout(layout);	//set the grid layout for the buttons
		
		this.buildBoard(); //meat and potatoes
		
		//listen for Dispose events so we can destroy all the widgets created in Board
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("DisposeListener");
				
				for(int i = 0; i > board.length; i++){
					for(int j = 0; j > board[i].length; j++){
						// get rid of all the buttons 
						
						board[i][j].dispose();
					}
				}
				
				parent.dispose(); // get rid of the parent shell
			}
		});
	}
	
	/**
	 * Sets all the colors used for the buttons, based on the values of each button. 
	 * 
	 * @param display - the global display object to pull SWT colors
	 */
	private void buildColorList(Display display){
		buttonColors.put("new", new Color(display, 255, 204, 153)); //for the newly randomly assigned buttons
		buttonColors.put("0", new Color(display, 255, 255, 255));
		buttonColors.put("2", new Color(display, 204, 229, 255));
		buttonColors.put("4", new Color(display, 153, 255, 255));
		buttonColors.put("8", new Color(display, 102, 255, 255));
		buttonColors.put("16", new Color(display, 51, 255, 255));
		buttonColors.put("32", new Color(display, 154, 204, 255));
		buttonColors.put("64", new Color(display, 102, 178, 255));
		buttonColors.put("128", new Color(display, 51, 153, 255));
		buttonColors.put("256", new Color(display, 0, 128, 255));
		buttonColors.put("512", new Color(display, 0, 102, 204));
		buttonColors.put("1024", new Color(display, 0, 76, 153));
		buttonColors.put("2048", new Color(display, 0, 102, 102));
		buttonColors.put("default", new Color(display, 0, 0, 0)); // if the points go over 2048, just use black for everything
		
		// I plan on playing around with the colors, so I'm going to leave this mess of a hash until I make a decision
		buttonFontColors.put("new", new Color(display, 128, 128, 128)); //for the newly randomly assigned buttons
		buttonFontColors.put("0", new Color(display, 128, 128, 128));
		buttonFontColors.put("2", new Color(display, 128, 128, 128));
		buttonFontColors.put("4", new Color(display, 128, 128, 128));
		buttonFontColors.put("8", new Color(display, 128, 128, 128));
		buttonFontColors.put("16", new Color(display, 128, 128, 128));
		buttonFontColors.put("32", new Color(display, 128, 128, 128));
		buttonFontColors.put("64", new Color(display, 128, 128, 128));
		buttonFontColors.put("128", new Color(display, 255, 255, 255));
		buttonFontColors.put("256", new Color(display, 255, 255, 255));
		buttonFontColors.put("512", new Color(display, 255, 255, 255));
		buttonFontColors.put("1024", new Color(display, 255, 255, 255));
		buttonFontColors.put("2048", new Color(display, 255, 255, 255));		
		buttonFontColors.put("default", new Color(display, 255, 255, 255)); // if the points go over 2048, just use black for everything
	}		
	
	/**
	 * Dumps the board to the console for debugging purposes
	 */
	@SuppressWarnings("unused")
	private void debugBoard(){
		System.out.print("\n\n\n\n\n\n\n\n\n\n");		
		
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[row].length; col++){
				System.out.print(board[row][col] + " ");
			}
			
			System.out.print("\n");
		}
	}
	
	/**
	 * Meat and potatoes. 
	 * @
	 * This will build the board (create all the buttons and put them in the grid), and then generate the first 2 values. 
	 */
	private void buildBoard(){
		board = new Button[BOARD_SIZE][BOARD_SIZE];
		
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
				
		gridData.widthHint = 100;
		gridData.heightHint = 100;		

		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j < BOARD_SIZE; j++){				
				board[i][j] = new Button(this, SWT.SINGLE | SWT.READ_ONLY
					    | SWT.BORDER | SWT.CENTER);
				board[i][j].setText(" ");
				
				board[i][j].setFont(buttonFont);
				board[i][j].setForeground(buttonFontColors.get("0"));
				board[i][j].setBackground(buttonColors.get("0"));
				
				board[i][j].setEnabled(false);
				board[i][j].setLayoutData(gridData);
			}
		}
		
		int randomAssignments = 0; //tracks the number of random assignments we make at the start
		
		while(true){
			this.addNewRandom();
			
			randomAssignments++;
			
			if(randomAssignments >= Math.ceil((BOARD_SIZE/2))){  //sets the number of randoms based on the board size
				break;
			}
		}	
	}
	
	/**
	 * Used for debugging specific use cases. Will create an exact board as dictated by testBoard. 
	 */
	@SuppressWarnings("unused")
	private void createTestBoard() {
		int[][] testBoard = new int[][]{
			{ 4, 2, 2, 0},
			{ 0, 2, 2, 0},
			{ 4, 4, 0, 4},
			{ 0, 0, 2, 0}
		};
	
		for(int i = 0; i < testBoard.length; i++) {
			for(int j = 0; j < testBoard[i].length; j++) {
				board[i][j].setText(Integer.toString(testBoard[i][j]));
				board[i][j].setBackground(buttonColors.get(Integer.toString(testBoard[i][j])));
				board[i][j].setForeground(buttonFontColors.get(Integer.toString(testBoard[i][j])));
			}
		}
	}
	
	/**
	 * Will add a random value on an available space on the board. 
	 * 
	 * @return - will return false if there's no available space
	 */
	private Boolean addNewRandom() {
		if(this.isSpaceAvailable()){ //check to make sure a spot is available
			int row = new Random().nextInt(board.length); //get a random row
			int col = new Random().nextInt(board[row].length); //get a random col
			
			int randomValue = random.nextBoolean() ? 2 : 4; //give me a random 2 or 4
			
			if(board[row][col].getText().equals(" ")){ //check to make sure we're in an empty spot
				board[row][col].setText(Integer.toString(randomValue)); //assign value
				board[row][col].setBackground(buttonColors.get("new")); //assign the "new" color to distinguish from other colors
			} else {
				//if we don't find a random spot, recursively call this function until we get something
				
				addNewRandom();
			}	
			
			return true;
		} else {
			// TODO throw exception for game over
			
			return false;
		}
	}
	
	/**
	 * Checks the entire board for a single available space
	 * 
	 * @return - true is a single space is available, false if nothing is found available
	 */
	private Boolean isSpaceAvailable(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){	
				if(board[i][j].getText().equals(" ")){
					//found something, break here and return true
					
					return true;
				}
			}
		}			
		
		return false;
	}
	
	/**
	 * Sets the enable/disable status of all buttons on the board. 
	 * 
	 * NOTE : The enable attribute is used to limit collapsing numbers to a single collapse. 
	 * 			e.g. : 2,2,2,2 -> 0,0,4,4 instead of 2,2,2,2 -> 0,0,0,8
	 * 
	 * @param enable - true to enable, false to disable
	 */
	private void setEnableButtons(Boolean enable){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				board[i][j].setEnabled(enable);				
			}
		}		
	}	
	
	/**
	 * Get the global score for the current board
	 * 
	 * @return - total points for this board
	 */
	public int getPoints() {
		return points;
	}

	public void swipeUp() throws GameOverException {
		System.out.println("Up");
		
		moves = 0;

		setEnableButtons(true);
		
		for(int col = 0; col < board[0].length; col++){					
			for(int row = 0; row < board.length; row++){					
				compareCellsUp(row, col, 1);
			}			
		}
		
		setEnableButtons(false);
		
		if(moves > 0){
			if(!this.addNewRandom()){
				throw new GameOverException("No Available Space");
			}	
		}		
	}
	
	private void compareCellsUp(int row, int col, int direction) {		
		if(row >= 0 && row < board.length - 1) {
			int currentValue = board[row][col].getText().equals(" ") ? 0 :
				Integer.parseInt(board[row][col].getText());					

			int nextValue = board[row + direction][col].getText().equals(" ") ? 0 :
							Integer.parseInt(board[row + direction][col].getText());
			
			if(currentValue == nextValue && currentValue > 0 && 
					(board[row][col].isEnabled() && board[row + direction][col].isEnabled())){
				currentValue += nextValue;
				
				nextValue = 0;
				
				board[row][col].setEnabled(false);
				board[row + direction][col].setEnabled(false);				
				
				points += currentValue;
				
				moves++;
				
			} else if (currentValue == 0 && nextValue != 0) {
				currentValue = nextValue;
				nextValue = 0;
				
				moves++;
			}
			
			board[row][col].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
			board[row + direction][col].setText(nextValue == 0 ? " " : Integer.toString(nextValue));		
			
			board[row][col].setBackground(buttonColors.get(Integer.toString(currentValue)));
			board[row][col].setForeground(buttonFontColors.get(Integer.toString(currentValue)));
			
			board[row + direction][col].setBackground(buttonColors.get(Integer.toString(nextValue)));
			board[row + direction][col].setForeground(buttonFontColors.get(Integer.toString(nextValue)));
			
			//go back the stack in the opposite direction until we can't make any more moves
			compareCellsUp(row - direction, col, direction);
		}	
	}

	public void swipeDown() throws GameOverException {
		moves = 0;
		
		setEnableButtons(true);
		
		for(int row = board.length - 1; row >= 0; row--) {
			for(int col = 0; col < board[row].length; col++){
				compareCellsDown(row, col, -1);
			}
		}
		
		setEnableButtons(false);
		
		if(moves > 0){
			if(!this.addNewRandom()){
				throw new GameOverException("No Available Space");
			}	
		}
	}
	
	private void compareCellsDown(int row, int col, int direction){
		if(row > 0 && row <= board.length - 1) {
			int currentValue = board[row][col].getText().equals(" ") ? 0 :
				Integer.parseInt(board[row][col].getText());					

			int nextValue = board[row + direction][col].getText().equals(" ") ? 0 :
							Integer.parseInt(board[row + direction][col].getText());
			
			if(currentValue == nextValue && currentValue > 0 && 
					(board[row][col].isEnabled() && board[row + direction][col].isEnabled())){
								
				currentValue += nextValue;
				nextValue = 0;
				
				//use the button enable/disable functionality to eliminate consecutive merges
				board[row][col].setEnabled(false);
				board[row + direction][col].setEnabled(false);
				
				points += currentValue;
				
				moves++;
			} else if (currentValue == 0 && nextValue != 0){
				currentValue = nextValue;
				nextValue = 0;			
				
				moves++;
			}
			
			board[row][col].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
			board[row + direction][col].setText(nextValue == 0 ? " " : Integer.toString(nextValue));	
			
			board[row][col].setBackground(buttonColors.get(Integer.toString(currentValue)));
			board[row][col].setForeground(buttonFontColors.get(Integer.toString(currentValue)));
			
			board[row + direction][col].setBackground(buttonColors.get(Integer.toString(nextValue)));
			board[row + direction][col].setForeground(buttonFontColors.get(Integer.toString(nextValue)));
			
			this.compareCellsDown(row + 1, col, direction);
		}	
	}	

	public void swipeLeft() throws GameOverException {
		moves = 0;

		setEnableButtons(true);
		
		for(int col = 0; col < board[0].length; col++){					
			for(int row = 0; row < board.length; row++){				
				compareCellsLeft(row, col, 1);
			}			
		}
		
		setEnableButtons(false);
		
		if(moves > 0){
			if(!this.addNewRandom()){
				throw new GameOverException("No Available Space");
			}	
		}
	}
	
	private void compareCellsLeft(int row, int col, int direction){		
		if(col >= 0 && col < board[row].length - 1) {
			int currentValue = board[row][col].getText().equals(" ") ? 0 :
				Integer.parseInt(board[row][col].getText());					

			int nextValue = board[row][col + direction].getText().equals(" ") ? 0 :
							Integer.parseInt(board[row][col + direction].getText());
			
			if(currentValue == nextValue && currentValue > 0 && 
					(board[row][col].isEnabled() && board[row][col + direction].isEnabled())){
								
				currentValue += nextValue;
				nextValue = 0;
				
				//use the button enable/disable functionality to eliminate consecutive merges
				board[row][col].setEnabled(false);
				board[row][col + direction].setEnabled(false);
				
				points += currentValue;
				
				moves++;
			} else if (currentValue == 0 && nextValue != 0){
				currentValue = nextValue;
				nextValue = 0;			
				
				moves++;
			}
			
			board[row][col].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
			board[row][col + direction].setText(nextValue == 0 ? " " : Integer.toString(nextValue));	
			
			board[row][col].setBackground(buttonColors.get(Integer.toString(currentValue)));
			board[row][col].setForeground(buttonFontColors.get(Integer.toString(currentValue)));
			
			board[row][col + direction].setBackground(buttonColors.get(Integer.toString(nextValue)));	
			board[row][col + direction].setForeground(buttonFontColors.get(Integer.toString(nextValue)));
			
			this.compareCellsLeft(row, col - direction, direction);
		}	
	}	
	
	public void swipeRight() throws GameOverException {
		moves = 0;
		
		setEnableButtons(true);

		for(int row = 0; row < board.length; row++) {
			for(int col = board[row].length - 1; col > 0; col--){
				compareCellsRight(row, col, -1);
			}
		}

		setEnableButtons(false);
		
		if(moves > 0){
			if(!this.addNewRandom()){
				throw new GameOverException("No Available Space");
			}		
		}
	}

	private void compareCellsRight(int row, int col, int direction){
		if(col > 0 && col < board[row].length) {
			int currentValue = board[row][col].getText().equals(" ") ? 0 :
				Integer.parseInt(board[row][col].getText());					

			int nextValue = board[row][col + direction].getText().equals(" ") ? 0 :
							Integer.parseInt(board[row][col + direction].getText());		
			
			if(currentValue == nextValue && currentValue > 0 && 
					(board[row][col].isEnabled() && board[row][col  + direction].isEnabled())){
								
				currentValue += nextValue;
				nextValue = 0;
				
				//use the button enable/disable functionality to eliminate consecutive merges
				board[row][col].setEnabled(false);
				board[row][col + direction].setEnabled(false);
				
				points += currentValue;
				
				moves++;
			} else if (currentValue == 0 && nextValue != 0){
				currentValue = nextValue;
				nextValue = 0;			
				
				moves++;
			}
			
			board[row][col].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
			board[row][col + direction].setText(nextValue == 0 ? " " : Integer.toString(nextValue));	
			
			board[row][col].setBackground(buttonColors.get(Integer.toString(currentValue)));
			board[row][col].setForeground(buttonFontColors.get(Integer.toString(currentValue)));
			
			board[row][col + direction].setBackground(buttonColors.get(Integer.toString(nextValue)));	
			board[row][col + direction].setForeground(buttonFontColors.get(Integer.toString(nextValue)));
			
			
			this.compareCellsRight(row, col - direction, direction);
		}		
	}	
}
