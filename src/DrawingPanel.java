import java.awt.Graphics;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
  private Main main;

  public DrawingPanel(Main mainInstance) { main = mainInstance; }

  @Override
  public void paintComponent(Graphics g) {
    main.getPen().drawToGraphics(g);
  }
}
