import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawingPanel extends JPanel {
  private Main main;
  private JFrame frame;

  // Setting main attributes
  public DrawingPanel(Main mainInstance, JFrame mainFrame) {
    main = mainInstance;
    frame = mainFrame;
  }

  @Override
  public void paintComponent(Graphics g) {
    // Draw the main image to the panel
    Main.instance.getPen().drawToGraphics(g);

    // Hack to prevent the UI rendering from breaking, causes components to get
    // redrawn too often. Too bad!
    frame.repaint();
  }
}
