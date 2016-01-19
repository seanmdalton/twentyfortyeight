package com.dalton.twentyfortyeight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * @author github.com/seanmdalton
 *
 */
class twentyfortyeight {

	static Label pointsLabel;
	
	static Display display;
	static Shell shell;

	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		
		buildMenuBar(shell); //create the main controls via a menu bar
	
	    GridLayout layout = new GridLayout(1, false);  // create a new GridLayout with one column
	   
	    shell.setLayout(layout);  // set the layout to the shell	    
		shell.setText("twentyfortyeight"); //set the shell windowbar text
		
		// Shell can be used as container
		pointsLabel = new Label(shell, SWT.NONE);
		pointsLabel.setText("Points : 0");
		GridData data = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		data.widthHint = 100;
		
		pointsLabel.setLayoutData(data);
	    
	    // create a new label which is used as a separator
	    Label seplabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
	    data = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
	    seplabel .setLayoutData(data);

	    //create our Board object, which will be the "game"
		Board mainBoard = new Board(shell, SWT.NONE);		
	    data = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
	    mainBoard.setLayoutData(data);	    
	    
	    shell.pack(); //assuming all the sizes were set correctly in the board widget, pack the shell
		shell.open();
		
        display.addFilter(SWT.KeyDown, new BoardListener(mainBoard));

		// run the event loop as long as the window is open
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// disposes all associated windows and their components
		display.dispose(); 		
	}
	
	/**
	 * Create all the game controls via the menu bar
	 * 
	 * @param shell - the main shell initiated in the contructor
	 */
	public static void buildMenuBar(Shell shell){
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");
		
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
		
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
		
		shell.setMenuBar(menuBar);
		
		exitItem.addListener(SWT.Selection, event-> {
		    shell.getDisplay().dispose();
		    System.exit(0);
		});
		
	}
	
	/**
	 * 
	 */
	public static void newGame () {
		// TODO create newgame functionality
	}
	
	public static void gameOver() {
		// TODO create gameover functionality
	}
	
	/**
	 * @author github.com/seanmdalton
	 * 
	 * Create a nested static class specifically for listening on keyevents. Feed the keyevents to the Board object.
	 */
	static public class BoardListener implements Listener {
		Board Board;

		public BoardListener(Board Board){
			this.Board = Board;
		}
		
		@Override
		public void handleEvent(Event event) {
			try {
				switch (event.keyCode) {
					case 16777217: Board.swipeUp();
						break;
					case 16777218: Board.swipeDown();
						break;
					case 16777219: Board.swipeLeft();
						break;
					case 16777220: Board.swipeRight();
						break;
				}				
			} catch (GameOverException e) {
				// TODO game over code!
				
				gameOver();
			}
			
			pointsLabel.setText("Points : " + Integer.toString(Board.getPoints()));
		}
	}
}
