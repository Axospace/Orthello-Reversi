import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AIListener implements ActionListener {
    private GUIView gui;
    private int player;

    public AIListener(GUIView gui, int player) {
        this.gui = gui;
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gui.ai(player);
    }
}