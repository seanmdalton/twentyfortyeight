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

class twentyfortyeight {
	
	static int points = 0;
	static Label pointsLabel;
	
	static Display display;
	static Shell shell;

	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		
		buildMenuBar(shell);
	
	    // create a new GridLayout with two columns
	    // of different size
	    GridLayout layout = new GridLayout(1, false);

	    // set the layout to the shell
	    shell.setLayout(layout);
		shell.setText("twentyfortyeight");
		
		// Shell can be used as container
		pointsLabel = new Label(shell, SWT.NONE);
		pointsLabel.setText("Points : " + points);
		GridData data = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		data.widthHint = 100;
		
		pointsLabel.setLayoutData(data);
	    
	    // create a new label which is used as a separator
	    Label seplabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
	    data = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
	    seplabel .setLayoutData(data);

		Board mainBoard = new Board(shell, SWT.NONE);		
	    data = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
	    mainBoard.setLayoutData(data);	    
	    
	    shell.pack();
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
	
	public static void newGame () {
		
	}
	
	public static void gameOver() {
		System.out.println("gameOver");
		
	    // create a new label which is used as a separator
	    Label gameOverLabel = new Label(shell, SWT.NONE);
	    gameOverLabel.setText("Game Over!");
	    gameOverLabel.setSize(50, 100);
	    gameOverLabel.setLocation(50, 100);
	    
	}
	
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
			
//			Board.drawBoard();
			
			pointsLabel.setText("Points : " + Integer.toString(Board.getPoints()));
		}
	}
}
