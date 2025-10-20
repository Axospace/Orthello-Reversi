import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardSquareListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		BoardSquare square = (BoardSquare) e.getSource();
	    square.click();
	}
}
