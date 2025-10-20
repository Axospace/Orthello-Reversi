public class ReversiController implements IController {

	IModel model;
	IView view;
	public void initialise( IModel model, IView view ) {
		this.model = model;
		this.view = view;
	}

	public void startup() {
		model.setFinished(false); 
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < height ; y++ )
				model.setBoardContents(x, y, 0);
		model.setBoardContents(3, 3, 1);	
		model.setBoardContents(4, 4, 1);
		model.setBoardContents(3, 4, 2);	
		model.setBoardContents(4, 3, 2);
		model.setPlayer(1);
		view.refreshView();
	}

	public void update() {
		
		if (!checkForAvailableMoves(model.getPlayer())) {
			model.setPlayer(op(model.getPlayer()));
			if (!checkForAvailableMoves(model.getPlayer())) {
				model.setPlayer(op(model.getPlayer()));
				model.setFinished(true);
			}
			else {
				model.setFinished(false);
			}
		} else {
			model.setFinished(false); 
		}
		
		if ( model.hasFinished() )
		{
			int totalWhite = getAmountOf(1);
			int totalBlack = getAmountOf(2);
			if (totalWhite > totalBlack) {
				view.feedbackToUser(1, "White won. White "+ totalWhite+ " to Black "+totalBlack+". Reset game to replay." );
				view.feedbackToUser(2, "White won. White "+ totalWhite+ " to Black "+totalBlack+". Reset game to replay." );
			} else if (totalWhite < totalBlack) {
				view.feedbackToUser(1, "Black won. Black "+ totalBlack+ " to White "+totalWhite+". Reset game to replay." );
				view.feedbackToUser(2, "Black won. Black "+ totalBlack+ " to White "+totalWhite+". Reset game to replay." );
			} else {
				view.feedbackToUser(1, "Draw. Both players ended with "+totalBlack+" pieces. Reset game to replay." );
				view.feedbackToUser(2, "Draw. Both players ended with "+totalBlack+" pieces. Reset game to replay." );
			}
			
		} else {
			setTurnMsg(model.getPlayer());
		}
		
		view.refreshView();
	}
	
	private int op(int a) { // Returns the opposite player
		if (a == 1) return 2;
		if (a == 2) return 1;
		return 0;
	}

	public void squareSelected( int player, int x, int y ) {
		if (model.getPlayer() == player) {
			if (model.getBoardContents(x, y) == 1 || model.getBoardContents(x, y) == 2) {
				view.feedbackToUser(player, "Cannot make move here, there is already a counter here!");
			} else {
				if (checkForTouchingCounters(x, y, player) && checkForAvailableMatch(x, y, player)) {
					horizontalOutflanks(x, y, player);
					verticalOutflanks(x, y, player);
					diagonalOutflanks(x, y, player);
					model.setBoardContents(x, y, player);	
					model.setPlayer(op(player));
					if (!checkForAvailableMoves(op(player))) {
						model.setPlayer(player);
						setTurnMsg(op(player));
						if (!checkForAvailableMoves(player)) {
							model.setPlayer(op(player));
							model.setFinished(true);
						}
					}
					else {
						setTurnMsg(player);
					}
				} else {
					view.feedbackToUser(player, "Invalid location to play a piece.");
					if (!checkForAvailableMoves(player)) {
						model.setPlayer(op(player));
						view.feedbackToUser(player, "No available turns, switching to other player!" );
						if (!checkForAvailableMoves(op(player))) {
							model.setPlayer(player);
							model.setFinished(true);
						}
					}
				}
			}
		} else {
			view.feedbackToUser(player, "It is not your turn!");
		}
		
		if ( model.hasFinished() )
		{
			int totalWhite = getAmountOf(1);
			int totalBlack = getAmountOf(2);
			if (totalWhite > totalBlack) {
				view.feedbackToUser(1, "White won. White "+ totalWhite+ " to Black "+totalBlack+". Reset game to replay." );
				view.feedbackToUser(2, "White won. White "+ totalWhite+ " to Black "+totalBlack+". Reset game to replay." );
			} else if (totalWhite < totalBlack) {
				view.feedbackToUser(1, "Black won. Black "+ totalBlack+ " to White "+totalWhite+". Reset game to replay." );
				view.feedbackToUser(2, "Black won. Black "+ totalBlack+ " to White "+totalWhite+". Reset game to replay." );
			} else {
				view.feedbackToUser(1, "Draw. Both players ended with "+totalBlack+" pieces. Reset game to replay." );
				view.feedbackToUser(2, "Draw. Both players ended with "+totalBlack+" pieces. Reset game to replay." );
			}
			
			return; // Don't do the set board contents
		}
		
		view.refreshView();
		
	}
	
	private int getAmountOf(int player) {
		int r = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (model.getBoardContents(i, j) == player) r++;
			}
		}
		return r;
	}
	private void setTurnMsg(int player) {
		if (model.getPlayer() == 2) {
			view.feedbackToUser(1, "White player - not your turn");
			view.feedbackToUser(2, "Black player - choose where to put your piece");
		}
		
		else {
			view.feedbackToUser(2, "Black player - not your turn");
			view.feedbackToUser(1, "White player - choose where to put your piece");
		}
	}
	
	private boolean checkForAvailableMoves(int player) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (model.getBoardContents(i, j) == 0 && checkForTouchingCounters(i, j, player) && checkForAvailableMatch(i, j, player)) return true;
			}
		}
		return false;
	}
	
	private boolean checkForTouchingCounters(int x, int y, int player) {
		if (x != 0) if (model.getBoardContents(x - 1, y) != 0) return true;
		if (x != 7) if (model.getBoardContents(x + 1, y) != 0) return true;
		if (y != 0) if (model.getBoardContents(x, y - 1) != 0) return true;
		if (y != 7) if (model.getBoardContents(x, y + 1) != 0) return true;
		if (x != 0 && y != 0) if (model.getBoardContents(x - 1, y - 1) != 0) return true;
		if (x != 7 && y != 0) if (model.getBoardContents(x + 1, y - 1) != 0) return true;
		if (x != 0 && y != 7) if (model.getBoardContents(x - 1, y + 1) != 0) return true;
		if (x != 7 && y != 7) if (model.getBoardContents(x + 1, y + 1) != 0) return true;
		return false;
	}
	
	private boolean checkForAvailableMatch(int x, int y, int player) {
		return (horizontalOutflankMatch(x, y, player) || verticalOutflankMatch(x, y, player) || diagonalOutflankMatch(x, y, player));
	}
	
	private int horizontalSquaresCaptured(int x, int y, int player) {
		boolean found = false;
		if (x!=0){
			for (int i = x-1; i>=0 && !found; i--) {
				if (model.getBoardContents(i, y) != op(player)) {
					if (model.getBoardContents(i, y) == player && i < x-1) return (x - 1 - i);
					found = true;
				}
			}
		}
		if (x!=7){
			found = false;
			for (int i = x+1; i<=7 && !found; i++) {
				if (model.getBoardContents(i, y) != op(player)) {
					if (model.getBoardContents(i, y) == player && i > x+1) return (i - x - 1);
					found = true;
				}
			}
		}
		return 0;
	}
	
	private int verticalSquaresCaptured(int x, int y, int player) {
		boolean found = false;
		if (y!=0){
			found = false;
			for (int i = y-1; i>=0 && !found; i--) {
				if (model.getBoardContents(x, i) != op(player)) {
					if (model.getBoardContents(x, i) == player && i < y-1) return (y - 1 - i);
					found = true;
				}
			}
		}
		if (y!=7){
			found = false;
			for (int i = y+1; i<=7 && !found; i++) {
				if (model.getBoardContents(x, i) != op(player)) {
					if (model.getBoardContents(x, i) == player && i > y+1) return (i - y - 1);
					found = true;
				}
			}
		}
		return 0;
	}
	
	private int diagonalSquaresCaptured(int x, int y, int player) {
		boolean found = false;
		int j = 0;
		
		if (y!=0 && x!=0){
			found = false;
			j = y-1;
			for (int i = x-1; i>=0 && j>=0 && !found; i--) {
				if (model.getBoardContents(i, j) != op(player)) {
					if (model.getBoardContents(i, j) == player && i < x-1) return (x - 1 - i);
					found = true;
				} else j--;
			}
		}
		
		if (y!=0 && x!=7){
			found = false;
			j = y-1;
			for (int i = x+1; i<=7 && j>=0 && !found; i++) {
				if (model.getBoardContents(i, j) != op(player)) {
					if (model.getBoardContents(i, j) == player && i > x+1) return (i - x - 1);
					found = true;
				} else j--;
			}
		}
		
		if (y!=7 && x!=0){
			found = false;
			j = y+1;
			for (int i = x-1; i>=0 && j<=7 && !found; i--) {
				if (model.getBoardContents(i, j) != op(player)) {
					if (model.getBoardContents(i, j) == player && i < x-1) return (x - 1 - i);
					found = true;
				} else j++;
			}
		}
		
		if (y!=7 && x!=7){
			found = false;
			j = y+1;
			for (int i = x+1; i<=7 && j<=7 && !found; i++) {
				if (model.getBoardContents(i, j) != op(player)) {
					if (model.getBoardContents(i, j) == player && i > x+1) return (i - x - 1);
					found = true;
				} else j++;
			}
		}
		return 0;
	}
	
	private int totalSquaresCaptured(int x, int y, int player) {
		return diagonalSquaresCaptured(x, y, player) + horizontalSquaresCaptured(x, y, player) + verticalSquaresCaptured(x, y, player);
	}
	
	private boolean horizontalOutflankMatch(int x, int y, int player) {
		return (horizontalSquaresCaptured(x, y, player) > 0);
	}
	
	private boolean verticalOutflankMatch(int x, int y, int player) {
		return (verticalSquaresCaptured(x, y, player) > 0);
	}
	
	private boolean diagonalOutflankMatch(int x, int y, int player) {
		return (diagonalSquaresCaptured(x, y, player) > 0);
	}
	
	private void horizontalOutflanks(int x, int y, int player) {
		boolean found = false;
		int tempi = 0;
		if (x!=0){
			for (int i = x-1; i>=0 && !found; i--) {
				if (model.getBoardContents(i, y) != op(player)) {
					found = true;
					tempi = i;
				}
			}
			if (found && (model.getBoardContents(tempi, y) != 0)) {
				for (int i = tempi; i < x; i++) {
					model.setBoardContents(i, y, player);	
				}
			}
		}
		if (x!=7){
			found = false;
			for (int i = x+1; i<=7 && !found; i++) {
				if (model.getBoardContents(i, y) != op(player)) {
					found=true;
					tempi = i;
				}
			}
			if (found && (model.getBoardContents(tempi, y) != 0)) {
				for (int i = tempi; i > x; i--) {
					model.setBoardContents(i, y, player);	
				}
			}
		}
	}
	
	private void verticalOutflanks(int x, int y, int player) {
		boolean found = false;
		int tempi = 0;
		if (y!=0){
			found = false;
			for (int i = y-1; i>=0 && !found; i--) {
				if (model.getBoardContents(x, i) != op(player)) {
					found=true;
					tempi = i;
				}
			}
			if (found && (model.getBoardContents(x, tempi) != 0)) {
				for (int i = tempi; i < y; i++) {
					model.setBoardContents(x, i, player);	
				}
			}
		}
		if (y!=7){
			found = false;
			for (int i = y+1; i<=7 && !found; i++) {
				if (model.getBoardContents(x, i) != op(player)) {
					found=true;
					tempi = i;
				}
			}
			if (found && (model.getBoardContents(x, tempi) != 0)) {
				for (int i = tempi; i > y; i--) {
					model.setBoardContents(x, i, player);
				}
			}
		}
	}
	
	private void diagonalOutflanks(int x, int y, int player) {
		boolean found = false;
		int j = 0;
		int tempi = 0;
		
		if (y!=0 && x!=0){
			found = false;
			j = y-1;
			for (int i = x-1; i>=0 && j>=0 && !found; i--) {
				if (model.getBoardContents(i, j) != op(player)) {
					found=true;
					tempi = i;
				} else j--;
			}
			if (found && model.getBoardContents(tempi, j) != 0) {
				for (int i = tempi; i < x; i++) {
					model.setBoardContents(i, j, player);
					j++;
					
				}
			}
		}
		
		if (y!=0 && x!=7){
			found = false;
			j = y-1;
			for (int i = x+1; i<=7 && j>=0 && !found; i++) {
				if (model.getBoardContents(i, j) != op(player)) {
					found=true;
					tempi = i;
				} else j--;
				
			}
			if (found && model.getBoardContents(tempi, j) != 0) {
				for (int i = tempi; i > x; i--) {
					model.setBoardContents(i, j, player);	
					j++;
				}
			}
		}
		
		if (y!=7 && x!=0){
			found = false;
			j = y+1;
			for (int i = x-1; i>=0 && j<=7 && !found; i--) {
				if (model.getBoardContents(i, j) != op(player)) {
					found=true;
					tempi = i;
				} else j++;
			}
			if (found && model.getBoardContents(tempi, j) != 0) {
				for (int i = tempi; i < x; i++) {
					model.setBoardContents(i, j, player);	
					j--;
				}
			}
		}
		
		if (y!=7 && x!=7){
			found = false;
			j = y+1;
			for (int i = x+1; i<=7 && j<=7 && !found; i++) {
				if (model.getBoardContents(i, j) != op(player)) {
					found=true;
					tempi = i;
				} else j++;
			}
			if (found && model.getBoardContents(tempi, j) != 0) {
				for (int i = tempi; i > x; i--) {
					model.setBoardContents(i, j, player);	
					j--;
				}
			}
		}
	}

	
	public void doAutomatedMove( int player ) {
		int xmax = -1;
		int ymax = -1;
		int rmax = 0;
		int r = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (model.getBoardContents(i, j) == 0) {
					r = totalSquaresCaptured(i, j, player);
					if (r > rmax) {
						rmax = r;
						xmax = i;
						ymax = j;
					}
				}
			}
		}
		if (xmax != -1 && ymax != -1 && rmax > 0) {
			this.squareSelected(player, xmax, ymax);
			
		}
	}
}











