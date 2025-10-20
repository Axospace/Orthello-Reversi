import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.*;

public class BoardSquare extends JButton {
	
	int state;
	int x, y;
	int playerno;
	IController c;
	BorderFactory borderFactory;
	
	public BoardSquare(int state, IController c, int x, int y, int playerno) {
		this.addActionListener(new BoardSquareListener());
		this.x = x;
		this.y = y;
		this.c = c;
		this.playerno = playerno;
		updateState(state);
	}
	
	public void updateState(int state) {
		this.state = state;
		this.setBackground(Color.GREEN);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		repaint();
	}
	
	@Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (state == 2) {
        	graphics.setColor(Color.WHITE);
        	graphics.fillOval(2, 2, 46, 46);
        	graphics.setColor(Color.BLACK);
        	graphics.fillOval(4, 4, 42, 42);
        } else if (state == 1) {
        	graphics.setColor(Color.BLACK);
        	graphics.fillOval(2, 2, 46, 46);
        	graphics.setColor(Color.WHITE);
        	graphics.fillOval(4, 4, 42, 42);
        }
    }
	
	public void refreshStateGUI() {
		repaint();
	}
	
	public void click() {
		c.squareSelected(playerno, x, y);
	}

}
