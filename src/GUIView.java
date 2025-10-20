import java.awt.GridLayout;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUIView implements IView{
	IModel model;
	IController controller;
	
	JFrame frame1;
	JFrame frame2;
	
	String msg1 = "White player - choose where to put your piece";
	String msg2 = "Black player - not your turn";
	
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	
	public void initialise( IModel model, IController controller ) {
		
		// Step 1: Assigns model and controller references to be global in the class
		this.model = model;
		this.controller = controller;
		
		// Step 2: Sets the frames and creates buttons for the two views
		this.frame1 = new JFrame("Reversi - white player");
		this.frame2 = new JFrame("Reversi - black player");
		
		JButton button1 = new JButton("Greedy AI");
		JButton button2 = new JButton("Restart");
		button1.addActionListener(new AIListener(this, 1));
		button2.addActionListener(new RestartListener(this));
		
		JButton button3 = new JButton("Greedy AI");
		JButton button4 = new JButton("Restart");
		button3.addActionListener(new AIListener(this, 2));
		button4.addActionListener(new RestartListener(this));
		
		frame1.setLayout(new BorderLayout());
		frame2.setLayout(new BorderLayout());
		
		// Step 3: Creates grid layouts for the frames
		this.panel1 = new JPanel();
		panel1.setLayout(new GridLayout(model.getBoardWidth(), model.getBoardHeight()));
		this.panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 2));
		this.panel3 = new JPanel();
		panel3.setLayout(new GridLayout(model.getBoardWidth(), model.getBoardHeight()));
		this.panel4 = new JPanel();
		panel4.setLayout(new GridLayout(1, 2));
		
		panel1.setPreferredSize(new Dimension(model.getBoardWidth() * 50, model.getBoardHeight() * 50));
		panel3.setPreferredSize(new Dimension(model.getBoardWidth() * 50, model.getBoardHeight() * 50));
		
		// Step 4: Configures frame 1
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.getContentPane().setLayout(new GridLayout(3, 1));
        frame1.add(new JLabel("game game player 1", SwingConstants.CENTER), BorderLayout.NORTH);
        panel2.add(button1);
        panel2.add(button2);
        frame1.add(panel1, BorderLayout.CENTER);
        frame1.add(panel2, BorderLayout.SOUTH);
        frame1.pack();
        frame1.setVisible(true);
        
		// Step 5: Configures frame 2
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        frame2.getContentPane().setLayout(new GridLayout(3, 1));
        frame2.add(new JLabel("game game player 2", SwingConstants.CENTER), BorderLayout.NORTH);
        panel4.add(button3);
        panel4.add(button4);
        frame2.add(panel3, BorderLayout.CENTER);
        frame2.add(panel4, BorderLayout.SOUTH);
        frame2.pack();
        frame2.setVisible(true);
		
		
		this.refreshView();
	}
	
	public void refreshView() {
		panel1.removeAll();
		panel3.removeAll();
		int temp; 
		
		
		
		for (int i = 0; i < model.getBoardWidth() * model.getBoardHeight(); i++) {
			temp = model.getBoardContents(i % model.getBoardWidth(), i / model.getBoardHeight());
			BoardSquare square = new BoardSquare(temp, controller, i % model.getBoardWidth(), i / model.getBoardHeight(), 1);
			panel1.add(square);
		}
		
		for (int i = model.getBoardWidth() * model.getBoardHeight() - 1; i > -1; i--) {
			temp = model.getBoardContents(i % model.getBoardWidth(), i / model.getBoardHeight());
			BoardSquare square = new BoardSquare(temp, controller, i % model.getBoardWidth(), i / model.getBoardHeight(), 2);
			panel3.add(square);
		}
		
		frame1.getContentPane().removeAll();
		frame2.getContentPane().removeAll();
		
		frame1.setLayout(new BorderLayout());
        frame1.add(new JLabel(msg1, SwingConstants.CENTER), BorderLayout.NORTH);
        frame1.add(panel1, BorderLayout.CENTER);
        frame1.add(panel2, BorderLayout.SOUTH);
        frame1.pack();
        
        frame2.setLayout(new BorderLayout());
		frame2.add(new JLabel(msg2, SwingConstants.CENTER), BorderLayout.NORTH);
		frame2.add(panel3, BorderLayout.CENTER);
        frame2.add(panel4, BorderLayout.SOUTH);
        frame2.pack();
		
		
	}
	
	public void feedbackToUser( int player, String message ) {
		if (player == 1) msg1 = message;
		else if (player == 2) msg2 = message;
		this.refreshView();
		
	}
	
	public void restart() {
		controller.startup();
	}
	
	public void ai(int player) {
		controller.doAutomatedMove(player);
	}
	
}

