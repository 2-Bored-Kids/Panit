import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DrawingPanel extends JPanel {
  private Main main;
  private JFrame frame;

  public DrawingPanel(Main mainInstance, JFrame mainFrame) { main = mainInstance; frame = mainFrame; }

  @Override
  public void paintComponent(Graphics g) {
    main.getPen().drawToGraphics(g);

    //Hack to prevent the ui rendering from breaking, causes components to get redrawn too often. Too bad!
    frame.repaint();
  }
}
