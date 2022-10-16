import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawingPanel extends JPanel {
  private JFrame frame;

  public DrawingPanel(JFrame mainFrame) { frame = mainFrame; }

  @Override
  public void paintComponent(Graphics g) {
    // Draw the main image to the panel
    Main.instance.getPen().drawToGraphics(g);

    // Hack to prevent the UI rendering from breaking, causes components to get
    // redrawn too often. Too bad!
    frame.repaint();
  }
}
