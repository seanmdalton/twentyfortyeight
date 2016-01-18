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
 * @author git.com/seanmdalton
 *
 */
public class Board extends Composite {		

	private final int BOARD_SIZE = 4; // sets the row/column size
	
	private Font buttonFont;
	private Hashtable<String, Color> buttonColors;
	private Button[][] board;
	
	private GridLayout layout;
	
	private Random random;
	
	private int moves = 0;
	
	private int points = 0;
		
	public Board(Composite parent, int style) {
		this(parent, style, SWT.NONE, SWT.BORDER);
	}

	private Board(Composite parent, int style, int labelStyle, int textStyle) {	
		super(parent, style);
		
		random = new Random();
		buttonColors = new Hashtable<String, Color>();
		
		Display display = Display.getCurrent();
		
		layout = new GridLayout(BOARD_SIZE, true);
		
		buttonFont = new Font(display, "arial", 30, SWT.BOLD);
		
		this.buildColorList(display);
		
		this.setBackground(buttonColors.get("2048"));
		
		this.setLayout(layout);		
		
		this.buildBoard();
//		this.drawBoard();
		
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.out.println("DisposeListener");
				
				for(int i = 0; i > board.length; i++){
					for(int j = 0; j > board[i].length; j++){
						// get rid of all the buttons 
						
						board[i][j].dispose();
					}
				}
				
				parent.dispose();
			}
		});
	}
	
	private void buildColorList(Display display){
		buttonColors.put("new", new Color(display, 240, 248, 255));
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
	}
	
	public void drawBoard(){
		System.out.print("\n\n\n\n\n\n\n\n\n\n");		
		
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j] + " ");
			}
			
			System.out.print("\n");
		}
	}
	
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
				board[i][j].setBackground(buttonColors.get("0"));
				
				board[i][j].setEnabled(false);
				board[i][j].setLayoutData(gridData);
			}
		}
		
		Boolean assignRandom = true;
		int randomAssignments = 0;
		
		while(assignRandom){
			this.addNewRandom();
			
			randomAssignments++;
			
			if(randomAssignments >= 2){
				assignRandom = false;
			}
		}
	}
	
	private Boolean addNewRandom() {
		if(this.isSpaceAvailable()){
			int row = new Random().nextInt(board.length);
			int col = new Random().nextInt(board[row].length);
			
			int randomValue = random.nextBoolean() ? 2 : 4;
			
			if(board[row][col].getText().equals(" ")){
				board[row][col].setText(Integer.toString(randomValue));
				board[row][col].setBackground(buttonColors.get("new"));
			} else {
				addNewRandom();
			}	
			
			return true;
		} else {
			// TODO throw exception for game over
			
			return false;
		}
	}
	
	private Boolean isSpaceAvailable(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){	
				if(board[i][j].getText().equals(" ")){
					return true;
				}
			}
		}			
		
		return false;
	}
	
	private void setEnableButtons(Boolean enable){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				board[i][j].setEnabled(enable);				
			}
		}		
	}	
	
	public int getPoints() {
		return points;
	}

	public void swipeUp() throws GameOverException {
		System.out.println("Up");
		
		moves = 0;

		setEnableButtons(true);
		
		for(int j = 0; j < board[0].length; j++){					
			for(int k = 1; k < board.length; k++){					
				compareCellsUp(k, j, -1);
			}			
		}
		
		setEnableButtons(false);
		
		if(moves > 0){
			if(!this.addNewRandom()){
				throw new GameOverException("No Available Space");
			}	
		}		
	}
	
	private void compareCellsUp(int row, int col, int direction){
		System.out.println("compareCellsUp");
		
		if(row > 0) {
			compareCellsUpDown(row, col, direction);
			
			this.compareCellsUp(row + direction, col, direction);
		}	
	}

	public void swipeDown() throws GameOverException {
		System.out.println("Down");
		
		moves = 0;
		
		setEnableButtons(true);
		
		for(int j = board.length - 1; j >= 0; j--) {
			for(int k = 0; k < board[j].length; k++){
				compareCellsDown(j, k, 1);
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
		System.out.println("compareCellsDown");
		
		if(row >= 0 && row <= board.length - 2) {
			compareCellsUpDown(row, col, direction);
			
			this.compareCellsDown(row + direction, col, direction);
		}	
	}	
	
	private void compareCellsUpDown(int row, int col, int direction) { 
		int currentValue = board[row + direction][col].getText().equals(" ") ? 0 :
			Integer.parseInt(board[row + direction][col].getText());					

		int nextValue = board[row][col].getText().equals(" ") ? 0 :
						Integer.parseInt(board[row][col].getText());
		
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
		
		board[row + direction][col].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
		board[row][col].setText(nextValue == 0 ? " " : Integer.toString(nextValue));	
		
		board[row + direction][col].setBackground(buttonColors.get(Integer.toString(currentValue)));
		board[row][col].setBackground(buttonColors.get(Integer.toString(nextValue)));
	}
	
	public void swipeLeft() throws GameOverException {
		System.out.println("Left");
		
		moves = 0;

		setEnableButtons(true);
		
		for(int j = 0; j < board[0].length; j++){					
			for(int k = 0; k < board.length; k++){					
				compareCellsLeft(k, j, -1);
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
		System.out.println("compareCellsLeft");
		
		if(col > 0) {
			compareCellsLeftRight(row, col, direction);
			
			this.compareCellsLeft(row, col  + direction, direction);
		}	
	}	
	
	public void swipeRight() throws GameOverException {
		System.out.println("Right");
		
		moves = 0;
		
		setEnableButtons(true);
		
		for(int j = 0 - 1; j < board.length; j++) {
			for(int k = board[j].length - 1; k >= 0; k--){
				compareCellsRight(k, j, -1);
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
		System.out.println("compareCellsRight");
				
		if(col >= 0 && col <= board[row].length - 2) {
			compareCellsLeftRight(row, col, direction);
			
			this.compareCellsRight(row + direction, col, direction);
		}	
	}	
	
	private void compareCellsLeftRight(int row, int col, int direction) { 
		int currentValue = board[row][col + direction].getText().equals(" ") ? 0 :
			Integer.parseInt(board[row][col + direction].getText());					

		int nextValue = board[row][col].getText().equals(" ") ? 0 :
						Integer.parseInt(board[row][col].getText());
		
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
		
		board[row][col + direction].setText(currentValue == 0 ? " " : Integer.toString(currentValue));
		board[row][col].setText(nextValue == 0 ? " " : Integer.toString(nextValue));	
		
		board[row][col + direction].setBackground(buttonColors.get(Integer.toString(currentValue)));
		board[row][col].setBackground(buttonColors.get(Integer.toString(nextValue)));		
	}	
}
